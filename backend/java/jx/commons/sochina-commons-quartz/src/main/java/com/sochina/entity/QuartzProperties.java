package com.sochina.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "quartz")
public class QuartzProperties {
    // quartz定时任务是否自动启动
    private boolean enable = true;
    // quartz 参数
    private String instanceName;
    private String instanceId;
    private String instanceIdGeneratorClass;
    // 线程池配置
    private String threadPoolClass;
    private String threadPoolThreadCount;
    private String threadPriority;
    // JobStore配置
    private String jobStoreClass;
    // 集群配置
    private String isClustered;
    private String clusterCheckinInterval;
    private String maxMisfiresToHandleAtATime;
    private String txIsolationLevelSerializable;
    private String misfireThreshold;
    private String tablePrefix;
}
