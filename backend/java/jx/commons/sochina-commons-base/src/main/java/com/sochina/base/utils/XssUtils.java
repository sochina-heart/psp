package com.sochina.base.utils;

import com.sochina.base.constants.BaseInitResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sochina.base.constants.Constants.EMPTY_STRING;

@Component
public class XssUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(XssUtils.class);

    /**
     * 将字符串中的敏感数据替换成全角字符
     */
    public static String escape(String str) {
        return Optional.ofNullable(str)
                .filter(StringUtils::isNotBlank)
                .map(s -> BaseInitResources.XSS_HALF_MAP.entrySet().stream()
                        .reduce(s, (s1, entry) -> s1.replace(entry.getKey(), entry.getValue()), String::concat))
                .orElse(EMPTY_STRING);
    }

    /**
     * 将字符串中的全角字符还原成半角
     */
    public static String recover(String str) {
        return Optional.ofNullable(str)
                .filter(StringUtils::isNotBlank)
                .map(s -> BaseInitResources.XSS_FULL_MAP.entrySet().stream()
                        .reduce(s, (s1, entry) -> s1.replace(entry.getKey(), entry.getValue()), String::concat))
                .orElse(EMPTY_STRING);
    }

    /**
     * 将字符串中的数据和表达式替换成空格
     */
    public static String clean(String str) {
        if (StringUtils.isBlank(str)) {
            return EMPTY_STRING;
        }
        return BaseInitResources.XSS_PATTERN_MAP.entrySet().stream()
                .reduce(str, (s, entry) -> {
                    Pattern pattern = Pattern.compile(entry.getValue(), Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(s);
                    return matcher.replaceAll(EMPTY_STRING);
                }, (s1, s2) -> s1);
    }
}
