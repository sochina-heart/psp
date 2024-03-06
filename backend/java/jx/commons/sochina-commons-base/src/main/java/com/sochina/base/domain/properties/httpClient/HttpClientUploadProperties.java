package com.sochina.base.domain.properties.httpClient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@ConfigurationProperties(prefix = "sochina.http-client.request")
@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpClientUploadProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    private int connectTimeout = 1000 * 60;
    private int connectRequestTimeout = 1000 * 10;
    private int socketTimeout = 1000 * 60 * 3;
}
