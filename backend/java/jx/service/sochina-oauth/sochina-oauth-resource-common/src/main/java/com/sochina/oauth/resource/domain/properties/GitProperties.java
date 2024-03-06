package com.sochina.oauth.resource.domain.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "security.oauth2.client.git")
public class GitProperties {
    private String clientId;
    private String clientSecret;
    private String authorizeUrl;
    private String redirectUrl;
    private String accessTokenUrl;
    private String userInfoUrl;
}
