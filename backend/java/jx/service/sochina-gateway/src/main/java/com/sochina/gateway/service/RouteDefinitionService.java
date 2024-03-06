package com.sochina.gateway.service;

import com.sochina.gateway.domain.CustomerRouteDefinition;

import java.util.List;

/**
 * @author sochina-heart
 */
public interface RouteDefinitionService {

    List<CustomerRouteDefinition> findAll();

    CustomerRouteDefinition findByRouteId(String routeId);

    CustomerRouteDefinition findById(Long routeDefinitionId);

    boolean saveOrUpdate(CustomerRouteDefinition route);

    // boolean updateById(CustomerRouteDefinition route);
    //
    // boolean insert(CustomerRouteDefinition route);

    boolean deleteById(CustomerRouteDefinition route);
}
