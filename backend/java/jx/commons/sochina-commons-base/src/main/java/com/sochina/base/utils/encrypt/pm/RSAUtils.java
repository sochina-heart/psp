package com.sochina.base.utils.encrypt.pm;

import com.sochina.base.exception.UtilException;
import com.sochina.base.utils.encrypt.Base64Utils;
import org.apache.commons.io.FileUtils;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RSAUtils {
    /**
     * 加密算法RSA
     */
    private static final String KEY_ALGORITHM = "RSA";
    /**
     * 签名算法
     */
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";
    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    /**
     * RSA 密钥位数
     */
    private static final int KEY_SIZE = 1024;
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = KEY_SIZE / 8;
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = MAX_DECRYPT_BLOCK - 11;

    /**
     * 生成密钥对(公钥和私钥)
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     */
    public static String sign(String data, String privateKey) throws Exception {
        byte[] keyBytes = Base64Utils.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data.getBytes());
        return Base64Utils.encode(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     */
    public static boolean verify(String data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data.getBytes());
        return signature.verify(Base64Utils.decode(sign));
    }

    /**
     * 私钥解密
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     */
    private static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        byte[] keyBytes = Base64Utils.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        return init(encryptedData, cipher);
    }

    /**
     * 公钥解密
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     */
    private static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        return init(encryptedData, cipher);
    }

    private static byte[] init(byte[] data, Cipher cipher) throws Exception {
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] result = out.toByteArray();
        out.close();
        return result;
    }

    /**
     * 公钥加密
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     */
    private static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        return init(data, cipher);
    }

    /**
     * 私钥加密
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     */
    private static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64Utils.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        return init(data, cipher);
    }

    /**
     * 获取私钥
     *
     * @param keyMap 密钥对
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64Utils.encode(key.getEncoded());
    }

    /**
     * 获取公钥
     *
     * @param keyMap 密钥对
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64Utils.encode(key.getEncoded());
    }

    /**
     * 生成公私钥文件
     *
     * @param publicFilePath  存储公钥的路径和文件名,例如：D:/pub/public.key
     * @param privateFilePath 存储私钥的路径和文件名,例如：D:/pri/private.key
     */
    public static void createKey(String publicFilePath, String privateFilePath, int keySize) throws Exception {
        // 生成密钥对
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyGen.initialize(keySize, new SecureRandom());
        KeyPair pair = keyGen.generateKeyPair();
        // 将已编码的密钥（公钥和私钥）字节写到文件中
        write(publicFilePath, pair.getPublic());
        write(privateFilePath, pair.getPrivate());
    }

    /**
     * 写入一个对象
     *
     * @param path 路径
     * @param key  密钥对象
     */
    private static void write(String path, Object key) throws Exception {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            // 创建文件目录
            boolean create = file.getParentFile().mkdirs();
            if (!create) {
                return;
            }
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(path)))) {
            oos.writeObject(key);
        } catch (Exception e) {
            throw new UtilException("密钥写入异常", e);
        }
    }

    /**
     * 根据公钥文件存放位置解析为公钥对象
     *
     * @param path 存放位置，例如：D:/pub/public.key
     */
    private static PublicKey resolvePublicKey(String path) throws Exception {
        try (FileInputStream fis = FileUtils.openInputStream(new File(path));
             ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            return (PublicKey) ois.readObject();
        }
    }

    /**
     * 根据私钥文件存放位置解析为私钥对象
     *
     * @param path 存放位置，例如：D:/pri/private.key
     */
    private static PrivateKey resolvePrivateKey(String path) throws Exception {
        try (FileInputStream fis = FileUtils.openInputStream(new File(path));
             ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            return (PrivateKey) ois.readObject();
        }
    }

    /**
     * 将传入的公钥对象转换为经过Base64编码后的公钥字符串
     *
     * @param pubKey
     */
    private static String getBase64PublicKeyString(PublicKey pubKey) {
        return Objects.requireNonNull(Base64Utils.encode(pubKey.getEncoded())).trim();
    }

    /**
     * 将传入的私钥对象转换为经过Base64编码后的私钥字符串
     *
     * @param priKey
     */
    private static String getBase64PrivateKeyString(PrivateKey priKey) {
        return Objects.requireNonNull(Base64Utils.encode(priKey.getEncoded())).trim();
    }

    /**
     * 将传入的公钥存储路径读取公钥后转换为经过Base64编码后的公钥字符串
     *
     * @param path 存放位置，例如：D:/pub/public.key
     */
    public static String getBase64PublicKeyString(String path) throws Exception {
        PublicKey pubKey = resolvePublicKey(path);
        return getBase64PublicKeyString(pubKey);
    }

    /**
     * 将传入的私钥存储路径读取私钥后转换为经过Base64编码后的私钥字符串
     *
     * @param path 存放位置，例如：D:/pri/private.key
     */
    public static String getBase64PrivateKeyString(String path) throws Exception {
        PrivateKey priKey = resolvePrivateKey(path);
        return getBase64PrivateKeyString(priKey);
    }

    /**
     * server端公钥加密
     */
    public static String encryptedDataOnServer(String data, String publicKey) throws Exception {
        return Base64Utils.encode(encryptByPublicKey(data.getBytes(), publicKey));
    }

    /**
     * server端私钥解密
     */
    public static String decryptDataOnServer(String data, String privateKey) throws Exception {
        byte[] rs = Base64Utils.decode(data);
        return new String(RSAUtils.decryptByPrivateKey(rs, privateKey), StandardCharsets.UTF_8);
    }
}
