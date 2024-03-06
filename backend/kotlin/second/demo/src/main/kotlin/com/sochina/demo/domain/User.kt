package com.sochina.demo.domain

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import java.util.*


class User {

    @TableId(type = IdType.NONE)
    var id: String = ""

    var name: String? = null

    var nickName: String? = null

    var createBy: String? = null

    var createTime: Date? = null

    var updateBy: String? = null

    var updateTime: Date? = null
}