package com.sochina.gateway.handler.global;

import com.sochina.webflux.utils.WebFluxResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ArithmeticExceptionHandler implements BaseExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArithmeticExceptionHandler.class);

    @Override
    public String getHandlerName() {
        return "ArithmeticException";
    }

    @Override
    public Mono<Void> doHandler(Throwable ex, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        LOGGER.error("网关统一异常处理--请求路径: {},异常信息: {}", exchange.getRequest().getPath(), ex.getMessage());
        return WebFluxResponseUtil.webFluxResponseWriter(response, ex.getMessage(), 500);
    }
}
