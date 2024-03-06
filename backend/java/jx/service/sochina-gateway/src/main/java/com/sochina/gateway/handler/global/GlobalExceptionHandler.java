package com.sochina.gateway.handler.global;

import com.sochina.base.utils.StringUtils;
import com.sochina.gateway.constants.GatewayInitResources;
import com.sochina.webflux.utils.WebFluxResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final List<BaseExceptionHandler> exceptionHandlerList;

    public GlobalExceptionHandler(List<BaseExceptionHandler> exceptionHandlerList) {
        this.exceptionHandlerList = exceptionHandlerList;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        String url = exchange.getRequest().getURI().getPath();
        if (StringUtils.matches(url, GatewayInitResources.EXCLUDE_URL)
                || StringUtils.isEndsWithStr(url, GatewayInitResources.STATIC_URL)) {
            return Mono.empty();
        }
        for (BaseExceptionHandler handler: exceptionHandlerList) {
            if (ex.getClass().getSimpleName().equals(handler.getHandlerName())) {
                return handler.doHandler(ex, exchange);
            }
        }
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        LOGGER.error("网关统一异常处理--请求路径: {},异常信息: ", url, ex);
        return WebFluxResponseUtil.webFluxResponseWriter(response, ex.getMessage(), 500);
    }
}