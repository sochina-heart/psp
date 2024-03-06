package com.sochina.base.enums.file.compression;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompressionImplements {
    ZIP("zipHandler");
    private final String extension;
}
