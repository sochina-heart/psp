package com.sochina.base.utils.encrypt.gm.sm4;

import com.sochina.base.utils.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

public class SM4Utils {
    private static final String ENCODING = Sm4Helper.ENCODING.getMsg();
    private static final String ALGORITHM_NAME = Sm4Helper.ALGORITHM_NAME.getMsg();
    private static final String ALGORITHM_NAME_CBC_PADDING = Sm4Helper.ALGORITHM_NAME_CBC_PADDING.getMsg();
    // 加密算法/分组加密模式/分组填充方式
    // PKCS5Padding-以8个字节为一组进行分组加密
    // 定义分组加密模式使用：PKCS5Padding
    private static final String ALGORITHM_NAME_ECB_PADDING = Sm4Helper.ALGORITHM_NAME_ECB_PADDING.getMsg();
    // 128-32位16进制；256-64位16进制
    private static final int DEFAULT_KEY_SIZE = 128;
    private static final String KEY = "bd74842f72ddbce7751dccf708a37c8f";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 自动生成密钥
     */
    private static byte[] generateKey() throws Exception {
        return generateKey(DEFAULT_KEY_SIZE);
    }

    public static String generateKeyString() throws Exception {
        return ByteUtils.toHexString(generateKey());
    }

    /**
     * @param keySize
     */
    private static byte[] generateKey(int keySize) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
        kg.init(keySize, new SecureRandom());
        return kg.generateKey().getEncoded();
    }

    /**
     * sm4加密_CBC
     *
     * @param hexKey   16进制密钥（忽略大小写）
     * @param paramStr 待加密字符串
     *                 返回16进制的加密字符串
     *                 <p>
     *                 加密模式：CBC
     */
    public static String encryptCbc(String hexKey, String paramStr) throws Exception {
        String result = "";
        // 16进制字符串-->byte[]
        byte[] keyData = ByteUtils.fromHexString(hexKey);
        // String-->byte[]
        byte[] srcData = paramStr.getBytes(ENCODING);
        // 加密后的数组
        byte[] cipherArray = encryptCbcPadding(keyData, srcData);
        // byte[]-->hexString
        result = ByteUtils.toHexString(cipherArray);
        return result;
    }

    /**
     * sm4加密_ECB
     *
     * @param hexKey   16进制密钥（忽略大小写）
     * @param paramStr 待加密字符串
     *                 返回16进制的加密字符串
     *                 <p>
     *                 加密模式：CBC
     */
    public static String encryptEcb(String hexKey, String paramStr) throws Exception {
        String result = "";
        // 16进制字符串-->byte[]
        byte[] keyData = ByteUtils.fromHexString(hexKey);
        // String-->byte[]
        byte[] srcData = paramStr.getBytes(ENCODING);
        // 加密后的数组
        byte[] cipherArray = encryptEcbPadding(keyData, srcData);
        // byte[]-->hexString
        result = ByteUtils.toHexString(cipherArray);
        return result;
    }

    /**
     * 加密模式之CBC
     *
     * @param key
     * @param data
     */
    private static byte[] encryptCbcPadding(byte[] key, byte[] data) throws Exception {
        Cipher cipher = generateCbcCipher(ALGORITHM_NAME_CBC_PADDING, Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    private static Cipher generateCbcCipher(String algorithmName, int mode, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(mode, sm4Key, generateIV());
        return cipher;
    }

    /**
     * 加密模式之ECB
     *
     * @param key
     * @param data
     */
    private static byte[] encryptEcbPadding(byte[] key, byte[] data) throws Exception {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    private static Cipher generateEcbCipher(String algorithmName, int mode, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(mode, sm4Key);
        return cipher;
    }

    // 生成iv
    private static AlgorithmParameters generateIV() throws Exception {
        // iv 为一个 16 字节的数组，这里采用和 iOS 端一样的构造方法，数据全为0
        byte[] iv = new byte[16];
        Arrays.fill(iv, (byte) 0x00);
        AlgorithmParameters params = AlgorithmParameters.getInstance(ALGORITHM_NAME);
        params.init(new IvParameterSpec(iv));
        return params;
    }

    /**
     * sm4解密_CBC
     *
     * @param hexKey 16进制密钥
     * @param text   16进制的加密字符串（忽略大小写）
     *               解密后的字符串
     *               <p>
     *               解密模式：采用CBC
     */
    public static String decryptCbc(String hexKey, String text) throws Exception {
        // 用于接收解密后的字符串
        String result = "";
        // hexString-->byte[]
        byte[] keyData = ByteUtils.fromHexString(hexKey);
        // hexString-->byte[]
        byte[] resultData = ByteUtils.fromHexString(text);
        // 解密
        byte[] srcData = decryptCbcPadding(keyData, resultData);
        // byte[]-->String
        result = new String(srcData, ENCODING);
        return result;
    }

    /**
     * sm4解密_ECB
     *
     * @param hexKey 16进制密钥
     * @param text   16进制的加密字符串（忽略大小写）
     *               解密后的字符串
     *               <p>
     *               解密模式：采用ECB
     */
    public static String decryptEcb(String hexKey, String text) throws Exception {
        // 用于接收解密后的字符串
        String result = "";
        // hexString-->byte[]
        byte[] keyData = ByteUtils.fromHexString(hexKey);
        // hexString-->byte[]
        byte[] resultData = ByteUtils.fromHexString(text);
        // 解密
        byte[] srcData = decryptEcbPadding(keyData, resultData);
        // byte[]-->String
        result = new String(srcData, ENCODING);
        return result;
    }

    /**
     * 解密
     *
     * @param key
     * @param cipherText
     */
    private static byte[] decryptCbcPadding(byte[] key, byte[] cipherText) throws Exception {
        Cipher cipher = generateCbcCipher(ALGORITHM_NAME_CBC_PADDING, Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(cipherText);
    }

    /**
     * 解密
     *
     * @param key
     * @param cipherText
     */
    private static byte[] decryptEcbPadding(byte[] key, byte[] cipherText) throws Exception {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(cipherText);
    }

    public static String encryptCbc(String paramStr) throws Exception {
        if (StringUtils.isEmpty(paramStr)) {
            return null;
        }
        return encryptCbc(KEY, paramStr);
    }

    public static String decryptCbc(String paramStr) throws Exception {
        if (StringUtils.isEmpty(paramStr)) {
            return null;
        }
        return decryptCbc(KEY, paramStr);
    }

    public static String encryptEcb(String paramStr) throws Exception {
        if (StringUtils.isEmpty(paramStr)) {
            return null;
        }
        return encryptEcb(KEY, paramStr);
    }

    public static String decryptEcb(String paramStr) throws Exception {
        if (StringUtils.isEmpty(paramStr)) {
            return null;
        }
        return decryptEcb(KEY, paramStr);
    }
}
