package com.sochina.gateway.controller;

import com.alibaba.fastjson2.JSONObject;
import com.sochina.base.utils.GsonUtils;
import com.sochina.base.utils.StringUtils;
import com.sochina.base.utils.web.AjaxResult;
import com.sochina.gateway.domain.CustomerRouteDefinition;
import com.sochina.gateway.service.RouteDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sochina-heart
 */
@ConditionalOnProperty(value = "sochina.route.db", havingValue = "true")
@RestController
@RequestMapping("/db/route")
public class RouteFromDatabaseController {

    private final RouteDefinitionService routeDefinitionService;

    @Autowired
    public RouteFromDatabaseController(RouteDefinitionService routeDefinitionService) {
        this.routeDefinitionService = routeDefinitionService;
    }

    @GetMapping(value = "/list")
    public AjaxResult list() {
        List<CustomerRouteDefinition> list = routeDefinitionService.findAll();
        return AjaxResult.success(list);
    }

    @PostMapping(value = "/saveOrUpdate")
    public AjaxResult save(@RequestBody @Validated CustomerRouteDefinition route) {
        CustomerRouteDefinition sameRouteIdObj = routeDefinitionService.findByRouteId(route.getRouteId());
        if (sameRouteIdObj != null && sameRouteIdObj.getRouteDefinitionId() != null) {
            if (route.getRouteDefinitionId() == null) {
                return AjaxResult.error("已存在相同 RouteId 的配置");
            }
        }
        route.setRoutePredicates(GsonUtils.toJson(route.getRoutePredicates()));

        route.setRouteFilters(GsonUtils.toJson(route.getRouteFilters()));

        boolean res = routeDefinitionService.saveOrUpdate(route);
        return AjaxResult.toAjax(res);
    }

    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody JSONObject jsonObject) {
        String routeId = jsonObject.getString("routeId");
        CustomerRouteDefinition route = routeDefinitionService.findByRouteId(routeId);
        if (route == null || StringUtils.isBlank(route.getRouteId())) {
            return AjaxResult.error("路由不存在");
        }

        boolean res = routeDefinitionService.deleteById(route);
        return AjaxResult.toAjax(res);
    }
}
