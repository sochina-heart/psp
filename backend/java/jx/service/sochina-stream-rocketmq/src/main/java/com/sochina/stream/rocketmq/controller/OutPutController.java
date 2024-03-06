package com.sochina.stream.rocketmq.controller;

import com.sochina.base.utils.GsonUtils;
import com.sochina.base.utils.id.uuid.UuidUtils;
import com.sochina.base.utils.web.AjaxResult;
import com.sochina.stream.rocketmq.entity.DemoMessage;
import com.sochina.stream.rocketmq.message.OutPutSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/demo")
public class OutPutController {
    @Resource
    private OutPutSource outPutSource;

    @GetMapping("/demo")
    public AjaxResult demo() {
        DemoMessage message = new DemoMessage();
        message.setId(UuidUtils.simpleUUID());
        String json = GsonUtils.toJson(message);
        Message<String> build = MessageBuilder.withPayload(json).build();
        boolean send = outPutSource.outPut().send(build);
        return AjaxResult.success(send);
    }
}
