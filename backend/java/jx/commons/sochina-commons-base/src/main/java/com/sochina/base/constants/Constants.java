package com.sochina.base.constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Constants {
    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";
    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";
    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";
    /**
     * www主域
     */
    public static final String WWW = "www.";
    /**
     * http请求
     */
    public static final String HTTP = "http://";
    /**
     * https请求
     */
    public static final String HTTPS = "https://";
    public static final String IP_LOCAL = "127.0.0.1";
    public static final String IPV6_LOCAL = "0:0:0:0:0:0:0:1";
    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";
    /**
     * bootstrap.yml
     */
    public static final String BOOTSTRAP_YML = "bootstrap.yml";
    /**
     * jasypt加解密盐值
     */
    public static final String JASYPT_ENCRYPTOR_PASSWORD = "jasypt.encryptor.password";
    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";
    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";
    /**
     * 英文逗号
     */
    public static final String COMMA = ",";
    public final static String ADD = "+";
    public final static String REDUCE = "-";
    public final static String TAKE = "*";
    public final static String REMOVE = "/";
    public final static String COLON = ":";
    /**
     * 空字符串
     */
    public static final String EMPTY_STRING = "";
    /**
     * 半角句号
     */
    public static final String HALF_STOP = ".";
    /**
     * 半角问号
     */
    public static final String HALF_ASK_MARK = "?";
    /**
     * 半角括号
     */
    public static final String HALF_BRACKET = "()";
    /**
     * null
     */
    public static final String EMPTY_NULL = null;
    /**
     * Pattern空数组
     */
    public static final Pattern[] EMPTY_PATTERN_ARRAY = new Pattern[0];
    /**
     * String空数组
     */
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    /**
     * String空List
     */
    public static final List<String> EMPTY_LIST = new ArrayList<>(0);
    /**
     * Map<String, String> 空Map
     */
    public static final Map<String, String> EMPTY_MAP = Collections.emptyMap();
    /**
     * 下划线 char
     */
    public static final char UNDERLINE_CHAR = '_';
    /**
     * 下划线 String
     */
    public static final String UNDERLINE_STRING = "_";
    /**
     * 等于号
     */
    public static final String EQUAL_SIGN = "=";
    /**
     * 并且
     */
    public static final String AND_SIGN = "&";
}
