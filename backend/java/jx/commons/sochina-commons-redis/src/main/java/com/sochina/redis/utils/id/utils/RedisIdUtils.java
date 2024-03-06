package com.sochina.redis.utils.id.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Component
public class RedisIdUtils {
    private static final long BEGIN_TIMESTAMP = 1668816000L;
    /**
     * 序列号位数
     */
    private static final int COUNT_BITS = 32;
    private StringRedisTemplate stringRedisTemplate;

    public RedisIdUtils(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public long nextID(String keyPrefix) {
        LocalDateTime now = LocalDateTime.now();
        long nowScond = now.toEpochSecond(ZoneOffset.of("+08:00"));
        long timestamp = nowScond - BEGIN_TIMESTAMP;
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        String key = date + ":" + keyPrefix;
        long count = stringRedisTemplate.opsForValue().increment(key);
        String value = stringRedisTemplate.opsForValue().get(key);
        if ("1".equals(value)) {
            stringRedisTemplate.expire(key, 1, TimeUnit.DAYS);
        }
        long ids = timestamp << COUNT_BITS | count;
        return ids;
    }
}