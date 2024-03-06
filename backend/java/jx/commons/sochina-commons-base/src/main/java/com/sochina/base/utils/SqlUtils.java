package com.sochina.base.utils;

import com.sochina.base.constants.BaseInitResources;
import com.sochina.base.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class SqlUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(SqlUtils.class);

    /**
     * 处理SQL字符串，将敏感数据、关键字以及符合正则表达式的内容，替换为空格
     *
     * @param sql SQL语句
     * @return 处理后的SQL语句
     */
    public static String processSql(String sql) {
        sql = Optional.ofNullable(sql)
                .orElse("");
        sql = Stream.concat(Arrays.stream(BaseInitResources.SQL_SENSITIVE_DATA), Arrays.stream(BaseInitResources.SQL_KEY_WORDS))
                .filter(StringUtils::isNotBlank)
                .reduce(sql, (str, s) -> str.replaceAll("(?i)" + s, Constants.EMPTY_STRING), String::concat);
        // 遍历正则表达式
        sql = Arrays.stream(BaseInitResources.SQL_PATTERNS)
                .reduce(sql, (s, p) -> p.matcher(s).replaceAll(Constants.EMPTY_STRING), (s1, s2) -> s1);
        return sql;
    }
}
