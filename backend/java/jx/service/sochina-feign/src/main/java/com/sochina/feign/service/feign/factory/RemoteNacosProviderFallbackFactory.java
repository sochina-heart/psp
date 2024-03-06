package com.sochina.feign.service.feign.factory;

import com.sochina.base.utils.web.AjaxResult;
import com.sochina.feign.service.feign.RemoteNacosProviderService;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RemoteNacosProviderFallbackFactory implements FallbackFactory<RemoteNacosProviderService> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteNacosProviderFallbackFactory.class);

    @Override
    public RemoteNacosProviderService create(Throwable throwable) {
        LOGGER.info("nacos-provider调用失败", throwable);
        return new RemoteNacosProviderService() {
            @Override
            public AjaxResult getPortReturnEntity(String username, String addr, Integer age) {
                return AjaxResult.error();
            }

            @Override
            public AjaxResult getInfoNoParam() {
                return AjaxResult.success("hello world");
            }
        };
    }
}
