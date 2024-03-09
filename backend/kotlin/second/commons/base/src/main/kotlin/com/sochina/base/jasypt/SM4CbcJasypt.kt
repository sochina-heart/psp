package com.sochina.base.jasypt

import com.sochina.base.utils.encrypt.gm.sm4.SM4Utils.decryptCbc
import com.sochina.base.utils.encrypt.gm.sm4.SM4Utils.encryptCbc
import org.jasypt.encryption.StringEncryptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component("SM4CbcJasypt")
class SM4CbcJasypt : StringEncryptor {

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(SM4CbcJasypt::class.java)
    }

    private val key: String = System.getProperty("gs4k")

    override fun encrypt(s: String?): String? {
        try {
            return encryptCbc(key, s)
        } catch (e: Exception) {
            LOGGER.error("配置文件加密发生异常", e)
            throw Exception("配置文件加密发生异常")
        }
    }

    override fun decrypt(s: String?): String? {
        try {
            return decryptCbc(key, s)
        } catch (e: Exception) {
            LOGGER.error("配置文件解密发生异常", e)
            throw Exception("配置文件解密发生异常")
        }
    }
}