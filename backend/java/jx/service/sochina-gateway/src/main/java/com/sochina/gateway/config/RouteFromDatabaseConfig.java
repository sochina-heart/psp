package com.sochina.gateway.config;

import com.sochina.gateway.handler.RouteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author sochina-heart
 */
@ConditionalOnProperty(value = "sochina.route.db", havingValue = "true")
@Configuration
public class RouteFromDatabaseConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteFromDatabaseConfig.class);

    private final RouteHandler routeHandler;

    @Autowired
    public RouteFromDatabaseConfig(RouteHandler routeHandler) {
        this.routeHandler = routeHandler;
    }

    @PostConstruct
    public void init() {
        LOGGER.info("首次初始化路由....");
        routeHandler.loadRouteConfig();
    }
}
