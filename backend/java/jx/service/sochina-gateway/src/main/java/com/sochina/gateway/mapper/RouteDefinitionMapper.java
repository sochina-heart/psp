package com.sochina.gateway.mapper;

import com.sochina.gateway.domain.CustomerRouteDefinition;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author sochina-heart
 */
@Mapper
@Repository
public interface RouteDefinitionMapper {

    List<CustomerRouteDefinition> findAll();

    CustomerRouteDefinition findByRouteId(String routeId);

    CustomerRouteDefinition findById(Long routeDefinitionId);

    boolean updateById(CustomerRouteDefinition route);

    boolean insert(CustomerRouteDefinition route);

    boolean deleteById(Long routeDefinitionId);
}
