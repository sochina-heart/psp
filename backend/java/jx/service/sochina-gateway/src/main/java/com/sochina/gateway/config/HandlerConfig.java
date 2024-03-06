package com.sochina.gateway.config;

import com.sochina.gateway.handler.global.BaseExceptionHandler;
import com.sochina.gateway.handler.global.GlobalExceptionHandler;
import com.sochina.gateway.handler.SentinelExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
public class HandlerConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerConfig.class);

    @Autowired
    private List<BaseExceptionHandler> exceptionHandlerList;

    @Order(-1)
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        LOGGER.info("GlobalExceptionHandler loaded");
        return new GlobalExceptionHandler(exceptionHandlerList);
    }

    @ConditionalOnProperty(value = "spring.cloud.sentinel.enabled", havingValue = "true")
    @Bean
    public SentinelExceptionHandler sentinelExceptionHandler() {
        LOGGER.info("SentinelExceptionHandler loaded");
        return new SentinelExceptionHandler();
    }
}
