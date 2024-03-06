package com.sochina.base.conf

import com.sochina.base.utils.thread.runnable.TaskToolExecutor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TaskPoolExecutorConfig {

    @Bean(initMethod = "init", destroyMethod = "destroy")
    fun ciToolExecutor(): TaskToolExecutor {
        val ciToolExecutor = TaskToolExecutor()
        ciToolExecutor.name = "ciToolExecutor"
        ciToolExecutor.coreSize = 8
        ciToolExecutor.maxSize = 16
        ciToolExecutor.queueSize = 512
        return ciToolExecutor
    }

    @Bean(initMethod = "init", destroyMethod = "destroy")
    fun msgExecutor(): TaskToolExecutor {
        val msgExecutor = TaskToolExecutor()
        msgExecutor.name = "msgExecutor"
        msgExecutor.coreSize = 8
        msgExecutor.maxSize = 16
        msgExecutor.queueSize = 512
        return msgExecutor
    }
}