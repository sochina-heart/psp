package com.sochina.oauth.Authorization.aspect;

import com.sochina.base.utils.web.R;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CustomOAuthTokenAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomOAuthTokenAspect.class);

    @Around("execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.postAccessToken(..))")
    public ResponseEntity response(ProceedingJoinPoint point) throws Throwable {
        Object proceed = point.proceed();
        ResponseEntity<OAuth2AccessToken> responseEntity = (ResponseEntity<OAuth2AccessToken>) proceed;
        return ResponseEntity.ok(R.ok(responseEntity.getBody()));
    }
}