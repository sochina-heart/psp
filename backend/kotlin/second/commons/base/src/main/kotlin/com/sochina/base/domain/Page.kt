package com.sochina.base.domain

import java.io.Serializable

open class Page<T>: Serializable {
    var records: List<T>? = null
    var pageNumber = 0
    var pageSize = 0
    var totalPage: Long = 0
    var totalRow: Long = 0
}