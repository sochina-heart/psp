package com.sochina.oauth.Authorization.handler;

import com.sochina.base.utils.StringUtils;
import com.sochina.base.utils.id.uuid.UuidUtils;
import com.sochina.oauth.resource.constants.OauthConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class UserTokenEnhancer implements TokenEnhancer {
    @Value("${security.oauth2.token.prefix:null}")
    private String tokenPrefix;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if (accessToken instanceof DefaultOAuth2AccessToken) {
            DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
            token.setValue(getToken());
            OAuth2RefreshToken refreshToken = token.getRefreshToken();
            if (refreshToken instanceof DefaultOAuth2RefreshToken) {
                token.setRefreshToken(new DefaultOAuth2RefreshToken(getToken()));
            }
            Map<String, Object> additionalInformation = new HashMap<>();
            additionalInformation.put(OauthConstants.CLIENT_ID, authentication.getOAuth2Request().getClientId());
            token.setAdditionalInformation(additionalInformation);
            return token;
        }
        return accessToken;
    }

    private String getToken() {
        return StringUtils.isBlank(tokenPrefix) ? UuidUtils.fastSimpleUUID() : StringUtils.join(tokenPrefix, UuidUtils.fastSimpleUUID());
    }
}
