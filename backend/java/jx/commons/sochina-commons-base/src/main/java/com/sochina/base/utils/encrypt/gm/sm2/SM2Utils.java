package com.sochina.base.utils.encrypt.gm.sm2;

import com.sochina.base.exception.UtilException;
import com.sochina.base.utils.StringUtils;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class SM2Utils {
    private static final Logger LOGGER = LoggerFactory.getLogger(SM2Utils.class);
    private static final int DIGEST_LENGTH = 32;
    // private static final String FWF_PUBLIC_KEY = "AlTPz/SfIhn85qZBiHTYprnneeUs049ECaaqEzZC/G4P";
    private static final String PUBLIC_KEY = "PUBLIC_KEY";
    private static final String PRIVATE_KEY = "PRIVATE_KEY";
    private static final String PUBLIC_KEY_VALUE = "A5OHqeG/hRLZ0RL4qDhNG0gF6sleCxkT9OTJayS/L6ZN";
    private static final String PRIVATE_KEY_VALUE = "bKMuRqbRa3au5h4pkhnxC7q4Je8Xv+yEzSzN+Qpz9rA=";
    private static BigInteger n = new BigInteger(
            "FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "7203DF6B" + "21C6052B" + "53BBF409" + "39D54123", 16);
    private static BigInteger p = new BigInteger(
            "FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "00000000" + "FFFFFFFF" + "FFFFFFFF", 16);
    private static BigInteger a = new BigInteger(
            "FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "00000000" + "FFFFFFFF" + "FFFFFFFC", 16);
    private static BigInteger b = new BigInteger(
            "28E9FA9E" + "9D9F5E34" + "4D5A9E4B" + "CF6509A7" + "F39789F5" + "15AB8F92" + "DDBCBD41" + "4D940E93", 16);
    private static BigInteger gx = new BigInteger(
            "32C4AE2C" + "1F198119" + "5F990446" + "6A39C994" + "8FE30BBF" + "F2660BE1" + "715A4589" + "334C74C7", 16);
    private static BigInteger gy = new BigInteger(
            "BC3736A2" + "F4F6779C" + "59BDCEE3" + "6B692153" + "D0A9877C" + "C62A4740" + "02DF32E5" + "2139F0A0", 16);
    private static ECDomainParameters ECC_BC_SPEC;
    private static SecureRandom random = new SecureRandom();
    private static ECCurve.Fp curve;
    private static ECPoint G;

    static {
        curve = new ECCurve.Fp(p, a, b);
        G = curve.createPoint(gx, gy);
        ECC_BC_SPEC = new ECDomainParameters(curve, G, n);
    }

    public static Map<String, Object> generateKeyPairMap() {
        Map<String, Object> keyMap = new HashMap<>(4);
        BigInteger privateKey = random(n.subtract(new BigInteger("1")));
        ECPoint publicKey = G.multiply(privateKey).normalize();
        if (checkPublicKey(publicKey)) {
            keyMap.put(PUBLIC_KEY, publicKey);
            keyMap.put(PRIVATE_KEY, privateKey);
            return keyMap;
        } else {
            return null;
        }
    }

    public static String getPublicKey(Map<String, Object> keyMap) {
        ECPoint publicKey = (ECPoint) keyMap.get(PUBLIC_KEY);
        byte[] keyEncoded = publicKey.getEncoded(true);
        return encryptBase64(keyEncoded);
    }

    public static String getPrivateKey(Map<String, Object> keyMap) {
        BigInteger privateKey = (BigInteger) keyMap.get(PRIVATE_KEY);
        byte[] keyEncoded = privateKey.toByteArray();
        return encryptBase64(keyEncoded);
    }

    public static String encrypt(String input, String publicKey) {
        byte[] encrypt = null;
        try {
            // String data = URLEncoder.encode(input, "utf-8");
            String data = input;
            byte[] keyByte = decryptBase64(publicKey);
            ECPoint point = curve.decodePoint(keyByte);
            encrypt = encrypt(data, point);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new UtilException(e.getMessage());
        }
        // return Hex.toHexString(encrypt);  //16进制 字符串
        return encryptBase64(encrypt);  // base64
    }

    private static byte[] encrypt(String input, ECPoint publicKey) {
        // byte[] inputBuffer = input.getBytes();
        byte[] inputBuffer = input.getBytes(StandardCharsets.UTF_8);
        byte[] c1Buffer;
        ECPoint kpb;
        byte[] t;
        do {
            BigInteger k = random(n);
            ECPoint c1 = G.multiply(k);
            c1Buffer = c1.getEncoded(false);
            BigInteger h = ECC_BC_SPEC.getH();
            if (h != null) {
                ECPoint s = publicKey.multiply(h);
                if (s.isInfinity()) {
                    throw new IllegalStateException();
                }
            }
            kpb = publicKey.multiply(k).normalize();
            byte[] kpbBytes = kpb.getEncoded(false);
            t = kdf(kpbBytes, inputBuffer.length);
        } while (allZero(t));
        byte[] c2 = new byte[inputBuffer.length];
        for (int i = 0; i < inputBuffer.length; i++) {
            c2[i] = (byte) (inputBuffer[i] ^ t[i]);
        }
        byte[] c3 = sm3hash(kpb.getXCoord().toBigInteger().toByteArray(), inputBuffer,
                kpb.getYCoord().toBigInteger().toByteArray());
        byte[] encryptResult = new byte[c1Buffer.length + c2.length + c3.length];
        System.arraycopy(c1Buffer, 0, encryptResult, 0, c1Buffer.length);
        System.arraycopy(c2, 0, encryptResult, c1Buffer.length, c2.length);
        System.arraycopy(c3, 0, encryptResult, c1Buffer.length + c2.length, c3.length);
        return encryptResult;
    }

    public static String decrypt(String input, String privateKeyStr) {
        byte[] keyByte = decryptBase64(privateKeyStr);
        BigInteger privateKey = new BigInteger(keyByte);
        // byte[] enStr = Hex.decode(input); //16进制 字符串
        byte[] enStr = decryptBase64(input); // base64
        return decrypt(enStr, privateKey);
    }

    private static String decrypt(byte[] encryptData, BigInteger privateKey) {
        byte[] c1Byte = new byte[65];
        System.arraycopy(encryptData, 0, c1Byte, 0, c1Byte.length);
        ECPoint c1 = curve.decodePoint(c1Byte).normalize();
        BigInteger h = ECC_BC_SPEC.getH();
        if (h != null) {
            ECPoint s = c1.multiply(h);
            if (s.isInfinity()) {
                throw new IllegalStateException();
            }
        }
        ECPoint dBC1 = c1.multiply(privateKey).normalize();
        byte[] dBC1Bytes = dBC1.getEncoded(false);
        int klen = encryptData.length - 65 - DIGEST_LENGTH;
        byte[] t = kdf(dBC1Bytes, klen);
        if (allZero(t)) {
            LOGGER.error("all zero");
            throw new IllegalStateException();
        }
        byte[] M = new byte[klen];
        for (int i = 0; i < M.length; i++) {
            M[i] = (byte) (encryptData[c1Byte.length + i] ^ t[i]);
        }
        byte[] C3 = new byte[DIGEST_LENGTH];
        System.arraycopy(encryptData, encryptData.length - DIGEST_LENGTH, C3, 0, DIGEST_LENGTH);
        byte[] u = sm3hash(dBC1.getXCoord().toBigInteger().toByteArray(), M,
                dBC1.getYCoord().toBigInteger().toByteArray());
        if (Arrays.equals(u, C3)) {
            return new String(M, StandardCharsets.UTF_8);
        } else {
            return null;
        }
    }

    private static boolean between(BigInteger param, BigInteger min, BigInteger max) {
        return param.compareTo(min) >= 0 && param.compareTo(max) < 0;
    }

    private static boolean checkPublicKey(ECPoint publicKey) {
        if (!publicKey.isInfinity()) {
            BigInteger x = publicKey.getXCoord().toBigInteger();
            BigInteger y = publicKey.getYCoord().toBigInteger();
            if (between(x, new BigInteger("0"), p) && between(y, new BigInteger("0"), p)) {
                BigInteger xResult = x.pow(3).add(a.multiply(x)).add(b).mod(p);
                BigInteger yResult = y.pow(2).mod(p);
                return yResult.equals(xResult) && publicKey.multiply(n).isInfinity();
            }
        }
        return false;
    }

    private static byte[] join(byte[]... params) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] res = null;
        try {
            for (byte[] param : params) {
                baos.write(param);
            }
            res = baos.toByteArray();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new UtilException(e.getMessage());
        }
        return res;
    }

    private static byte[] sm3hash(byte[]... params) {
        byte[] res = null;
        try {
            res = SM2Helper.hash(join(params));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new UtilException(e.getMessage());
        }
        return res;
    }

    private static byte[] ZA(String IDA, ECPoint aPublicKey) {
        byte[] idaBytes = IDA.getBytes();
        int entlenA = idaBytes.length * 8;
        byte[] entla = new byte[]{(byte) (entlenA & 0xFF00), (byte) (entlenA & 0x00FF)};
        return sm3hash(entla, idaBytes, a.toByteArray(), b.toByteArray(), gx.toByteArray(), gy.toByteArray(),
                aPublicKey.getXCoord().toBigInteger().toByteArray(),
                aPublicKey.getYCoord().toBigInteger().toByteArray());
    }

    private static String sign(String M, String signFlag, ECPoint publicKey, BigInteger privateKey) {
        byte[] ZA = ZA(signFlag, publicKey);
        byte[] M_ = join(ZA, M.getBytes());
        BigInteger e = new BigInteger(1, sm3hash(M_));
        BigInteger k;
        BigInteger r;
        do {
            k = random(n);
            ECPoint p1 = G.multiply(k).normalize();
            BigInteger x1 = p1.getXCoord().toBigInteger();
            r = e.add(x1);
            r = r.mod(n);
        } while (r.equals(BigInteger.ZERO) || r.add(k).equals(n));
        BigInteger s = ((privateKey.add(BigInteger.ONE).modInverse(n))
                .multiply((k.subtract(r.multiply(privateKey))).mod(n))).mod(n);
        byte[] rBytes = r.toByteArray();
        byte[] sBytes = s.toByteArray();
        String rBase64String = encryptBase64(rBytes);
        String sBase64String = encryptBase64(sBytes);
        return rBase64String + "," + sBase64String;
    }

    private static byte[] kdf(byte[] Z, int klen) {
        int ct = 1;
        int end = (int) Math.ceil(klen * 1.0 / 32);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            for (int i = 1; i < end; i++) {
                baos.write(sm3hash(Z, SM2Helper.toByteArray(ct)));
                ct++;
            }
            byte[] last = sm3hash(Z, SM2Helper.toByteArray(ct));
            if (klen % 32 == 0) {
                baos.write(last);
            } else {
                baos.write(last, 0, klen % 32);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    private static String encryptBase64(byte[] key) {
        return new String(Base64.getEncoder().encode(key), StandardCharsets.UTF_8);
    }

    private static byte[] decryptBase64(String key) {
        try {
            return Base64.getDecoder().decode(key.getBytes(StandardCharsets.UTF_8));
        } catch (IllegalArgumentException e) {
            byte[] bytes;
            try {
                bytes = Base64.getMimeDecoder().decode(key.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e1) {
                throw new UtilException(key + " Base64.getMimeDecoder()报错", e1);
            }
            if (null == bytes || bytes.length == 0) {
                throw new UtilException(key + " Base64.getMimeDecoder()转byte为null");
            }
            return bytes;
        }
    }

    private static BigInteger random(BigInteger max) {
        BigInteger r = new BigInteger(256, random);
        while (r.compareTo(max) >= 0) {
            r = new BigInteger(128, random);
        }
        return r;
    }

    private static boolean allZero(byte[] buffer) {
        if (null == buffer || buffer.length == 0) {
            throw new UtilException("buffer为null");
        }
        for (byte value : buffer) {
            if (value != 0) {
                return false;
            }
        }
        return true;
    }

    public static String encrypt(String input) {
        if (StringUtils.isEmpty(input)) {
            return null;
        }
        return encrypt(input, PUBLIC_KEY_VALUE);
    }

    public static String decrypt(String input) {
        if (StringUtils.isEmpty(input)) {
            return null;
        }
        return decrypt(input, PRIVATE_KEY_VALUE);
    }
}
