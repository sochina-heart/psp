package com.sochina.base.utils.character

import java.util.*
import java.util.regex.Pattern

class XssUtils private constructor(private val commonsProperties: com.sochina.base.properties.CommonsProperties) :
    ICharacterTool {

    companion object {

        @Volatile
        private var instance: XssUtils? = null

        fun getInstance(commonsProperties: com.sochina.base.properties.CommonsProperties): XssUtils {
            return instance ?: synchronized(this) {
                instance ?: XssUtils(commonsProperties).also { instance = it }
            }
        }
    }

    private var XSS_HALF_MAP = mutableMapOf<String, String>()

    private var XSS_FULL_MAP = mutableMapOf<String, String>()

    private var XSS_PATTERN_MAP = mutableMapOf<String, String>()

    private var XSS_SENSITIVE_DATA: List<String>? = emptyList()

    init {
        XSS_PATTERN_MAP = Optional.ofNullable(commonsProperties.xssProperties.pattern).get()
        XSS_SENSITIVE_DATA = commonsProperties.xssProperties.sensitiveData.let { it.customSplit() }
        XSS_SENSITIVE_DATA?.forEach {
            val value = CharUtils.half2Full(it)
            value?.run {
                XSS_HALF_MAP[it] = value
                XSS_FULL_MAP[value] = it
            }
        }
    }

    /**
     * 将字符串中的敏感数据替换成全角字符
     */
    override fun doCheck(str: String): String {
        return str.takeIf { it.isNotBlank() }
            ?.let {
                XSS_HALF_MAP.entries.fold(it) { acc, entry ->
                    acc.replace(entry.key, entry.value)
                }
            }
            ?: ""
    }

    /*
    * 将字符串中的全角字符转换成半角字符
    *  */
    override fun recover(str: String): String {
        return str.takeIf { it.isNotBlank() }
            ?.let {
                XSS_FULL_MAP.entries.fold(it) { acc, entry ->
                    acc.replace(entry.key, entry.value)
                }
            }
            ?: ""
    }

    /*
    * 将所有符合条件的特殊字符转换成空格
    *  */
    fun clean(str: String): String {
        return str.takeIf { it.isNotBlank() }
            ?.let {
                XSS_PATTERN_MAP.entries.fold(it) { acc, entity ->
                    val pattern = Pattern.compile(entity.value, Pattern.CASE_INSENSITIVE)
                    val matcher = pattern.matcher(acc)
                    matcher.replaceAll("")
                }
            }
            ?: ""
    }

    private fun String.customSplit(): List<String> {
        return this.split(",")
    }
}