package com.sochina.base.utils;

public class CharUtils {
    /**
     * ASCII表中可见字符从!开始，偏移位值为33(Decimal)
     * 半角
     */
    static final char DBC_CHAR_START = 33;
    /**
     * ASCII表中可见字符到~结束，偏移位值为126(Decimal)
     * 半角
     */
    static final char DBC_CHAR_END = 126;
    /**
     * 全角对应于ASCII表的可见字符从！开始，偏移值为65281
     * 全角
     */
    static final char SBC_CHAR_START = 65281;
    /**
     * 全角对应于ASCII表的可见字符到～结束，偏移值为65374
     * 全角
     */
    static final char SBC_CHAR_END = 65374;
    /**
     * ASCII表中除空格外的可见字符与对应的全角字符的相对偏移
     * 全角半角转换间隔
     */
    static final int CONVERT_STEP = 65248;
    /**
     * 全角空格的值，它没有遵从与ASCII的相对偏移，必须特殊处理
     * 全角空格 12288
     */
    static final char SBC_SPACE = 12288;
    /**
     * 半角空格的值，在ASCII中为32(Decimal)
     * 半角空格
     */
    static final char DBC_SPACE = ' ';

    /**
     * 半角字符->全角字符转换
     *
     * @param src 要转换的包含半角字符的任意字符串
     * @return 全角字符串
     */
    public static String half2Full(String src) {
        if (src == null) {
            return null;
        }
        char[] c = src.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == DBC_SPACE) {
                c[i] = SBC_SPACE;
            } else if (c[i] >= DBC_CHAR_START && c[i] <= DBC_CHAR_END) {
                c[i] = (char) (c[i] + CONVERT_STEP);
            }
        }
        return new String(c);
    }

    /**
     * 全角字符->半角字符转换
     *
     * @param src 要转换的包含全角字符的任意字符串
     * @return 半角字符串
     */
    public static String full2Half(String src) {
        if (src == null) {
            return null;
        }
        char[] c = src.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == SBC_SPACE) {
                c[i] = DBC_SPACE;
            } else if (c[i] >= SBC_CHAR_START && c[i] <= SBC_CHAR_END) {
                c[i] = (char) (c[i] - CONVERT_STEP);
            }
        }
        return new String(c);
    }
}
