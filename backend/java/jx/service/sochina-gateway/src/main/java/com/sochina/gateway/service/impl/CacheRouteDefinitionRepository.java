package com.sochina.gateway.service.impl;

import com.sochina.gateway.service.RouteDefinitionCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author sochina-heart
 */
@ConditionalOnProperty(value = "sochina.route.db", havingValue = "true")
@Service
public class CacheRouteDefinitionRepository implements RouteDefinitionRepository {

    private final RouteDefinitionCacheService routeDefinitionCacheService;

    @Autowired
    public CacheRouteDefinitionRepository(RouteDefinitionCacheService routeDefinitionCacheService) {
        this.routeDefinitionCacheService = routeDefinitionCacheService;
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<RouteDefinition> list = routeDefinitionCacheService.getRouteDefinitions();
        return Flux.fromIterable(list);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(r -> {
            routeDefinitionCacheService.save(r);
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            if (routeDefinitionCacheService.has(id)) {
                routeDefinitionCacheService.delete(id);
                return Mono.empty();
            }

            return Mono.defer(() -> Mono.error(new NotFoundException("未找到路由配置：" + routeId)));
        });
    }
}
