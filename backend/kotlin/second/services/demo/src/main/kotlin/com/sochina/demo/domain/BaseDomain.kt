package com.sochina.demo.domain

import com.baomidou.mybatisplus.annotation.TableField
import java.util.*

open class BaseDomain {

    var state: String? = null

    @TableField("delete_flag")
    var deleteFlag: String? = null

    @TableField("create_by")
    var createBy: String? = null

    @TableField("create_time")
    var createTime: Date? = null

    @TableField("update_by")
    var updateBy: String? = null

    @TableField("update_time")
    var updateTime: Date? = null
}