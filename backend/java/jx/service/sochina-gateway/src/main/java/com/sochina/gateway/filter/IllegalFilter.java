package com.sochina.gateway.filter;

import com.sochina.base.constants.Constants;
import com.sochina.base.utils.StringUtils;
import com.sochina.gateway.constants.GatewayInitResources;
import com.sochina.gateway.domain.properties.IllegalProperties;
import com.sochina.gateway.domain.properties.SochinaProperties;
import com.sochina.gateway.illegal.checker.IllegalChecker;
import com.sochina.gateway.illegal.checker.impl.SqlChecker;
import com.sochina.gateway.illegal.checker.impl.XssChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class IllegalFilter implements WebFilter, Ordered {
    private static final Logger LOGGER = LoggerFactory.getLogger(IllegalFilter.class);
    private static List<String> ILLEGAL_EXCLUDE_URL;
    private final SochinaProperties sochinaProperties;
    private final List<IllegalChecker> modifiers = new ArrayList<>();

    public IllegalFilter(SochinaProperties sochinaProperties) {
        LOGGER.info("init IllegalFilter start");
        this.sochinaProperties = sochinaProperties;
        ILLEGAL_EXCLUDE_URL = Optional.ofNullable(sochinaProperties.getIllegalProperties())
                .map(IllegalProperties::getExcludeUrl)
                .orElse(Constants.EMPTY_LIST);
        if (sochinaProperties.getXssProperties().isEnable()) {
            modifiers.add(XssChecker.getInstance());
        }
        if (sochinaProperties.getSqlProperties().isEnable()) {
            modifiers.add(SqlChecker.getInstance());
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        String url = serverWebExchange.getRequest().getURI().getPath();
        if (StringUtils.matches(url, ILLEGAL_EXCLUDE_URL)
                || StringUtils.isEndsWithStr(url, GatewayInitResources.STATIC_URL)) {
            return webFilterChain.filter(serverWebExchange);
        }
        return webFilterChain
                .filter(serverWebExchange.mutate()
                        .request(decorateRequestBody(serverWebExchange))
                        .build());
    }

    @Override
    public int getOrder() {
        return sochinaProperties.getIllegalProperties().getOrder();
    }

    private ServerHttpRequestDecorator decorateRequestBody(ServerWebExchange serverWebExchange) {
        ServerHttpRequest request = serverWebExchange.getRequest();
        HttpMethod method = request.getMethod();
        MediaType contentType = request.getHeaders().getContentType();
        if ((method == HttpMethod.GET)
                || contentType == null
                || contentType.includes(MediaType.APPLICATION_FORM_URLENCODED)) {
            return requestDecorator(serverWebExchange);
        } else if (contentType.includes(MediaType.APPLICATION_JSON)) {
            return requestJsonDecorator(serverWebExchange);
        } else {
            return new ServerHttpRequestDecorator(request);
        }
    }

    private ServerHttpRequestDecorator requestJsonDecorator(ServerWebExchange serverWebExchange) {
        return new ServerHttpRequestDecorator(serverWebExchange.getRequest()) {
            @Override
            public Flux<DataBuffer> getBody() {
                return super.getBody()
                        .map(buffer -> {
                            byte[] content = new byte[buffer.readableByteCount()];
                            buffer.read(content);
                            String value = new String(content, StandardCharsets.UTF_8);
                            value = applyModifiers(value);
                            byte[] uppedContent = value.getBytes(StandardCharsets.UTF_8);
                            DataBuffer uppedBuffer = serverWebExchange.getResponse().bufferFactory().wrap(uppedContent);
                            DataBufferUtils.release(buffer);
                            return uppedBuffer;
                        });
            }
        };
    }

    private ServerHttpRequestDecorator requestDecorator(ServerWebExchange serverWebExchange) {
        return new ServerHttpRequestDecorator(serverWebExchange.getRequest()) {
            @Override
            public MultiValueMap<String, String> getQueryParams() {
                return super.getQueryParams().entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, entry ->
                                entry.getValue().stream().map(IllegalFilter.this::applyModifiers)
                                        .collect(Collectors.toList()), (key, value) -> key, LinkedMultiValueMap::new));
            }
        };
    }

    private String applyModifiers(String value) {
        for (IllegalChecker checker : modifiers) {
            value = checker.doCheck(value);
        }
        return value;
    }
}
