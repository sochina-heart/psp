package com.sochina.oauth.Authorization.config;

import com.sochina.oauth.Authorization.filter.CustomClientCredentialsTokenEndpointFilter;
import com.sochina.oauth.Authorization.handler.UserTokenEnhancer;
import com.sochina.oauth.Authorization.service.CustomClientDetailsService;
import com.sochina.oauth.resource.exception.CustomWebResponseExceptionTranslator;
import com.sochina.oauth.resource.handler.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private TokenStore tokenStore;
    @Resource
    private CustomWebResponseExceptionTranslator customWebResponseExceptionTranslator;
    @Resource
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Resource
    private DataSource dataSource;

    @Bean
    public ClientDetailsService clientDetails() {
        return new CustomClientDetailsService(dataSource);
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new UserTokenEnhancer();
    }

    // 配置客户端
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /*clients.inMemory()
                .withClient("sochina")
                .secret(passwordEncoder.encode("sochina"))
                .authorizedGrantTypes(GrantType.PASSWORD.getExtension(), GrantType.REFRESH_TOKEN.getExtension(), GrantType.CLIENT_CREDENTIALS.getExtension())
                .scopes("read_scope");*/
        clients.withClientDetails(clientDetails());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        // token持久化容器
        tokenServices.setTokenStore(tokenStore);
        // 是否支持refresh_token，默认false
        tokenServices.setSupportRefreshToken(true);
        // 客户端信息
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        // 自定义token生成
        tokenServices.setTokenEnhancer(tokenEnhancer());
        // access_token 的有效时长 (秒), 默认 12 小时
        // tokenServices.setAccessTokenValiditySeconds(60*15);
        // refresh_token 的有效时长 (秒), 默认 30 天
        // tokenServices.setRefreshTokenValiditySeconds(60*20);
        // 是否复用refresh_token,默认为true(如果为false,则每次请求刷新都会删除旧的refresh_token,创建新的refresh_token)
        tokenServices.setReuseRefreshToken(false);
        endpoints.authenticationManager(authenticationManager)
                // .tokenStore(tokenStore)
                // .reuseRefreshTokens(false)
                // .tokenEnhancer(tokenEnhancer())
                .tokenServices(tokenServices)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .exceptionTranslator(customWebResponseExceptionTranslator);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        CustomClientCredentialsTokenEndpointFilter endpointFilter = new CustomClientCredentialsTokenEndpointFilter(security);
        // 初始化的时候执行
        endpointFilter.afterPropertiesSet();
        // 格式化客户端异常的响应格式
        endpointFilter.setAuthenticationEntryPoint(customAuthenticationEntryPoint);
        security
                //.allowFormAuthenticationForClients()
                .checkTokenAccess("isAuthenticated()")
                .addTokenEndpointAuthenticationFilter(endpointFilter);
    }
}