package com.sochina.base.properties.httpClient

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.io.Serializable

@ConfigurationProperties(prefix = "sochina.http-client.response")
@Configuration
class HttpClientUploadProperties: HttpClientCommonsProperties(), Serializable {
}