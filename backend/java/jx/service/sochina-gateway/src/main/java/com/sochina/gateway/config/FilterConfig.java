package com.sochina.gateway.config;

import com.sochina.gateway.domain.properties.SochinaProperties;
import com.sochina.gateway.filter.AddResponseHeaderFilter;
import com.sochina.gateway.filter.BlackFilter;
import com.sochina.gateway.filter.IllegalFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilterConfig.class);
    private final SochinaProperties sochinaProperties;

    public FilterConfig(SochinaProperties sochinaProperties) {
        this.sochinaProperties = sochinaProperties;
    }

    @ConditionalOnProperty(value = "sochina.response-properties.enable", havingValue = "true")
    @Bean
    public AddResponseHeaderFilter addResponseHeaderFilter() {
        LOGGER.info("AddResponseHeaderFilter loaded");
        return new AddResponseHeaderFilter(sochinaProperties);
    }

    @ConditionalOnProperty(value = "sochina.illegal-properties.enable", havingValue = "true")
    @Bean
    public IllegalFilter illegalFilter() {
        LOGGER.info("IllegalFilter loaded");
        return new IllegalFilter(sochinaProperties);
    }

    @ConditionalOnProperty(value = "sochina.black-properties.enable", havingValue = "true")
    @Bean
    public BlackFilter blackFilter() {
        LOGGER.info("BlackFilter loaded");
        return new BlackFilter(sochinaProperties);
    }
}
