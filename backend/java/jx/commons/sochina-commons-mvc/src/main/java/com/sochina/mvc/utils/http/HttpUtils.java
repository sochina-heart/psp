package com.sochina.mvc.utils.http;

import com.sochina.base.constants.Constants;
import com.sochina.base.constants.Headers;
import com.sochina.base.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class HttpUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url 发送请求的 URL
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url) {
        return sendGet(url, StringUtils.EMPTY);
    }

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        return sendGet(url, param, Constants.UTF8);
    }

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url         发送请求的 URL
     * @param param       请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param contentType 编码类型
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, String contentType) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String urlNameString = StringUtils.isNotBlank(param) ? url + "?" + param : url;
            LOGGER.info("sendGet - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty(Headers.ACCEPT, "*/*");
            connection.setRequestProperty(Headers.CONNECTION, "Keep-Alive");
            connection.setRequestProperty(Headers.USER_AGENT, "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), contentType));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            LOGGER.info("recv - {}", result);
        } catch (ConnectException e) {
            LOGGER.error("调用HttpUtils.sendGet ConnectException, url=" + url + ",param=" + param, e);
        } catch (SocketTimeoutException e) {
            LOGGER.error("调用HttpUtils.sendGet SocketTimeoutException, url=" + url + ",param=" + param, e);
        } catch (IOException e) {
            LOGGER.error("调用HttpUtils.sendGet IOException, url=" + url + ",param=" + param, e);
        } catch (Exception e) {
            LOGGER.error("调用HttpsUtil.sendGet Exception, url=" + url + ",param=" + param, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                LOGGER.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
            }
        }
        return result.toString();
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            LOGGER.info("sendPost - {}", url);
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty(Headers.ACCEPT, "*/*");
            conn.setRequestProperty(Headers.CONNECTION, "Keep-Alive");
            conn.setRequestProperty(Headers.USER_AGENT, "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty(Headers.ACCEPT_CHARSET, "utf-8");
            conn.setRequestProperty(Headers.CONTENT_TYPE, "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            LOGGER.info("recv - {}", result);
        } catch (ConnectException e) {
            LOGGER.error("调用HttpUtils.sendPost ConnectException, url=" + url + ",param=" + param, e);
        } catch (SocketTimeoutException e) {
            LOGGER.error("调用HttpUtils.sendPost SocketTimeoutException, url=" + url + ",param=" + param, e);
        } catch (IOException e) {
            LOGGER.error("调用HttpUtils.sendPost IOException, url=" + url + ",param=" + param, e);
        } catch (Exception e) {
            LOGGER.error("调用HttpsUtil.sendPost Exception, url=" + url + ",param=" + param, e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                LOGGER.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
            }
        }
        return result.toString();
    }

    public static String sendSSLPost(String url, String param) {
        StringBuilder result = new StringBuilder();
        String urlNameString = url + "?" + param;
        try {
            LOGGER.info("sendSSLPost - {}", urlNameString);
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
            URL console = new URL(urlNameString);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setRequestProperty(Headers.ACCEPT, "*/*");
            conn.setRequestProperty(Headers.CONNECTION, "Keep-Alive");
            conn.setRequestProperty(Headers.USER_AGENT, "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty(Headers.ACCEPT_CHARSET, "utf-8");
            conn.setRequestProperty(Headers.CONTENT_TYPE, "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String ret = "";
            while ((ret = br.readLine()) != null) {
                if (StringUtils.isNotEmpty(ret)) {
                    result.append(new String(ret.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
                }
            }
            LOGGER.info("recv - {}", result);
            conn.disconnect();
            br.close();
        } catch (ConnectException e) {
            LOGGER.error("调用HttpUtils.sendSSLPost ConnectException, url=" + url + ",param=" + param, e);
        } catch (SocketTimeoutException e) {
            LOGGER.error("调用HttpUtils.sendSSLPost SocketTimeoutException, url=" + url + ",param=" + param, e);
        } catch (IOException e) {
            LOGGER.error("调用HttpUtils.sendSSLPost IOException, url=" + url + ",param=" + param, e);
        } catch (Exception e) {
            LOGGER.error("调用HttpsUtil.sendSSLPost Exception, url=" + url + ",param=" + param, e);
        }
        return result.toString();
    }

    /**
     * 获取请求方法
     *
     * @param request
     * @return 方法
     */
    public static String getMethod(HttpServletRequest request) {
        return request.getMethod();
    }

    /**
     * 获取所有headerName
     *
     * @param request
     * @return headerName列表
     */
    public static List<String> getHeaderNames(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        return Collections.list(headerNames);
    }

    /**
     * 判断指定请求头是否存在
     *
     * @param request
     * @param header
     * @return true/false
     */
    public static Boolean headerIsExist(HttpServletRequest request, String header) {
        boolean result = false;
        List<String> headerNames = getHeaderNames(request);
        if (headerNames.stream().anyMatch(item -> item.equalsIgnoreCase(header))) {
            result = true;
        }
        return result;
    }

    /**
     * 获取指定header
     *
     * @param request
     * @param header
     * @return header
     */
    public static String getHeader(HttpServletRequest request, String header) {
        return request.getHeader(header);
    }

    private static class TrustAnyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}