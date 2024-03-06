package com.sochina.base.enums.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseType {
    TOKEN("token");
    private final String extension;
}
