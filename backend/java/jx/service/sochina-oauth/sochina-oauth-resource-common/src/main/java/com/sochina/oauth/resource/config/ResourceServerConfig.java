package com.sochina.oauth.resource.config;

import com.sochina.oauth.resource.handler.CustomAccessDeniedHandler;
import com.sochina.oauth.resource.handler.CustomAuthenticationEntryPoint;
import com.sochina.oauth.resource.handler.CustomOAuth2AuthenticationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import javax.annotation.Resource;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceServerConfig.class);
    @Resource
    private FilterIgnorePropertiesConfig filterIgnorePropertiesConfig;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.stateless(true);
        resources.authenticationManager(new CustomOAuth2AuthenticationManager(filterIgnorePropertiesConfig));
        resources.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // session创建策略
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        // 所有请求需要认证
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
        filterIgnorePropertiesConfig.getUrls().forEach(url -> registry.antMatchers(url).permitAll());
        registry.anyRequest().authenticated();
    }
}
