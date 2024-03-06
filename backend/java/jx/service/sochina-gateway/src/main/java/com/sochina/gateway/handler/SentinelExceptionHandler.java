package com.sochina.gateway.handler;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.sochina.base.utils.GsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sochina.base.constants.HttpStatus.*;

public class SentinelExceptionHandler implements BlockRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SentinelExceptionHandler.class);
    private static Map<Class<? extends Throwable>, Map.Entry<Integer, String>> SENTINEL_EXCEPTION_MAP;

    public SentinelExceptionHandler() {
        LOGGER.info("init SentinelExceptionHandler start");
        SENTINEL_EXCEPTION_MAP = Stream.of(
                new AbstractMap.SimpleEntry<>(FlowException.class, new AbstractMap.SimpleEntry<>(SENTINEL_FLOW, "接口限流")),
                new AbstractMap.SimpleEntry<>(DegradeException.class, new AbstractMap.SimpleEntry<>(SENTINEL_DEGRADE, "服务降级")),
                new AbstractMap.SimpleEntry<>(ParamFlowException.class, new AbstractMap.SimpleEntry<>(SENTINEL_PARAM_FLOW, "热点参数限流")),
                new AbstractMap.SimpleEntry<>(SystemBlockException.class, new AbstractMap.SimpleEntry<>(SENTINEL_SYSTEM_BLOCK, "触发系统保护规则")),
                new AbstractMap.SimpleEntry<>(AuthorityException.class, new AbstractMap.SimpleEntry<>(SENTINEL_AUTHORITY, "授权规则不通过"))
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
        Map.Entry<Integer, String> resultMap = SENTINEL_EXCEPTION_MAP.entrySet().stream()
                .filter(entry -> entry.getKey().isInstance(throwable))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(new AbstractMap.SimpleEntry<>(ERROR, "服务器错误"));
        return ServerResponse.status(HttpStatus.BAD_GATEWAY)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(GsonUtils.toJson(resultMap)));
    }
}
