package com.sochina.gateway.domain.properties;

import com.sochina.base.domain.properties.SqlProperties;
import com.sochina.base.domain.properties.XssProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "sochina")
@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SochinaProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    private ResponseProperties responseProperties = new ResponseProperties();
    private IllegalProperties illegalProperties = new IllegalProperties();
    private CorsProperties corsProperties = new CorsProperties();
    private BlackProperties blackProperties = new BlackProperties();
    private SqlProperties sqlProperties = new SqlProperties();
    private XssProperties xssProperties = new XssProperties();
    private List<String> excludeUrl = new ArrayList<>();
    private List<String> staticUrl = new ArrayList<>();
}
