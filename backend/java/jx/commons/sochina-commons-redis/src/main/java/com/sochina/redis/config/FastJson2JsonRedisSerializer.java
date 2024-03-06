package com.sochina.redis.config;

import com.alibaba.fastjson2.JSON;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private Class<T> clazz;

    public FastJson2JsonRedisSerializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        try {
            return JSON.toJSONString(t).getBytes(DEFAULT_CHARSET);
        } catch (Exception e) {
            throw new SerializationException("Could not serialize: " + e.getMessage(), e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            String str = new String(bytes, DEFAULT_CHARSET);
            return JSON.parseObject(str, clazz);
        } catch (Exception e) {
            throw new SerializationException("Could not deserialize: " + e.getMessage(), e);
        }
    }
}
