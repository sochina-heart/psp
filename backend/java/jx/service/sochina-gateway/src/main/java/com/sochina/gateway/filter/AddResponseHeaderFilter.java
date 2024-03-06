package com.sochina.gateway.filter;

import com.sochina.base.constants.Constants;
import com.sochina.gateway.domain.properties.ResponseProperties;
import com.sochina.gateway.domain.properties.SochinaProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

public class AddResponseHeaderFilter implements WebFilter, Ordered {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddResponseHeaderFilter.class);
    private static Map<String, String> RESPONSE_HEADER_MAP;
    private final SochinaProperties sochinaProperties;

    public AddResponseHeaderFilter(SochinaProperties sochinaProperties) {
        LOGGER.info("init AddResponseHeaderFilter start");
        this.sochinaProperties = sochinaProperties;
        RESPONSE_HEADER_MAP = Optional.ofNullable(sochinaProperties.getResponseProperties())
                .map(ResponseProperties::getHeader)
                .orElse(Constants.EMPTY_MAP);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        HttpHeaders headers = serverWebExchange.getResponse().getHeaders();
        RESPONSE_HEADER_MAP.forEach(headers::add);
        return webFilterChain.filter(serverWebExchange);
    }

    @Override
    public int getOrder() {
        return sochinaProperties.getResponseProperties().getOrder();
    }
}
