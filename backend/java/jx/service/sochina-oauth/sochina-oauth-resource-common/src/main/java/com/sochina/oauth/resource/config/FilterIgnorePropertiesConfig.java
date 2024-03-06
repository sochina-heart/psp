package com.sochina.oauth.resource.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConditionalOnExpression("!'${security.oauth2.ignore}'.isEmpty()")
@ConfigurationProperties(prefix = "security.oauth2.ignore")
public class FilterIgnorePropertiesConfig {
    private List<String> urls = new ArrayList<>();
}
