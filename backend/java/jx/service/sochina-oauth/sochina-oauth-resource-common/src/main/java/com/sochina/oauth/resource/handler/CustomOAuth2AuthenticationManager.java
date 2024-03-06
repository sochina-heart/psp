package com.sochina.oauth.resource.handler;

import com.sochina.base.utils.StringUtils;
import com.sochina.base.utils.id.uuid.UuidUtils;
import com.sochina.mvc.utils.ServletUtils;
import com.sochina.oauth.resource.config.FilterIgnorePropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;

public class CustomOAuth2AuthenticationManager extends OAuth2AuthenticationManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomOAuth2AuthenticationManager.class);
    private final FilterIgnorePropertiesConfig filterIgnorePropertiesConfig;

    public CustomOAuth2AuthenticationManager(FilterIgnorePropertiesConfig filterIgnorePropertiesConfig) {
        this.filterIgnorePropertiesConfig = filterIgnorePropertiesConfig;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LOGGER.debug(filterIgnorePropertiesConfig.toString());
        if (StringUtils.matches(ServletUtils.getRequest().getRequestURI(), filterIgnorePropertiesConfig.getUrls())) {
            try {
                return super.authenticate(authentication);
            } catch (AuthenticationException | InvalidTokenException e) {
                LOGGER.error("", e);
                return new AnonymousAuthenticationToken(UuidUtils.fastSimpleUUID(), "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
            }
        }
        return super.authenticate(authentication);
    }
}
