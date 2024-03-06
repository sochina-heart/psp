package com.sochina.base.utils.encrypt.pm;

import com.sochina.base.exception.UtilException;
import com.sochina.base.utils.StringUtils;
import com.sochina.base.utils.encrypt.Base64Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(AESUtils.class);
    // 编码
    private static final String ENCODING = "UTF-8";
    // 算法定义
    private static final String AES_ALGORITHM = "AES";
    // 指定填充方式
    private static final String CIPHER_PADDING = "AES/ECB/PKCS5Padding";
    private static final String CIPHER_CBC_PADDING = "AES/CBC/PKCS5Padding";
    // 偏移量(CBC中使用，增强加密算法强度)
    private static final String IV_SEED = "1234567812345678";

    /**
     * AES加密_ECB
     *
     * @param content 待加密内容
     * @param aesKey  密码
     * @return
     */
    public static String encryptEcb(String content, String aesKey) {
        if (StringUtils.isBlank(content)) {
            LOGGER.info("AES_ECB encrypt: the content is null!");
            return null;
        }
        // 判断秘钥是否为16位
        if (StringUtils.isNotBlank(aesKey) && aesKey.length() == 16) {
            try {
                // 对密码进行编码
                byte[] bytes = aesKey.getBytes(ENCODING);
                // 设置加密算法，生成秘钥
                SecretKeySpec skeySpec = new SecretKeySpec(bytes, AES_ALGORITHM);
                // "算法/模式/补码方式"
                Cipher cipher = Cipher.getInstance(CIPHER_PADDING);
                // 选择加密
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
                // 根据待加密内容生成字节数组
                byte[] encrypted = cipher.doFinal(content.getBytes(ENCODING));
                // 返回base64字符串
                return Base64Utils.encode(encrypted);
            } catch (Exception e) {
                LOGGER.info("AES_ECB encrypt exception:" + e.getMessage());
                throw new UtilException(e.getMessage());
            }
        } else {
            LOGGER.info("AES_ECB encrypt: the aesKey is null or error!");
            return null;
        }
    }

    /**
     * AES解密_ECB
     *
     * @param content 待解密内容
     * @param aesKey  密码
     * @return
     */
    public static String decryptEcb(String content, String aesKey) {
        if (StringUtils.isBlank(content)) {
            LOGGER.info("AES_ECB decrypt: the content is null!");
            return null;
        }
        // 判断秘钥是否为16位
        if (StringUtils.isNotBlank(aesKey) && aesKey.length() == 16) {
            try {
                // 对密码进行编码
                byte[] bytes = aesKey.getBytes(ENCODING);
                // 设置解密算法，生成秘钥
                SecretKeySpec skeySpec = new SecretKeySpec(bytes, AES_ALGORITHM);
                // "算法/模式/补码方式"
                Cipher cipher = Cipher.getInstance(CIPHER_PADDING);
                // 选择解密
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
                // 先进行Base64解码
                byte[] decodeBase64 = Base64Utils.decode(content);
                // 根据待解密内容进行解密
                assert decodeBase64 != null;
                byte[] decrypted = cipher.doFinal(decodeBase64);
                // 将字节数组转成字符串
                return new String(decrypted, ENCODING);
            } catch (Exception e) {
                LOGGER.info("AES_ECB decrypt exception:" + e.getMessage());
                throw new UtilException(e.getMessage());
            }
        } else {
            LOGGER.info("AES_ECB decrypt: the aesKey is null or error!");
            return null;
        }
    }

    /**
     * AES_CBC加密
     *
     * @param content 待加密内容
     * @param aesKey  密码
     * @return
     */
    public static String encryptCbc(String content, String aesKey) {
        if (StringUtils.isBlank(content)) {
            LOGGER.info("AES_CBC encrypt: the content is null!");
            return null;
        }
        // 判断秘钥是否为16位
        if (StringUtils.isNotBlank(aesKey) && aesKey.length() == 16) {
            try {
                // 对密码进行编码
                byte[] bytes = aesKey.getBytes(ENCODING);
                // 设置加密算法，生成秘钥
                SecretKeySpec skeySpec = new SecretKeySpec(bytes, AES_ALGORITHM);
                // "算法/模式/补码方式"
                Cipher cipher = Cipher.getInstance(CIPHER_CBC_PADDING);
                // 偏移
                IvParameterSpec iv = new IvParameterSpec(IV_SEED.getBytes(ENCODING));
                // 选择加密
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
                // 根据待加密内容生成字节数组
                byte[] encrypted = cipher.doFinal(content.getBytes(ENCODING));
                // 返回base64字符串
                return Base64Utils.encode(encrypted);
            } catch (Exception e) {
                LOGGER.info("AES_CBC encrypt exception:" + e.getMessage());
                throw new UtilException(e.getMessage());
            }
        } else {
            LOGGER.info("AES_CBC encrypt: the aesKey is null or error!");
            return null;
        }
    }

    /**
     * AES_CBC解密
     *
     * @param content 待解密内容
     * @param aesKey  密码
     * @return
     */
    public static String decryptCbc(String content, String aesKey) {
        if (StringUtils.isBlank(content)) {
            LOGGER.info("AES_CBC decrypt: the content is null!");
            return null;
        }
        // 判断秘钥是否为16位
        if (StringUtils.isNotBlank(aesKey) && aesKey.length() == 16) {
            try {
                // 对密码进行编码
                byte[] bytes = aesKey.getBytes(ENCODING);
                // 设置解密算法，生成秘钥
                SecretKeySpec skeySpec = new SecretKeySpec(bytes, AES_ALGORITHM);
                // 偏移
                IvParameterSpec iv = new IvParameterSpec(IV_SEED.getBytes(ENCODING));
                // "算法/模式/补码方式"
                Cipher cipher = Cipher.getInstance(CIPHER_CBC_PADDING);
                // 选择解密
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
                // 先进行Base64解码
                byte[] decodeBase64 = Base64Utils.decode(content);
                // 根据待解密内容进行解密
                assert decodeBase64 != null;
                byte[] decrypted = cipher.doFinal(decodeBase64);
                // 将字节数组转成字符串
                return new String(decrypted, ENCODING);
            } catch (Exception e) {
                LOGGER.info("AES_CBC decrypt exception:" + e.getMessage());
                throw new UtilException(e.getMessage());
            }
        } else {
            LOGGER.info("AES_CBC decrypt: the aesKey is null or error!");
            return null;
        }
    }
}
