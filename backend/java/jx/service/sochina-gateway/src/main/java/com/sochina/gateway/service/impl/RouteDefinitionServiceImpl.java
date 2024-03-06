package com.sochina.gateway.service.impl;

import com.sochina.gateway.domain.CustomerRouteDefinition;
import com.sochina.gateway.handler.RouteHandler;
import com.sochina.gateway.mapper.RouteDefinitionMapper;
import com.sochina.gateway.service.RouteDefinitionCacheService;
import com.sochina.gateway.service.RouteDefinitionService;
import com.sochina.redis.utils.id.utils.RedisIdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author sochina-heart
 */
@ConditionalOnProperty(value = "sochina.route.db", havingValue = "true")
@Service
public class RouteDefinitionServiceImpl implements RouteDefinitionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteDefinitionServiceImpl.class);

    private static final String ROUTE_PREFIX = "routeId";

    private final RouteDefinitionMapper routeDefinitionMapper;

    private final RouteDefinitionCacheService routeDefinitionCacheService;

    private final RedisIdUtils redisIdUtils;

    private final RouteHandler routeHandler;

    @Autowired
    public RouteDefinitionServiceImpl(RouteDefinitionMapper routeDefinitionMapper, RouteDefinitionCacheService routeDefinitionCacheService, RedisIdUtils redisIdUtils, @Lazy RouteHandler routeHandler) {
        this.routeDefinitionMapper = routeDefinitionMapper;
        this.routeDefinitionCacheService = routeDefinitionCacheService;
        this.redisIdUtils = redisIdUtils;
        this.routeHandler = routeHandler;
    }

    @Override
    public List<CustomerRouteDefinition> findAll() {
        return routeDefinitionMapper.findAll();
    }

    @Override
    public boolean saveOrUpdate(CustomerRouteDefinition route) {
        CustomerRouteDefinition oldRoute = routeDefinitionMapper.findById(route.getRouteDefinitionId());
        boolean res = false;

        res = Optional.ofNullable(oldRoute)
                .map(CustomerRouteDefinition::getRouteDefinitionId)
                .map(routeDefinitionId -> routeDefinitionMapper.updateById(route))
                .orElseGet(() -> {
                    route.setRouteDefinitionId(redisIdUtils.nextID(ROUTE_PREFIX));
                    return routeDefinitionMapper.insert(route);
                });

        if (res) {
            LOGGER.info("更新缓存，通知网关重新加载路由信息...");
            routeDefinitionCacheService.save(route.parseToRoute());
            routeHandler.loadRouteConfig();
        }

        return res;
    }

    @Override
    public boolean deleteById(CustomerRouteDefinition route) {
        boolean res = routeDefinitionMapper.deleteById(route.getRouteDefinitionId());
        if (res) {
            LOGGER.info("更新缓存，通知网关重新加载路由信息...");
            routeDefinitionCacheService.delete(route.getRouteId());
            routeHandler.loadRouteConfig();
        }
        return res;
    }

    @Override
    public CustomerRouteDefinition findByRouteId(String routeId) {
        return routeDefinitionMapper.findByRouteId(routeId);
    }

    @Override
    public CustomerRouteDefinition findById(Long routeDefinitionId) {
        return routeDefinitionMapper.findById(routeDefinitionId);
    }
}
