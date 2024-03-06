package com.sochina.base.enums.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GrantType {
    AUTHORIZATION_CODE("authorization_code"),
    PASSWORD("password"),
    CLIENT_CREDENTIALS("client_credentials"),
    REFRESH_TOKEN("refresh_token");
    private final String extension;
}
