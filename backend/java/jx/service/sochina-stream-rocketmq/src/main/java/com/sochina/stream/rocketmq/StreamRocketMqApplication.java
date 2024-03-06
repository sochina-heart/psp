package com.sochina.stream.rocketmq;

import com.sochina.stream.rocketmq.message.IntPutSource;
import com.sochina.stream.rocketmq.message.OutPutSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableBinding({OutPutSource.class, IntPutSource.class})
public class StreamRocketMqApplication {
    public static void main(String[] args) {
        SpringApplication.run(StreamRocketMqApplication.class, args);
    }
}
