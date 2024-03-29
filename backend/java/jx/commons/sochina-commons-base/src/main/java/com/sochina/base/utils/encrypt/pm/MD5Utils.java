package com.sochina.base.utils.encrypt.pm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    static final char HEX_DIGITS_LOWER[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final Logger LOGGER = LoggerFactory.getLogger(MD5Utils.class);

    /**
     * 对字符串 MD5 无盐值加密
     *
     * @param plainText 传入要加密的字符串
     * @return MD5加密后生成32位(小写字母 + 数字)字符串
     */
    public static String MD5Lower(String plainText) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            md.update(plainText.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值。1 固定值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    /**
     * 对字符串 MD5 加密
     *
     * @param plainText 传入要加密的字符串
     * @return MD5加密后生成32位(大写字母 + 数字)字符串
     */
    public static String MD5Upper(String plainText) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            md.update(plainText.getBytes());
            // 获得密文
            byte[] mdResult = md.digest();
            // 把密文转换成十六进制的字符串形式
            int j = mdResult.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = mdResult[i];
                // 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                // 取字节中低 4 位的数字转换
                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    /**
     * 对字符串 MD5 加盐值加密
     *
     * @param plainText 传入要加密的字符串
     * @param saltValue 传入要加的盐值
     * @return MD5加密后生成32位(小写字母 + 数字)字符串
     */
    public static String MD5Lower(String plainText, String saltValue) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            md.update(plainText.getBytes());
            md.update(saltValue.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值。1 固定值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    /**
     * 对字符串 MD5 加盐值加密
     *
     * @param plainText 传入要加密的字符串
     * @param saltValue 传入要加的盐值
     * @return MD5加密后生成32位(大写字母 + 数字)字符串
     */
    public static String MD5Upper(String plainText, String saltValue) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            md.update(plainText.getBytes());
            md.update(saltValue.getBytes());
            // 获得密文
            byte[] mdResult = md.digest();
            // 把密文转换成十六进制的字符串形式
            int j = mdResult.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = mdResult[i];
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    /**
     * MD5加密后生成32位(小写字母+数字)字符串
     * 同 MD5Lower() 一样
     */
    public final static String MD5(String plainText) {
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(plainText.getBytes("UTF-8"));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = HEX_DIGITS_LOWER[byte0 >>> 4 & 0xf];
                str[k++] = HEX_DIGITS_LOWER[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }
}
