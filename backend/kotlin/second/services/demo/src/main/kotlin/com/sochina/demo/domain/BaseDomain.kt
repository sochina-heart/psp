package com.sochina.demo.domain

import com.fasterxml.jackson.annotation.JsonFormat
import com.mybatisflex.annotation.Column
import com.sochina.base.domain.Page
import org.apache.poi.ss.formula.functions.T
import java.io.Serializable
import java.util.Date

open class BaseDomain: Serializable {

    var state: String? = null

    @Column("delete_flag")
    var deleteFlag: String? = null

    @Column("create_by")
    var createBy: String? = null

    @Column("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var createTime: Date? = null

    @Column("update_by")
    var updateBy: String? = null

    @Column("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var updateTime: Date? = null

    @Column(ignore = true)
    var page: Page<T>? = null
}