package com.sochina.mvc.config;

import com.sochina.mvc.domain.properties.SpringSessionProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@ConditionalOnProperty(value = "sochina.spring-session-properties.enable", havingValue = "true")
@Configuration
public class SpringSessionConfig {

    private final SpringSessionProperties springSessionProperties;

    public SpringSessionConfig(SpringSessionProperties springSessionProperties) {
        this.springSessionProperties = springSessionProperties;
    }

    // spring session设置samesite属性
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName(springSessionProperties.getCookieName());
        serializer.setDomainName(springSessionProperties.getDomainName());
        serializer.setCookiePath(springSessionProperties.getCookiePath());
        serializer.setCookieMaxAge(springSessionProperties.getCoolieMaxAge());
        serializer.setSameSite(springSessionProperties.getSameSite());  // 设置SameSite属性
        serializer.setUseHttpOnlyCookie(springSessionProperties.isUseHttpOnlyCookie());
        serializer.setUseSecureCookie(springSessionProperties.isUseSecureCookie());
        return serializer;
    }
}
