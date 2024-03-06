package com.sochina.base.constants;

import com.sochina.base.domain.properties.CommonProperties;
import com.sochina.base.domain.properties.SqlProperties;
import com.sochina.base.domain.properties.XssProperties;
import com.sochina.base.utils.CharUtils;
import com.sochina.base.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.sochina.base.constants.Constants.*;

@Component
public class BaseInitResources {
    public static final Map<String, String> XSS_HALF_MAP = new HashMap<>();
    public static final Map<String, String> XSS_FULL_MAP = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseInitResources.class);
    public static String[] XSS_SENSITIVE_DATA;
    public static Map<String, String> XSS_PATTERN_MAP;
    public static Pattern[] SQL_PATTERNS;
    public static String[] SQL_SENSITIVE_DATA;
    public static String[] SQL_KEY_WORDS;
    private final CommonProperties commonProperties;

    public BaseInitResources(CommonProperties commonProperties) {
        LOGGER.info("init BaseInitResource start");
        this.commonProperties = commonProperties;
        XSS_SENSITIVE_DATA = Optional.ofNullable(commonProperties.getXssProperties())
                .map(XssProperties::getSensitiveData)
                .map(s -> StringUtils.split(s, COMMA))
                .orElse(EMPTY_STRING_ARRAY);
        XSS_PATTERN_MAP = Optional.ofNullable(commonProperties.getXssProperties())
                .map(XssProperties::getPattern)
                .orElse(EMPTY_MAP);
        Optional.ofNullable(XSS_SENSITIVE_DATA)
                .map(Stream::of)
                .orElseGet(Stream::empty)
                .forEach(key -> {
                    String value = CharUtils.half2Full(key);
                    XSS_HALF_MAP.put(key, value);
                    XSS_FULL_MAP.put(value, key);
                });
        SQL_PATTERNS = Optional.ofNullable(commonProperties.getSqlProperties())
                .map(SqlProperties::getPattern)
                .map(patterns -> Arrays.stream(StringUtils.split(patterns, COMMA))
                        .map(pattern -> Pattern.compile(pattern, Pattern.CASE_INSENSITIVE))
                        .toArray(Pattern[]::new))
                .orElse(EMPTY_PATTERN_ARRAY);
        SQL_SENSITIVE_DATA = Optional.ofNullable(commonProperties.getSqlProperties())
                .map(SqlProperties::getSensitiveData)
                .map(s -> StringUtils.split(s, COMMA))
                .orElse(EMPTY_STRING_ARRAY);
        SQL_KEY_WORDS = Optional.ofNullable(commonProperties.getSqlProperties())
                .map(SqlProperties::getKeyword)
                .map(s -> StringUtils.split(s, COMMA))
                .orElse(EMPTY_STRING_ARRAY);
    }
}
