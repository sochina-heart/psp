package com.sochina.base.domain.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConfigurationProperties(prefix = "sochina.security")
@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSecurityProperties {
    private List<String> urlList;
}
