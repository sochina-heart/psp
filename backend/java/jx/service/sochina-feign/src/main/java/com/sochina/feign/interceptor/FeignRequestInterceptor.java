package com.sochina.feign.interceptor;

import com.sochina.mvc.utils.ServletUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
public class FeignRequestInterceptor implements RequestInterceptor {
    private final static Logger LOGGER = LoggerFactory.getLogger(FeignRequestInterceptor.class);

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Map<String, Collection<String>> headers = ServletUtils.getHeaders();
        if (!headers.isEmpty()) requestTemplate.headers(headers);
        LOGGER.debug("feign request header:{}", requestTemplate);
    }
}
