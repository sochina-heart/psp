package com.sochina.config;

import com.sochina.entity.QuartzProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class ScheduleConfig {
    @Resource
    private QuartzProperties properties;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(dataSource);
        // quartz参数
        Properties prop = new Properties();
        prop.put("org.quartz.scheduler.instanceName", properties.getInstanceName());
        prop.put("org.quartz.scheduler.instanceId", properties.getInstanceId());
        prop.put("org.quartz.scheduler.instanceIdGenerator.class", properties.getInstanceIdGeneratorClass());
        // 线程池配置
        prop.put("org.quartz.threadPool.class", properties.getThreadPoolClass());
        prop.put("org.quartz.threadPool.threadCount", properties.getThreadPoolThreadCount());
        prop.put("org.quartz.threadPool.threadPriority", properties.getThreadPriority());
        // JobStore配置
        prop.put("org.quartz.jobStore.class", properties.getJobStoreClass());
        // 集群配置
        prop.put("org.quartz.jobStore.isClustered", properties.getIsClustered());
        prop.put("org.quartz.jobStore.clusterCheckinInterval", properties.getClusterCheckinInterval());
        prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", properties.getMaxMisfiresToHandleAtATime());
        prop.put("org.quartz.jobStore.txIsolationLevelSerializable", properties.getTxIsolationLevelSerializable());
        prop.put("org.quartz.jobStore.misfireThreshold", properties.getMisfireThreshold());
        prop.put("org.quartz.jobStore.tablePrefix", properties.getTablePrefix());
        factory.setQuartzProperties(prop);
        factory.setSchedulerName("SochinaScheduler");
        // 延时启动
        factory.setStartupDelay(1);
        factory.setApplicationContextSchedulerContextKey("applicationContextKey");
        // 可选，QuartzScheduler
        // 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        factory.setOverwriteExistingJobs(true);
        // 设置自动启动，默认为true
        factory.setAutoStartup(properties.isEnable());
        return factory;
    }
}
