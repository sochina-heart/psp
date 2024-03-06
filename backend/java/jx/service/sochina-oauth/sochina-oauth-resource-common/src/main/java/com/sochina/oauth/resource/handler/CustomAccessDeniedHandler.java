package com.sochina.oauth.resource.handler;

import cn.hutool.http.HttpStatus;
import com.sochina.mvc.utils.MvcR;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) {
        MvcR.failRender(HttpStatus.HTTP_UNAUTHORIZED, exception.getMessage(), response, HttpStatus.HTTP_UNAUTHORIZED);
        LOGGER.error("AccessDenied异常: [{}], [{}], [{}]", exception.getMessage(), exception.getLocalizedMessage(), exception.toString());
    }
}
