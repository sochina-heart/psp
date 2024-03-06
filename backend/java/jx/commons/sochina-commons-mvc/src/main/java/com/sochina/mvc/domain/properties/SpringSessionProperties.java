package com.sochina.mvc.domain.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@ConfigurationProperties("sochina.spring-session-properties")
@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpringSessionProperties implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean enable = true;
    private String cookieName;
    private String domainName;
    private String cookiePath;
    private int coolieMaxAge;
    private String sameSite;
    private boolean useHttpOnlyCookie = true;
    // 如果浏览器阻拦set-cookie，这里设置true
    private boolean useSecureCookie = false;
}
