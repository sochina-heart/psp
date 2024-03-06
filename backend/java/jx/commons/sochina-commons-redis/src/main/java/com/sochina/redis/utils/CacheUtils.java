package com.sochina.redis.utils;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CacheUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheUtils.class);
    private static volatile CacheUtils singletonInstance;
    private final RedisTemplate redisTemplate;

    private final RedisConnectionFactory redisConnectionFactory;

    private CacheUtils() {
        redisTemplate = this.getRedisTemplate();
        redisConnectionFactory = this.getRedisConnectionFactory();
    }

    public static CacheUtils getInstance() {
        if (singletonInstance == null) {
            synchronized (CacheUtils.class) {
                if (singletonInstance == null) {
                    singletonInstance = new CacheUtils();
                }
            }
        }
        return singletonInstance;
    }

    private RedisTemplate getRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    private RedisConnectionFactory getRedisConnectionFactory() {
        Properties properties = new Properties();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("application.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        Object maxActive = properties.get(RedisConfigKey.MAX_ACTIVE.getExtension());
        poolConfig.setMaxTotal(ObjectUtil.isEmpty(maxActive) ? 8 : (int) maxActive);
        Object maxIdle = properties.get(RedisConfigKey.MAX_IDLE.getExtension());
        poolConfig.setMaxIdle(ObjectUtil.isEmpty(maxIdle) ? 8 : (int) maxIdle);
        Object maxWait = properties.get(RedisConfigKey.MAX_WAIT.getExtension());
        poolConfig.setMaxWaitMillis(ObjectUtil.isEmpty(maxWait) ? -1 : (long) maxWait);
        Object minIdle = properties.get(RedisConfigKey.MIN_IDLE.getExtension());
        poolConfig.setMinIdle(ObjectUtil.isEmpty(minIdle) ? 0 : (int) minIdle);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(false);
        poolConfig.setTestWhileIdle(true);
        Object timeOut = properties.get(RedisConfigKey.TIME_OUT.getExtension());
        JedisClientConfiguration clientConfig = JedisClientConfiguration.builder()
                .usePooling().poolConfig(poolConfig).and().readTimeout(Duration.ofMillis(ObjectUtil.isEmpty(timeOut) ? 10000 : (long) timeOut)).build();
        // 单点redis
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        // 哨兵redis
        // RedisSentinelConfiguration redisConfig = new RedisSentinelConfiguration();
        // 集群redis
        // RedisClusterConfiguration redisConfig = new RedisClusterConfiguration();
        Object host = properties.get(RedisConfigKey.HOST.getExtension());
        redisConfig.setHostName(ObjectUtil.isEmpty(host) ? "127.0.0.1" : (String) host);
        Object password = properties.get(RedisConfigKey.PASSWORD.getExtension());
        redisConfig.setPassword(RedisPassword.of((String) password));
        Object port = properties.get(RedisConfigKey.PORT.getExtension());
        redisConfig.setPort(ObjectUtil.isEmpty(password) ? 6379 : (int) port);
        Object database = properties.get(RedisConfigKey.DATABASE.getExtension());
        redisConfig.setDatabase(ObjectUtil.isEmpty(database) ? 0 : (int) database);
        return new JedisConnectionFactory(redisConfig, clientConfig);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间
     */
    public long getExpire(final String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    public boolean deleteObject(final Collection collection) {
        return redisTemplate.delete(collection) > 0;
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet) {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext()) {
            setOperation.add(it.next());
        }
        return setOperation;
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(final String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey) {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys) {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 删除Hash中的某条数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return 是否成功
     */
    public boolean deleteCacheMapValue(final String key, final String hKey) {
        return redisTemplate.opsForHash().delete(key, hKey) > 0;
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * scan 方法实现
     *
     * @param pattern  表达式
     * @param consumer 对迭代到的key进行操作
     */
    public void scan(String pattern, Consumer<byte[]> consumer) {
        this.redisTemplate.execute((RedisConnection connection) -> {
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(Long.MAX_VALUE).match(pattern).build())) {
                cursor.forEachRemaining(consumer);
                return null;
            } catch (IOException e) {
                LOGGER.error("scan 实现发生异常 ", e);
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 获取符合表达式的key
     *
     * @param pattern 表达式
     * @return
     */
    public Set<String> customKeys(String pattern) {
        Set<String> keys = new HashSet<>();
        this.scan(pattern, item -> {
            keys.add(new String(item, StandardCharsets.UTF_8));
        });
        return keys;
    }

    public Map customHGetAll(final String key) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        return (Map) hashOperations.keys(key).stream()
                .collect(Collectors.toMap(Function.identity(), item -> hashOperations.get(key, item)));
    }
}

enum RedisConfigKey {

    HOST("spring.redis.host"),
    PASSWORD("spring.redis.password"),

    PORT("spring.redis.port"),

    DATABASE("spring.redis.database"),

    TIME_OUT("spring.redis.timeout"),

    MAX_ACTIVE("spring.redis.jedis.pool.max-active"),

    MAX_IDLE("spring.redis.jedis.pool.max-idle"),

    MIN_IDLE("spring.redis.jedis.pool.min-idle"),

    MAX_WAIT("spring.redis.jedis.pool.max-wait"),

    TIME_BETWEEN_EVICTION_RUNS("spring.redis.jedis.pool.time-between-eviction-runs");

    private String extension;

    RedisConfigKey(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}