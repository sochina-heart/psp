package com.sochina.oauth.Authorization.config;

import com.sochina.oauth.Authorization.handler.CustomAuthenticationKeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;

@Configuration
public class BeanConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(BeanConfig.class);
    @Resource
    private RedisConnectionFactory redisConnectionFactory;
    @Value("${sochina.single.enable:false}")
    private boolean isSingle;
    @Value("${security.oauth2.redis.key.prefix:oauth2:}")
    private String oauth2Prefix;

    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        if (isSingle) {
            tokenStore.setAuthenticationKeyGenerator(new CustomAuthenticationKeyGenerator());
        }
        tokenStore.setPrefix(oauth2Prefix);
        return tokenStore;
    }
}
