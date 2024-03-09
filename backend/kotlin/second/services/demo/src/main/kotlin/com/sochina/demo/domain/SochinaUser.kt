package com.sochina.demo.domain

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import lombok.Data
import java.util.*

@TableName("sochina_user")
class SochinaUser: BaseDomain() {

    @TableId(type = IdType.INPUT)
    var userId: String? = null

    var account: String? = null

    @TableField("user_name")
    var userName: String? = null

    @TableField("user_password")
    var userPassword: String? = null

    var age: Int? = null

    var sex: String? = null

    @TableField("user_email")
    var userEmail: String? = null

    @TableField("home_address")
    var homeAddress: String? = null

    @TableField("personal_description")
    var personalDescription: String? = null
}