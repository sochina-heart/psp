package com.sochina.base.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GsonUtils {
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final Gson gson = new GsonBuilder()
            .setDateFormat(DATE_PATTERN)
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .registerTypeAdapter(Date.class, new DateSerializer())
            .registerTypeAdapter(Integer.class, new IntSerializer())
            .registerTypeAdapter(Double.class, new DoubleSerializer())
            .create();

    /**
     * 将Java对象转换成Json字符串
     *
     * @param object 待转换的Java对象
     * @return 转换后的Json字符串
     */
    public static <T> String toJson(T object) {
        return gson.toJson(object);
    }

    /**
     * 将Json字符串转换成Java对象
     *
     * @param json  待解析的Json字符串
     * @param clazz 目标Java对象类型
     * @param <T>   目标Java对象类型
     * @return 解析后的Java对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    /**
     * 将Json字符串转换成Java对象
     *
     * @param json 待解析的Json字符串
     * @param type 目标Java对象类型
     * @param <T>  目标Java对象类型
     * @return 解析后的Java对象
     */
    public static <T> T fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }

    /**
     * 将JsonElement转换为Java对象
     *
     * @param element JsonElement对象
     * @param clazz   目标Java对象类型
     * @param <T>     目标Java对象类型
     * @return 解析后的Java对象
     */
    public static <T> T fromJsonElement(JsonElement element, Class<T> clazz) {
        return gson.fromJson(element, clazz);
    }

    /**
     * 将Java对象转换成JsonElement对象
     *
     * @param object 待转换的Java对象
     * @return 转换后的JsonElement对象
     */
    public static JsonElement toJsonElement(Object object) {
        return gson.toJsonTree(object);
    }

    /**
     * 将Json字符串解析成List<T>对象
     *
     * @param json  要解析的Json字符串
     * @param clazz 目标Java对象类型
     * @param <T>   目标Java对象类型
     * @return 解析后的List<T>
     */
    public static <T> List<T> fromJsonToList(String json, Class<T> clazz) {
        Type type = TypeToken.getParameterized(List.class, clazz).getType();
        return gson.fromJson(json, type);
    }

    /**
     * 将Json字符串解析成Map对象
     *
     * @param json 要解析的Json字符串
     * @return 解析后的Map对象
     */
    public static Map<String, Object> fromJsonToMap(String json) {
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    private static class DateDeserializer implements com.google.gson.JsonDeserializer<Date> {
        @Override
        public Date deserialize(com.google.gson.JsonElement json, Type typeOfT,
                                com.google.gson.JsonDeserializationContext context)
                throws com.google.gson.JsonParseException {
            String dateStr = json.getAsString();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
                return sdf.parse(dateStr);
            } catch (ParseException e) {
                throw new com.google.gson.JsonParseException("Failed to parse date: " + dateStr, e);
            }
        }
    }

    private static class DateSerializer implements com.google.gson.JsonSerializer<Date> {
        @Override
        public com.google.gson.JsonElement serialize(Date date, Type typeOfSrc,
                                                     com.google.gson.JsonSerializationContext context) {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
            return new com.google.gson.JsonPrimitive(sdf.format(date));
        }
    }

    public static class IntSerializer implements com.google.gson.JsonSerializer<Integer> {
        @Override
        public com.google.gson.JsonElement serialize(Integer src, Type typeOfSrc, com.google.gson.JsonSerializationContext context) {
            return new com.google.gson.JsonPrimitive(src);
        }
    }

    public static class DoubleSerializer implements com.google.gson.JsonSerializer<Double> {
        @Override
        public com.google.gson.JsonElement serialize(Double src, Type typeOfSrc, com.google.gson.JsonSerializationContext context) {
            return new com.google.gson.JsonPrimitive(src);
        }
    }
}
