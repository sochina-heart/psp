package com.sochina.webflux.utils;

import com.sochina.base.constants.Constants;
import com.sochina.base.constants.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebFluxIpUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebFluxIpUtil.class);
    private static final String IP_UNKNOWN = "unknown";
    private static final int IP_LEN = 15;
    private static final Pattern IP_PATTERN = Pattern.compile("((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}");

    public static String getIpAddress(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ipAddress = headers.getFirst(Headers.X_FORWARDED_FOR);
        if (ipAddress == null || ipAddress.isEmpty() || IP_UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = headers.getFirst(Headers.PROXY_CLIENT_IP);
        }
        if (ipAddress == null || ipAddress.isEmpty() || IP_UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = headers.getFirst(Headers.WL_PROXY_CLIENT_IP);
        }
        if (ipAddress == null || ipAddress.isEmpty() || IP_UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = headers.getFirst(Headers.X_REAL_IP);
        }
        if (ipAddress == null || ipAddress.isEmpty() || IP_UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = Optional.ofNullable(request.getRemoteAddress())
                    .map(address -> address.getAddress().getHostAddress())
                    .orElse(Constants.EMPTY_STRING);
            if (Constants.IP_LOCAL.equals(ipAddress) || Constants.IPV6_LOCAL.equals(ipAddress)) {
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }
                ipAddress = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > IP_LEN) {
            int index = ipAddress.indexOf(Constants.COMMA);
            if (index > 0) {
                ipAddress = ipAddress.substring(0, index);
            }
        }
        return ipAddress;
    }

    /**
     * 获取客户端端口
     *
     * @param request
     * @return
     */
    public static Integer getPort(ServerHttpRequest request) {
        return Objects.requireNonNull(request.getRemoteAddress()).getPort();
    }

    /**
     * 验证IP字符串是否合规
     *
     * @param ip ip字符串
     * @return true标识合规
     */
    public static boolean validIp(String ip) {
        Matcher m = IP_PATTERN.matcher(ip);
        return m.matches();
    }
}
