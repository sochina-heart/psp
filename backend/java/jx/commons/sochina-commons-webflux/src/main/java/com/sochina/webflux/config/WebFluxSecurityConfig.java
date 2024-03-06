package com.sochina.webflux.config;

import com.sochina.base.constants.Constants;
import com.sochina.base.domain.properties.WebSecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.List;
import java.util.Optional;

@Configuration
@EnableWebFluxSecurity
public class WebFluxSecurityConfig {
    private static List<String> URL_LIST;
    private final WebSecurityProperties webSecurityProperties;

    public WebFluxSecurityConfig(WebSecurityProperties webSecurityProperties) {
        this.webSecurityProperties = webSecurityProperties;
        URL_LIST = Optional.ofNullable(webSecurityProperties.getUrlList())
                .orElse(Constants.EMPTY_LIST);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.csrf()
                .disable()
                .authorizeExchange()
                .pathMatchers(URL_LIST.toArray(new String[0]))
                .authenticated()
                .anyExchange()
                .permitAll()
                .and()
                .formLogin()
                .and()
                .build();
    }
}
