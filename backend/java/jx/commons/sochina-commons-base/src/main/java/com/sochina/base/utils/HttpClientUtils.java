package com.sochina.base.utils;

import com.alibaba.fastjson2.JSONObject;
import com.sochina.base.constants.Constants;
import com.sochina.base.domain.properties.httpClient.HttpClientRequestProperties;
import com.sochina.base.domain.properties.httpClient.HttpClientUploadProperties;
import com.sochina.base.exception.HttpClientException;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class HttpClientUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);
    private static RequestConfig REQUEST_CONFIG;
    private static RequestConfig UPLOAD_CONFIG;

    public HttpClientUtils(HttpClientRequestProperties requestProperties, HttpClientUploadProperties uploadProperties) {
        REQUEST_CONFIG = RequestConfig.custom()
                // 客户端和服务端建立连接超时时间
                .setConnectTimeout(requestProperties.getConnectTimeout())
                // 从连接池获取连接的超时时间
                .setConnectionRequestTimeout(requestProperties.getConnectRequestTimeout())
                // 客户端从服务器读取数据的超时时间
                .setSocketTimeout(requestProperties.getSocketTimeout())
                .build();
        UPLOAD_CONFIG = RequestConfig.custom()
                // 客户端和服务端建立连接超时时间
                .setConnectTimeout(uploadProperties.getConnectTimeout())
                // 从连接池获取连接的超时时间
                .setConnectionRequestTimeout(uploadProperties.getConnectRequestTimeout())
                // 客户端从服务器读取数据的超时时间
                .setSocketTimeout(uploadProperties.getSocketTimeout())
                .build();
    }

    /**
     * 处理http get请求headers
     *
     * @param headers 请求头
     * @param httpGet httpGet
     */
    private static void handleHeadersOfGet(Map<String, Collection<String>> headers, HttpGet httpGet) {
        Optional.ofNullable(headers)
                .orElse(Collections.emptyMap())
                .entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                .forEach(entry -> httpGet.setHeader(entry.getKey(), String.valueOf(entry.getValue())));
    }

    /**
     * 处理http post请求headers
     *
     * @param headers  请求头
     * @param httpPost httpPost
     */
    private static void handleHeadersOfPost(Map<String, Collection<String>> headers, HttpPost httpPost) {
        Optional.ofNullable(headers)
                .orElse(Collections.emptyMap())
                .entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                .forEach(entry -> httpPost.setHeader(entry.getKey(), String.valueOf(entry.getValue())));
    }

    /**
     * 处理http get请求参数
     *
     * @param params  参数
     * @param builder
     */
    private static void handleParamsOfGet(Map<String, Object> params, URIBuilder builder) {
        Optional.ofNullable(params)
                .orElse(Collections.emptyMap())
                .entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                .forEach(entry -> builder.addParameter(entry.getKey(), String.valueOf(entry.getValue())));
    }

    /**
     * 发送get请求，接收json响应数据
     *
     * @param url     访问地址，无query参数
     * @param params  query参数
     * @param headers 请求头
     * @return
     */
    public JSONObject doGet(String url, Map<String, Object> params, Map<String, Collection<String>> headers) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultStr;
        try {
            URIBuilder builder = new URIBuilder(url);
            handleParamsOfGet(params, builder);
            URI uri = builder.build();
            LOGGER.debug("http 请求地址：{}", url);
            if (!params.isEmpty()) {
                LOGGER.debug("http 请求参数：{}", params);
            }
            // 创建http get请求
            HttpGet httpGet = new HttpGet(uri);
            handleHeadersOfGet(headers, httpGet);
            httpGet.setConfig(REQUEST_CONFIG);
            // 执行请求
            response = httpClient.execute(httpGet);
            resultStr = responseHandle(response);
        } catch (URISyntaxException | IOException e) {
            LOGGER.error("http 发送请求异常 url：{}", url, e);
            throw new HttpClientException(e);
        } finally {
            streamClose(response, httpClient);
        }
        return JSONObject.parseObject(resultStr);
    }

    /**
     * 发送post请求，上传byte
     *
     * @param url       访问地址，无query参数
     * @param queryName 参数名
     * @param bytes
     * @param fileName  文件名
     * @param headers   请求头
     * @return
     */
    public JSONObject doPostBinaryBody(String url, String queryName, byte[] bytes, String fileName, Map<String, Collection<String>> headers) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultStr;
        try {
            LOGGER.debug("http 请求地址：{}", url);
            HttpPost httpPost = new HttpPost(url);
            handleHeadersOfPost(headers, httpPost);
            httpPost.setConfig(UPLOAD_CONFIG);
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.addBinaryBody(queryName, bytes, ContentType.MULTIPART_FORM_DATA, fileName);
            httpPost.setEntity(multipartEntityBuilder.build());
            response = httpClient.execute(httpPost);
            resultStr = responseHandle(response);
        } catch (IOException e) {
            LOGGER.error("http 发送请求异常 url：{}", url, e);
            throw new HttpClientException(e);
        } finally {
            streamClose(response, httpClient);
        }
        return JSONObject.parseObject(resultStr);
    }

    /**
     * 发送post请求，form-data数据传输
     *
     * @param url      访问地址，无query参数
     * @param formData query参数
     * @param headers  请求头
     * @return
     */
    public JSONObject doPostFormData(String url, Map<String, Object> formData, Map<String, Collection<String>> headers) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultStr;
        try {
            LOGGER.debug("http 请求地址：{}", url);
            HttpPost httpPost = new HttpPost(url);
            handleHeadersOfPost(headers, httpPost);
            httpPost.setConfig(REQUEST_CONFIG);
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            formData.keySet().stream()
                    .filter(Objects::nonNull)
                    .forEach(key -> multipartEntityBuilder.addTextBody(key, String.valueOf(formData.get(key)), ContentType.MULTIPART_FORM_DATA));
            httpPost.setEntity(multipartEntityBuilder.build());
            response = httpClient.execute(httpPost);
            resultStr = responseHandle(response);
        } catch (IOException e) {
            LOGGER.error("http 发送请求异常 url：{}", url, e);
            throw new HttpClientException(e);
        } finally {
            streamClose(response, httpClient);
        }
        return JSONObject.parseObject(resultStr);
    }

    /**
     * 发送post请求，接收json响应数据
     * post application/x-www-form-urlencoded
     *
     * @param url     访问地址，无query参数
     * @param params  query参数
     * @param headers 请求头
     * @return
     */
    public JSONObject doPost(String url, Map<String, Object> params, Map<String, Collection<String>> headers) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultStr;
        try {
            LOGGER.debug("http 请求地址：{}", url);
            HttpPost httpPost = new HttpPost(url);
            handleHeadersOfPost(headers, httpPost);
            httpPost.setConfig(REQUEST_CONFIG);
            if (params != null) {
                List<NameValuePair> paramList = params.keySet().stream()
                        .filter(Objects::nonNull)
                        .map(key -> new BasicNameValuePair(key, String.valueOf(params.get(key))))
                        .collect(Collectors.toList());
                // 模拟form data
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            response = httpClient.execute(httpPost);
            resultStr = responseHandle(response);
        } catch (IOException e) {
            LOGGER.error("http 发送请求异常 url：{}", url, e);
            throw new HttpClientException(e);
        } finally {
            streamClose(response, httpClient);
        }
        return JSONObject.parseObject(resultStr);
    }

    /**
     * 发送post请求，接收json响应数据
     * post application/json
     *
     * @param url     访问路径，无query参数
     * @param json    query参数
     * @param headers 请求头
     * @return
     */
    public JSONObject doPostJson(String url, String json, Map<String, Collection<String>> headers) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultStr;
        try {
            LOGGER.debug("http 请求路径：{}", url);
            HttpPost httpPost = new HttpPost(url);
            handleHeadersOfPost(headers, httpPost);
            httpPost.setConfig(REQUEST_CONFIG);
            if (StringUtils.isNoneBlank(json)) {
                StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
                httpPost.setEntity(entity);
            }
            response = httpClient.execute(httpPost);
            resultStr = responseHandle(response);
        } catch (IOException e) {
            LOGGER.error("http 发送请求异常 url：{}", url, e);
            throw new HttpClientException(e);
        } finally {
            streamClose(response, httpClient);
        }
        return JSONObject.parseObject(resultStr);
    }

    /**
     * http流关闭
     *
     * @param response
     * @param httpClient
     */
    private void streamClose(CloseableHttpResponse response, CloseableHttpClient httpClient) {
        try {
            if (response != null) {
                response.close();
            }
            httpClient.close();
        } catch (IOException e) {
            LOGGER.error("http 关闭流异常", e);
        }
    }

    /**
     * http响应处理
     *
     * @param response
     * @return
     */
    private String responseHandle(CloseableHttpResponse response) {
        String resultStr;
        if (response.getStatusLine().getStatusCode() == 200) {
            try {
                resultStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            LOGGER.debug("http 响应内容：{}", resultStr);
        } else {
            LOGGER.error("http 响应状态码：{}", response.getStatusLine().getStatusCode());
            throw new HttpClientException("http 请求失败 状态码：{}", response.getStatusLine().getStatusCode());
        }
        return Constants.EMPTY_STRING;
    }
}
