package com.sochina.stream.rocketmq.consumer;

import com.sochina.base.utils.GsonUtils;
import com.sochina.stream.rocketmq.entity.DemoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
public class DemoConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoConsumer.class);

    @StreamListener("input")
    public void onMessage(String json) {
        LOGGER.info(json + "---");
        DemoMessage message = GsonUtils.fromJson(json, DemoMessage.class);
        LOGGER.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }
}
