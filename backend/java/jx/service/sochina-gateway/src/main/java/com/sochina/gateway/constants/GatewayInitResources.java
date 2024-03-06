package com.sochina.gateway.constants;

import com.sochina.base.constants.Constants;
import com.sochina.gateway.domain.properties.SochinaProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GatewayInitResources {
    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayInitResources.class);
    public static List<String> EXCLUDE_URL;
    public static List<String> STATIC_URL;
    private final SochinaProperties sochinaProperties;

    public GatewayInitResources(SochinaProperties sochinaProperties) {
        LOGGER.info("init GatewayInitResource start");
        this.sochinaProperties = sochinaProperties;
        EXCLUDE_URL = Optional.ofNullable(sochinaProperties.getExcludeUrl())
                .orElse(Constants.EMPTY_LIST);
        STATIC_URL = Optional.ofNullable(sochinaProperties.getStaticUrl())
                .orElse(Constants.EMPTY_LIST);
    }
}
