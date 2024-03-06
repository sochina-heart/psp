package com.sochina.base.config;

import com.sochina.base.utils.thread.TaskToolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorPoolConfig {
    /**
     * 工具类线程池
     *
     * @return
     */
    @Bean(initMethod = "init", destroyMethod = "destroy")
    public TaskToolExecutor ciToolExecutor() {
        TaskToolExecutor ciToolExecutor = new TaskToolExecutor();
        ciToolExecutor.setName("ciToolExecutor");
        ciToolExecutor.setCoreSize(8);
        ciToolExecutor.setMaxSize(16);
        ciToolExecutor.setQueueSize(512);
        return ciToolExecutor;
    }

    /**
     * 消息通知类线程池
     *
     * @return
     */
    @Bean(initMethod = "init", destroyMethod = "destroy")
    public TaskToolExecutor msgExecutor() {
        TaskToolExecutor msgExecutor = new TaskToolExecutor();
        msgExecutor.setName("msgExecutor");
        msgExecutor.setCoreSize(8);
        msgExecutor.setMaxSize(16);
        msgExecutor.setQueueSize(512);
        return msgExecutor;
    }
}
