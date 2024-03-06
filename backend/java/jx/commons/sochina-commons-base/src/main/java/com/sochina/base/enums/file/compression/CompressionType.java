package com.sochina.base.enums.file.compression;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompressionType {
    ZIP(".zip"),
    RAR(".rar"),
    SEVEN_Z(".7z"),
    TAR(".tar"),
    TAR_GZ(".tar.gz");
    private final String extension;
}
