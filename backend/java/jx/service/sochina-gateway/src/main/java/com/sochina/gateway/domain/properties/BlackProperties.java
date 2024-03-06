package com.sochina.gateway.domain.properties;

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
public class BlackProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean enable = true;
    private int order = -1;
    private List<String> ipList = new ArrayList<>();
    private List<String> urlList = new ArrayList<>();
}
