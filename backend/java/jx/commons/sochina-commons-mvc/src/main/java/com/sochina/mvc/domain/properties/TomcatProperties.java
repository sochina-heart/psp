package com.sochina.mvc.domain.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "sochina.tomcat-properties")
@Configuration
@Data
@AllArgsConstructor
public class TomcatProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean enable = true;
    private String userConstraint;
    private String pattern;
    private List<String> omittedMethod = new ArrayList<>();
    private boolean useHttpOnly = true;
    private boolean allowTrace = true;
    private String cookieSameSite;

    public TomcatProperties() {
        userConstraint = "CONFIDENTIAL";
        pattern = "/*";
        omittedMethod.add("GET");
        omittedMethod.add("POST");
        cookieSameSite = "LAX";
    }
}
