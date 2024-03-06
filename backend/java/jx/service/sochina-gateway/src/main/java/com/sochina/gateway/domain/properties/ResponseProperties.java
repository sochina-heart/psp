package com.sochina.gateway.domain.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean enable = true;
    private int order = -1;
    private Map<String, String> header = new HashMap<>();
}
