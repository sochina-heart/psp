package com.sochina.stream.rocketmq.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface IntPutSource {
    @Input("input")
    SubscribableChannel input();
}
