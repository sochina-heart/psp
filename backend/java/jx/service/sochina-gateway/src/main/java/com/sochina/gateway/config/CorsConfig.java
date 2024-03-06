package com.sochina.gateway.config;

import com.sochina.base.constants.Constants;
import com.sochina.base.utils.StringUtils;
import com.sochina.gateway.domain.properties.CorsProperties;
import com.sochina.gateway.domain.properties.SochinaProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import javax.annotation.Resource;

@Configuration
@EnableWebFlux
@ConditionalOnProperty(value = "sochina.cors-properties.enable", havingValue = "true")
public class CorsConfig implements WebFluxConfigurer {
    @Resource
    private SochinaProperties sochinaProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsProperties corsProperties = sochinaProperties.getCorsProperties();
        if (StringUtils.isNotNull(corsProperties)) {
            CorsRegistration cr = null;
            cr = registry.addMapping(corsProperties.getMapping());
            if (corsProperties.getAllowedOrigins() != null) {
                cr.allowedOrigins(corsProperties.getAllowedOrigins().split(Constants.COMMA));
            }
            if (corsProperties.getAllowedMethods() != null) {
                cr.allowedMethods(corsProperties.getAllowedMethods().split(Constants.COMMA));
            }
            if (corsProperties.getAllowedHeaders() != null) {
                cr.allowedHeaders(corsProperties.getAllowedHeaders().split(Constants.COMMA));
            }
            if (corsProperties.getExposedHeaders() != null) {
                cr.exposedHeaders(corsProperties.getExposedHeaders().split(Constants.COMMA));
            }
            cr.allowCredentials(corsProperties.isAllowCredentials());
            cr.maxAge(corsProperties.getMaxAge());
        }
    }
}
