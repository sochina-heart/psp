package com.sochina.oauth.resource.handler;

import cn.hutool.http.HttpStatus;
import com.sochina.mvc.utils.MvcR;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        MvcR.failRender(HttpStatus.HTTP_UNAUTHORIZED, exception.getMessage(), response, HttpStatus.HTTP_UNAUTHORIZED);
        LOGGER.error("Authentication异常: [{}], [{}], [{}]", request.getRequestURI(), exception.getMessage(), exception.toString());
    }
}