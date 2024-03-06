package com.sochina.gateway.service.impl;

import com.sochina.base.utils.GsonUtils;
import com.sochina.base.utils.StringUtils;
import com.sochina.gateway.service.RouteDefinitionCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sochina-heart
 */
@ConditionalOnProperty(value = "sochina.route.db", havingValue = "true")
@Service
public class RouteDefinitionCacheServiceImpl implements RouteDefinitionCacheService {

    /**
     * 本次缓存
     */
    private static ConcurrentHashMap<String, RouteDefinition> definitionMap = new ConcurrentHashMap<>();

    @Value("${spring.application.name}")
    private String APPLICATION_NAME;

    /**
     * redis 缓存地址
     */
    private static final String SPACE = "-route";

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<RouteDefinition> getRouteDefinitions() {
        List<RouteDefinition> list = new ArrayList<>();
        if (definitionMap.size() > 0) {
            return new ArrayList<>(definitionMap.values());
        } else {
            redisTemplate.opsForHash().values(APPLICATION_NAME.concat(SPACE))
                    .stream().forEach(r -> {
                        RouteDefinition route = GsonUtils.fromJson(r.toString(), RouteDefinition.class);
                        list.add(route);
                        definitionMap.put(route.getId(), route);
                    });
            return list;
        }
    }

    @Override
    public boolean saveAll(List<RouteDefinition> definitions) {
        if (definitions != null && definitions.size() > 0) {
            definitions.forEach(this::save);
            return true;
        }
        return false;
    }

    @Override
    public boolean has(String routeId) {
        return definitionMap.containsKey(routeId) ? true : redisTemplate.opsForHash().hasKey(APPLICATION_NAME.concat(SPACE), routeId);
    }

    @Override
    public boolean delete(String routeId) {
        if (has(routeId)) {
            definitionMap.remove(routeId);
            redisTemplate.opsForHash().delete(APPLICATION_NAME.concat(SPACE), routeId);
            return true;
        }
        return false;
    }

    @Override
    public boolean save(RouteDefinition r) {
        if (r != null && StringUtils.isNotBlank(r.getId())) {
            definitionMap.put(r.getId(), r);
            redisTemplate.opsForHash().put(APPLICATION_NAME.concat(SPACE), r.getId(), GsonUtils.toJson(r));
            return true;
        }
        return false;
    }
}
