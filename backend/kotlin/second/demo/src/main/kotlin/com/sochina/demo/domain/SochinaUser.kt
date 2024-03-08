package com.sochina.demo.domain

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.util.*

@TableName("sochina_user")
class SochinaUser {

    @TableId(type = IdType.INPUT)
    var userId: String? = null

    var account: String? = null

    @TableField("user_name")
    var userName: String? = null

    @TableField("user_password")
    var userPassword: String? = null

    @TableField("user_email")
    var userEmail: String? = null

    @TableField("home_address")
    var homeAddress: String? = null

    @TableField("personal_description")
    var personalDescription: String? = null

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