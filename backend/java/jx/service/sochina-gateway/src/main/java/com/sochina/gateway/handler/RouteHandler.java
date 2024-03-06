package com.sochina.gateway.handler;

import com.sochina.base.utils.GsonUtils;
import com.sochina.gateway.domain.CustomerRouteDefinition;
import com.sochina.gateway.service.RouteDefinitionService;
import com.sochina.gateway.service.impl.CacheRouteDefinitionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sochina-heart
 */
@Component
public class RouteHandler implements ApplicationEventPublisherAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteHandler.class);

    private ApplicationEventPublisher publisher;

    @Resource
    private RouteDefinitionService routeDefinitionService;

    @Resource
    private CacheRouteDefinitionRepository cacheRouteDefinitionRepository;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public void loadRouteConfig() {
        LOGGER.info("加载路由配置...");
        Flux<RouteDefinition> definitionFlux = cacheRouteDefinitionRepository.getRouteDefinitions();
        new Thread(() -> {
            List<String> existRouteIds = definitionFlux.toStream().map(RouteDefinition::getId).collect(Collectors.toList());
            List<CustomerRouteDefinition> appRouteList = routeDefinitionService.findAll();
            if (appRouteList != null && !appRouteList.isEmpty()) {
                appRouteList.forEach(a -> {
                    if (("1").equals(a.getRouteState()) && existRouteIds.contains(a.getRouteId())) {
                        deleteRoute(a.getRouteId());
                    } else {
                        RouteDefinition routeDefinition = a.parseToRoute();
                        LOGGER.info("loadRoute: {}", GsonUtils.toJson(routeDefinition));
                        if (routeDefinition != null) {
                            cacheRouteDefinitionRepository.save(Mono.just(routeDefinition)).subscribe();
                        }
                    }
                });
            }
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
        }).start();
    }

    public void deleteRoute(String routeId) {
        LOGGER.info("删除路由：{}", routeId);
        cacheRouteDefinitionRepository.delete(Mono.just(routeId)).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }
}
