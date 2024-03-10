package com.sochina.base.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.io.Serializable

@ConfigurationProperties(prefix = "sochina")
@Configuration
class CommonsProperties: Serializable {

    var xssProperties = XssProperties()
}