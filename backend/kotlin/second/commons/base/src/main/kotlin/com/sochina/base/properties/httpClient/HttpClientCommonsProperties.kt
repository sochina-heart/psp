package com.sochina.base.properties.httpClient

import java.io.Serializable

open class HttpClientCommonsProperties: Serializable {

    val connectRequestTimeout: Long = 1000 * 10
    val responseTimeout: Long = 1000 * 60 * 3
}