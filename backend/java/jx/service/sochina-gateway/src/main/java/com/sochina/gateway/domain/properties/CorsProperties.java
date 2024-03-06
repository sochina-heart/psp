package com.sochina.gateway.domain.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CorsProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean enable = true;
    private String mapping;
    private String allowedOrigins;
    private String allowedMethods;
    private String allowedHeaders;
    private String exposedHeaders;
    private boolean allowCredentials = true;
    private Integer maxAge = 3600;
}
