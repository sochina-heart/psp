package com.sochina.base.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.sochina.base.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class RestTemplateUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateUtils.class);

    /**
     * get 请求
     *
     * @param restTemplate
     * @param url          路径
     * @param headerMap    请求头
     * @param paramMap     参数
     * @return
     */
    public static String get(RestTemplate restTemplate, String url, Map<String, String> headerMap, Map<String, String> paramMap) {
        HttpHeaders headers = new HttpHeaders();
        if (!CollectionUtil.isEmpty(headerMap)) {
            headerMap.forEach(headers::set);
        }
        StringBuffer paramStr = new StringBuffer(Constants.EMPTY_STRING);
        if (!CollectionUtil.isEmpty(paramMap)) {
            paramMap.forEach((k, v) -> {
                if (paramStr.toString().equals(Constants.EMPTY_STRING)) {
                    paramStr.append(Constants.HALF_ASK_MARK).append(k).append(Constants.EQUAL_SIGN).append(v);
                } else {
                    paramStr.append(Constants.AND_SIGN).append(k).append(Constants.EQUAL_SIGN).append(v);
                }
            });
        }
        ResponseEntity<String> httpEntity = restTemplate.exchange(url + paramStr.toString(), HttpMethod.GET, CollectionUtil.isEmpty(headerMap) ? null : new HttpEntity<>(headers), String.class);
        return httpEntity.getBody();
    }

    /**
     * post 请求 json
     *
     * @param restTemplate
     * @param url            路径
     * @param headerMap      请求头
     * @param paramObjectStr 参数
     * @return
     */
    public static String postJson(RestTemplate restTemplate, String url, Map<String, String> headerMap, String paramObjectStr) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (!CollectionUtil.isEmpty(headerMap)) {
            headerMap.forEach(headers::set);
        }
        return restTemplate.postForObject(url, new HttpEntity<>(paramObjectStr, headers), String.class);
    }

    /**
     * post 请求 form
     *
     * @param restTemplate
     * @param url
     * @param headerMap
     * @param paramMap
     * @return
     */
    public static String postForm(RestTemplate restTemplate, String url, Map<String, String> headerMap, MultiValueMap<String, Object> paramMap) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        if (!CollectionUtil.isEmpty(headerMap)) {
            headerMap.forEach(headers::set);
        }
        return restTemplate.postForObject(url, new HttpEntity<>(paramMap, headers), String.class);
    }
}
