package com.sochina.oauth.resource.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sochina.oauth.resource.domain.properties.GitProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class GitLoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitLoginController.class);
    @Autowired
    private GitProperties githubProperties;

    /**
     * 登录接口，重定向至github
     *
     * @return 跳转url
     */
    @GetMapping("/authorize")
    public String authorize() {
        String url = githubProperties.getAuthorizeUrl() +
                "?client_id=" + githubProperties.getClientId() +
                "&redirect_uri=" + githubProperties.getRedirectUrl() +
                "&response_type=code";
        LOGGER.info("/authorize url:{}", url);
        return "redirect:" + url;
    }

    /**
     * 回调接口，用户同意授权后，GitHub会将授权码传递给此接口
     *
     * @param code GitHub重定向时附加的授权码，只能用一次
     * @return
     */
    @GetMapping("/oauth/redirect")
    @ResponseBody
    public String redirect(@RequestParam("code") String code) throws JsonProcessingException {
        LOGGER.info("/oauth/redirect code:{}", code);
        // 使用code获取token
        String accessToken = this.getAccessToken(code);
        // 使用token获取userInfo
        return this.getUserInfo(accessToken);
    }

    /**
     * 使用授权码获取token
     *
     * @param code
     * @return
     */
    private String getAccessToken(String code) throws JsonProcessingException {
        String url = githubProperties.getAccessTokenUrl() +
                "?client_id=" + githubProperties.getClientId() +
                "&client_secret=" + githubProperties.getClientSecret() +
                "&code=" + code +
                "&grant_type=authorization_code" +
                "&redirect_uri=" + githubProperties.getRedirectUrl();
        LOGGER.info("getAccessToken url:{}", url);
        // 构建请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        // 指定响应返回json格式
        // requestHeaders.add("accept", "application/json");
        // 构建请求实体
        HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        // post 请求方式
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        String responseStr = response.getBody();
        // 解析响应json字符串
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseStr);
        LOGGER.info("jsonNode-{}", jsonNode);
        String accessToken = jsonNode.get("access_token").asText();
        LOGGER.info("getAccessToken accessToken:{}", accessToken);
        return accessToken;
    }

    /**
     * @param accessToken 使用token获取userInfo
     * @return
     */
    private String getUserInfo(String accessToken) {
        String url = githubProperties.getUserInfoUrl();
        // 构建请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        // 指定响应返回json格式
        requestHeaders.add("accept", "application/json");
        // AccessToken放在请求头中
        requestHeaders.add("Authorization", "token " + accessToken);
        // 构建请求实体
        HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        // get请求方式
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        String userInfo = response.getBody();
        LOGGER.info("getUserInfo userInfo：{}", userInfo);
        return userInfo;
    }
}
