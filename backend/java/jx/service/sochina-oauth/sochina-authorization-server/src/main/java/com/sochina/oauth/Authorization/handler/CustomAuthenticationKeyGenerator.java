package com.sochina.oauth.Authorization.handler;

import com.sochina.base.utils.id.uuid.UuidUtils;
import com.sochina.oauth.resource.constants.OauthConstants;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

public class CustomAuthenticationKeyGenerator extends DefaultAuthenticationKeyGenerator {
    @Override
    public String extractKey(OAuth2Authentication authentication) {
        Map<String, String> values = new LinkedHashMap();
        OAuth2Request authorizationRequest = authentication.getOAuth2Request();
        if (!authentication.isClientOnly()) {
            values.put(OauthConstants.USERNAME, authentication.getName());
        }
        values.put(OauthConstants.CLIENT_ID, authorizationRequest.getClientId());
        if (authorizationRequest.getScope() != null) {
            values.put(OauthConstants.SCOPE, OAuth2Utils.formatParameterList(new TreeSet(authorizationRequest.getScope())));
        }
        values.put(OauthConstants.UUID, UuidUtils.fastSimpleUUID());
        return this.generateKey(values);
    }
}
