package com.sochina.base.domain.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@ConfigurationProperties(prefix = "sochina")
@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    private SqlProperties sqlProperties = new SqlProperties();
    private XssProperties xssProperties = new XssProperties();
}
