package com.sochina.feign.service.feign;

import com.sochina.base.utils.web.AjaxResult;
import com.sochina.feign.service.feign.factory.RemoteNacosProviderFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "nacos-provider", fallbackFactory = RemoteNacosProviderFallbackFactory.class)
public interface RemoteNacosProviderService {
    @GetMapping("/provider/getPortReturnEntity")
        // @RequestLine("GET /provider/getPortReturnEntity")
        // @RequestMapping(method = RequestMethod.GET, value = "/provider/getPortReturnEntity")
    AjaxResult getPortReturnEntity(@RequestParam("username") String username,
                                   @RequestParam("addr") String addr,
                                   @RequestParam("age") Integer age);

    @PostMapping("/provider/getInfoNoParam")
        // @RequestLine("POST /provider/getInfoNoParam")
        // @RequestMapping(method = RequestMethod.POST, value = "/provider/getInfoNoParam")
    AjaxResult getInfoNoParam();
}
