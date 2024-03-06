package com.sochina.base.jasypt.gm;

import com.sochina.base.exception.CustomerException;
import com.sochina.base.utils.StringUtils;
import com.sochina.base.utils.encrypt.gm.sm4.SM4Utils;
import org.jasypt.encryption.StringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("SM4EncryptorByCbc")
public class SM4EncryptorByCbc implements StringEncryptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SM4EncryptorByCbc.class);

    @Override
    public String encrypt(String s) {
        if (StringUtils.isNotBlank(s)) {
            try {
                s = SM4Utils.encryptCbc(s);
            } catch (Exception e) {
                LOGGER.error("配置文件加密发生异常 - {}", e.getMessage());
                throw new CustomerException("配置文件加密发生异常");
            }
        }
        return s;
    }

    @Override
    public String decrypt(String s) {
        if (StringUtils.isNotBlank(s)) {
            try {
                s = SM4Utils.decryptCbc(s);
            } catch (Exception e) {
                LOGGER.error("配置文件解密发生异常 - {}", e.getMessage());
                throw new CustomerException("配置文件解密发生异常");
            }
        }
        return s;
    }
}
