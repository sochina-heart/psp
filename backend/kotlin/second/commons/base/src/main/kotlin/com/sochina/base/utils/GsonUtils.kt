package com.sochina.base.utils

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

object GsonUtils {
    private const val DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"
    private val gson: Gson = GsonBuilder()
        .setDateFormat(DATE_PATTERN)
        .registerTypeAdapter(Date::class.java, DateDeserializer())
        .registerTypeAdapter(Date::class.java, DateSerializer())
        .registerTypeAdapter(Int::class.java, IntSerializer())
        .registerTypeAdapter(Double::class.java, DoubleSerializer())
        .create()

    /**
     * 将Java对象转换成Json字符串
     *
     * @param object 待转换的Java对象
     * @return 转换后的Json字符串
     */
    @JvmStatic
    fun <T> toJson(`object`: T): String {
        return gson.toJson(`object`)
    }

    /**
     * 将Json字符串转换成Java对象
     *
     * @param json  待解析的Json字符串
     * @param clazz 目标Java对象类型
     * @param <T>   目标Java对象类型
     * @return 解析后的Java对象
    </T> */
    @JvmStatic
    fun <T> fromJson(json: String?, clazz: Class<T>?): T {
        return gson.fromJson(json, clazz)
    }

    /**
     * 将Json字符串转换成Java对象
     *
     * @param json 待解析的Json字符串
     * @param type 目标Java对象类型
     * @param <T>  目标Java对象类型
     * @return 解析后的Java对象
    </T> */
    @JvmStatic
    fun <T> fromJson(json: String?, type: Type?): T {
        return gson.fromJson(json, type)
    }

    /**
     * 将JsonElement转换为Java对象
     *
     * @param element JsonElement对象
     * @param clazz   目标Java对象类型
     * @param <T>     目标Java对象类型
     * @return 解析后的Java对象
    </T> */
    @JvmStatic
    fun <T> fromJsonElement(element: JsonElement?, clazz: Class<T>?): T {
        return gson.fromJson(element, clazz)
    }

    /**
     * 将Java对象转换成JsonElement对象
     *
     * @param object 待转换的Java对象
     * @return 转换后的JsonElement对象
     */
    @JvmStatic
    fun toJsonElement(`object`: Any?): JsonElement {
        return gson.toJsonTree(`object`)
    }

    /**
     * 将Json字符串解析成List<T>对象
     *
     * @param json  要解析的Json字符串
     * @param clazz 目标Java对象类型
     * @param <T>   目标Java对象类型
     * @return 解析后的List<T>
    </T></T></T> */
    @JvmStatic
    fun <T> fromJsonToList(json: String?, clazz: Class<T>?): List<T> {
        val type = TypeToken.getParameterized(MutableList::class.java, clazz).type
        return gson.fromJson(json, type)
    }

    /**
     * 将Json字符串解析成Map对象
     *
     * @param json 要解析的Json字符串
     * @return 解析后的Map对象
     */
    @JvmStatic
    fun fromJsonToMap(json: String?): Map<String, Any> {
        val type = object : TypeToken<Map<String?, Any?>?>() {
        }.type
        return gson.fromJson(json, type)
    }

    private class DateDeserializer : JsonDeserializer<Date> {
        @Throws(JsonParseException::class)
        override fun deserialize(
            json: JsonElement, typeOfT: Type,
            context: JsonDeserializationContext
        ): Date {
            val dateStr = json.asString
            try {
                val sdf = SimpleDateFormat(DATE_PATTERN)
                return sdf.parse(dateStr)
            } catch (e: ParseException) {
                throw JsonParseException("Failed to parse date: $dateStr", e)
            }
        }
    }

    private class DateSerializer : JsonSerializer<Date?> {
        override fun serialize(
            date: Date?, typeOfSrc: Type,
            context: JsonSerializationContext
        ): JsonElement {
            val sdf = SimpleDateFormat(DATE_PATTERN)
            return JsonPrimitive(sdf.format(date))
        }
    }

    class IntSerializer : JsonSerializer<Int?> {
        override fun serialize(src: Int?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            return JsonPrimitive(src)
        }
    }

    class DoubleSerializer : JsonSerializer<Double?> {
        override fun serialize(src: Double?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            return JsonPrimitive(src)
        }
    }
}