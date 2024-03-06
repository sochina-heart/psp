package com.sochina.base.domain.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XssProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean enable = true;
    private int order = -1;
    private String sensitiveData;
    private Map<String, String> pattern = new HashMap<>();
    private List<String> excludeUrl = new ArrayList<>();
}
