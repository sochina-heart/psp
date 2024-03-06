package com.sochina.feign.service.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DynamicFeign {
    private static Map<String, RemoteNacosProviderService> processMap = new HashMap<>();
    private FeignClientBuilder feignClientBuilder;

    public DynamicFeign(@Autowired ApplicationContext appContext) {
        this.feignClientBuilder = new FeignClientBuilder(appContext);
    }

    public RemoteNacosProviderService getFeignClient(String serviceName, String path) {
        String key = serviceName + path;
        RemoteNacosProviderService api = processMap.get(key);
        if (api != null)
            return api;
        synchronized (DynamicFeign.class) {
            api = processMap.get(key);
            if (api != null)
                return api;
            api = this.feignClientBuilder.forType(RemoteNacosProviderService.class, serviceName).path(path).build();
            processMap.put(key, api);
        }
        return api;
    }
}
