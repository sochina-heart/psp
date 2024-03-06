package com.sochina.gateway.handler.global;

import com.sochina.webflux.utils.WebFluxResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class WebExchangeBindExceptionHandler implements BaseExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebExchangeBindExceptionHandler.class);

    @Override
    public String getHandlerName() {
        return "WebExchangeBindException";
    }

    @Override
    public Mono<Void> doHandler(Throwable ex, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]+");
        Matcher matcher = pattern.matcher(Objects.requireNonNull(ex.getMessage()));
        String errorMessage = Stream.generate(() -> {
                    if (matcher.find()) {
                        return matcher.group();
                    } else {
                        return null;
                    }
                })
                .limit(100)
                .collect(Collectors.joining("、"));
        LOGGER.error("网关统一异常处理--请求路径: {},异常信息: {}", exchange.getRequest().getPath(), errorMessage);
        return WebFluxResponseUtil.webFluxResponseWriter(response, errorMessage, 400);
    }
}
