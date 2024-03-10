package com.sochina.base.properties

import java.io.Serializable

/**
 * @author sochina-heart
 */
open class BaseProperties: Serializable {

    var enable: Boolean = true

    var excludeUrl: MutableList<String>? = null;

    var order: Int = -1
}