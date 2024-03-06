package com.sochina.base.domain.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqlProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean enable = true;
    private String pattern;
    private String sensitiveData;
    private String keyword;
    private List<String> excludeUrl = new ArrayList<>();
}
