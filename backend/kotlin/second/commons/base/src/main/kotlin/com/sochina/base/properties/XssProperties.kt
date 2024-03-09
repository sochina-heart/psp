package com.sochina.base.properties

/**
 * @author sochina-heart
 */
class XssProperties : BaseProperties() {
    var sensitiveData: String = ""
    var pattern: MutableMap<String, String>? = mutableMapOf()
}