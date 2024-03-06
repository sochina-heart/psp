package com.sochina.base.properties.httpClient

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties(prefix = "sochina.http-client.request")
@Configuration
class HttpClientRequestProperties: HttpClientCommonsProperties() {

}