package com.sochina.gateway.filter;

import com.sochina.base.constants.Constants;
import com.sochina.base.constants.HttpStatus;
import com.sochina.base.utils.StringUtils;
import com.sochina.gateway.domain.properties.BlackProperties;
import com.sochina.gateway.domain.properties.SochinaProperties;
import com.sochina.webflux.utils.WebFluxIpUtil;
import com.sochina.webflux.utils.WebFluxResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public class BlackFilter implements WebFilter, Ordered {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlackFilter.class);
    private static List<String> IP_LIST;
    private static List<String> URL_LIST;
    private final SochinaProperties sochinaProperties;

    public BlackFilter(SochinaProperties sochinaProperties) {
        LOGGER.info("init BlackFilter start");
        this.sochinaProperties = sochinaProperties;
        IP_LIST = Optional.ofNullable(sochinaProperties.getBlackProperties())
                .map(BlackProperties::getIpList)
                .orElse(Constants.EMPTY_LIST);
        URL_LIST = Optional.ofNullable(sochinaProperties.getBlackProperties())
                .map(BlackProperties::getUrlList)
                .orElse(Constants.EMPTY_LIST);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        ServerHttpRequest request = serverWebExchange.getRequest();
        String ipAddress = WebFluxIpUtil.getIpAddress(request);
        if (StringUtils.matches(ipAddress, IP_LIST)) {
            LOGGER.info("{}-该ip不允许访问服务", ipAddress);
            return WebFluxResponseUtil.webFluxResponseWriter(serverWebExchange.getResponse(), "该ip不允许访问服务", HttpStatus.BLACK_IP);
        }
        String path = request.getURI().getPath();
        if (StringUtils.matches(path, URL_LIST)) {
            LOGGER.info("{}-该接口不允许访问", path);
            return WebFluxResponseUtil.webFluxResponseWriter(serverWebExchange.getResponse(), "该接口不允许访问", HttpStatus.BLACK_URL);
        }
        return webFilterChain.filter(serverWebExchange);
    }

    @Override
    public int getOrder() {
        return sochinaProperties.getBlackProperties().getOrder();
    }
}
