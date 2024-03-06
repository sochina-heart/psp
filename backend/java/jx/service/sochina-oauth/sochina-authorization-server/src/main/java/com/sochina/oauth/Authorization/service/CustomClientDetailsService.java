package com.sochina.oauth.Authorization.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

public class CustomClientDetailsService extends JdbcClientDetailsService {
    public CustomClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }

    @Cacheable(value = "oauth2:client:details", key = "#clientId", unless = "#result == null ")
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        return super.loadClientByClientId(clientId);
    }
}
