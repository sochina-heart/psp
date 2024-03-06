package com.sochina.gateway.domain;

import com.alibaba.fastjson2.JSONException;
import com.sochina.base.utils.GsonUtils;
import com.sochina.base.utils.StringUtils;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.net.URI;
import java.util.List;

/**
 * @author sochina-heart
 */
public class CustomerRouteDefinition implements Serializable {

    /**
     * 路由定义主键
     */
    private Long routeDefinitionId;

    /**
     * 路由id
     */
    @NotBlank(message = "路由id不能为空")
    private String routeId;

    /**
     * 目标uri
     */
    @NotBlank(message = "目标uri不能为空")
    private String routeUri;

    /**
     * 断言
     */
    @NotNull(message = "断言不能为空")
    private Object routePredicates;

    /**
     * 过滤器
     */
    @NotNull(message = "过滤器不能为空")
    private Object routeFilters;

    /**
     * 序号
     */
    private int routeOrder;

    /**
     * 状态 0启用 1未启用
     */
    private String routeState;

    /**
     * 删除标志 0未删除 1已删除
     */
    private String routeDelete;

    /**
     * 更新时间
     */
    private String updateTime;

    public CustomerRouteDefinition() {
    }

    public CustomerRouteDefinition(Long routeDefinitionId, String routeId, String routeUri, Object routePredicates, Object routeFilters, int routeOrder, String routeState, String routeDelete, String updateTime) {
        this.routeDefinitionId = routeDefinitionId;
        this.routeId = routeId;
        this.routeUri = routeUri;
        this.routePredicates = routePredicates;
        this.routeFilters = routeFilters;
        this.routeOrder = routeOrder;
        this.routeState = routeState;
        this.routeDelete = routeDelete;
        this.updateTime = updateTime;
    }

    public Long getRouteDefinitionId() {
        return routeDefinitionId;
    }

    public void setRouteDefinitionId(Long routeDefinitionId) {
        this.routeDefinitionId = routeDefinitionId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteUri() {
        return routeUri;
    }

    public void setRouteUri(String routeUri) {
        this.routeUri = routeUri;
    }

    public Object getRoutePredicates() {
        return routePredicates;
    }

    public void setRoutePredicates(Object routePredicates) {
        this.routePredicates = routePredicates;
    }

    public Object getRouteFilters() {
        return routeFilters;
    }

    public void setRouteFilters(Object routeFilters) {
        this.routeFilters = routeFilters;
    }

    public int getRouteOrder() {
        return routeOrder;
    }

    public void setRouteOrder(int routeOrder) {
        this.routeOrder = routeOrder;
    }

    public String getRouteState() {
        return routeState;
    }

    public void setRouteState(String routeState) {
        this.routeState = routeState;
    }

    public String getRouteDelete() {
        return routeDelete;
    }

    public void setRouteDelete(String routeDelete) {
        this.routeDelete = routeDelete;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "CustomerRouteDefinition{" +
                "routeDefinitionId=" + routeDefinitionId +
                ", routeId='" + routeId + '\'' +
                ", routeUri='" + routeUri + '\'' +
                ", routePredicates=" + routePredicates +
                ", routeFilters=" + routeFilters +
                ", routeOrder=" + routeOrder +
                ", routeState='" + routeState + '\'' +
                ", routeDelete='" + routeDelete + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }

    /**
     * 将CustomerRouteDefinition转换为RouteDefinition
     *
     * @return RouteDefinition
     * @throws JSONException
     */
    public RouteDefinition parseToRoute() throws JSONException {
        if (StringUtils.isBlank(routeId) || StringUtils.isBlank(routeUri)) {
            return null;
        }

        String routePredicates = String.valueOf(this.routePredicates);
        List<PredicateDefinition> predicateDefinitions = StringUtils.isBlank(routePredicates)
                ? null : GsonUtils.fromJsonToList(routePredicates, PredicateDefinition.class);

        String routeFilters = String.valueOf(this.routeFilters);
        List<FilterDefinition> filterDefinitions = StringUtils.isBlank(routeFilters)
                ? null : GsonUtils.fromJsonToList(routeFilters, FilterDefinition.class);

        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(routeId);
        routeDefinition.setUri(URI.create(routeUri));
        routeDefinition.setPredicates(predicateDefinitions);
        routeDefinition.setFilters(filterDefinitions);
        routeDefinition.setOrder(routeOrder);
        return routeDefinition;
    }
}
