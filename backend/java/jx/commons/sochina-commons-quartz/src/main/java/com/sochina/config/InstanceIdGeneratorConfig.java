package com.sochina.config;

import com.sochina.base.utils.id.uuid.UuidUtils;
import org.quartz.spi.InstanceIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstanceIdGeneratorConfig implements InstanceIdGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstanceIdGeneratorConfig.class);

    @Override
    public String generateInstanceId() {
        LOGGER.info("生成instanceId");
        return UuidUtils.fastSimpleUUID();
    }
}
