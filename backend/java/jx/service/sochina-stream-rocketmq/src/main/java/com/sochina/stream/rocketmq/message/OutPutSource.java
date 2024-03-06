package com.sochina.stream.rocketmq.message;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface OutPutSource {
    @Output("output")
    MessageChannel outPut();
}
