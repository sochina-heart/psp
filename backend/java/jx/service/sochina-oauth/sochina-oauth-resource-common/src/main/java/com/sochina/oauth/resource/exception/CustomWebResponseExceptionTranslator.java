package com.sochina.oauth.resource.exception;

import com.sochina.base.utils.web.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.io.IOException;

@Component
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomWebResponseExceptionTranslator.class);
    private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    @Override
    public ResponseEntity translate(Exception e) throws Exception {
        Throwable[] causeChain = this.throwableAnalyzer.determineCauseChain(e);
        Exception ase = (OAuth2Exception) this.throwableAnalyzer.getFirstThrowableOfType(OAuth2Exception.class, causeChain);
        // 异常链中有OAuth2Exception异常
        if (ase != null) {
            return this.handleOAuth2Exception((OAuth2Exception) ase);
        }
        // 身份验证相关异常
        ase = (AuthenticationException) this.throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class, causeChain);
        if (ase != null) {
            return this.handleOAuth2Exception(new CustomWebResponseExceptionTranslator.UnauthorizedException(e.getMessage(), e));
        }
        // 异常链中包含拒绝访问异常
        ase = (AccessDeniedException) this.throwableAnalyzer.getFirstThrowableOfType(AccessDeniedException.class, causeChain);
        if (ase instanceof AccessDeniedException) {
            return this.handleOAuth2Exception(new CustomWebResponseExceptionTranslator.ForbiddenException(ase.getMessage(), ase));
        }
        // 异常链中包含Http方法请求异常
        ase = (HttpRequestMethodNotSupportedException) this.throwableAnalyzer.getFirstThrowableOfType(HttpRequestMethodNotSupportedException.class, causeChain);
        if (ase instanceof HttpRequestMethodNotSupportedException) {
            return this.handleOAuth2Exception(new CustomWebResponseExceptionTranslator.MethodNotAllowed(ase.getMessage(), ase));
        }
        return this.handleOAuth2Exception(new CustomWebResponseExceptionTranslator.ServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e));
    }

    private ResponseEntity<OAuth2Exception> handleOAuth2Exception(OAuth2Exception e) throws IOException {
        int status = e.getHttpErrorCode();
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache());
        headers.setPragma(CacheControl.noCache().getHeaderValue());
        if (status == HttpStatus.UNAUTHORIZED.value() || e instanceof InsufficientScopeException) {
            headers.set(HttpHeaders.WWW_AUTHENTICATE, String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, e.getSummary()));
        }
        ResponseEntity<OAuth2Exception> response = new ResponseEntity(R.fail(e.getMessage()), headers, HttpStatus.valueOf(status));
        return response;
    }

    private static class MethodNotAllowed extends OAuth2Exception {
        public MethodNotAllowed(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "method_not_allowed";
        }

        @Override
        public int getHttpErrorCode() {
            return 405;
        }
    }

    private static class UnauthorizedException extends OAuth2Exception {
        public UnauthorizedException(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "unauthorized";
        }

        @Override
        public int getHttpErrorCode() {
            return 401;
        }
    }

    private static class ServerErrorException extends OAuth2Exception {
        public ServerErrorException(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "server_error";
        }

        @Override
        public int getHttpErrorCode() {
            return 500;
        }
    }

    private static class ForbiddenException extends OAuth2Exception {
        public ForbiddenException(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "access_denied";
        }

        @Override
        public int getHttpErrorCode() {
            return 403;
        }
    }
}