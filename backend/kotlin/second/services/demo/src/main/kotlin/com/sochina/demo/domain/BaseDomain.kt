package com.sochina.demo.domain

import com.mybatisflex.annotation.Column
import java.util.Date

open class BaseDomain {

    var state: String? = null

    @Column("delete_flag")
    var deleteFlag: String? = null

    @Column("create_by")
    var createBy: String? = null

    @Column("create_time")
    var createTime: Date? = null

    @Column("update_by")
    var updateBy: String? = null

    @Column("update_time")
    var updateTime: Date? = null
}