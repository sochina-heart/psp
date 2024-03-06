package com.sochina.gateway.handler.global;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface BaseExceptionHandler {

    String getHandlerName();

    Mono<Void> doHandler(Throwable ex, ServerWebExchange exchange);
}
