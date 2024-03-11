package com.sochina.demo.domain

import com.mybatisflex.annotation.Column
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.KeyType
import com.mybatisflex.annotation.Table
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length
import java.io.Serializable

@Table("sochina_user")
class SochinaUser: BaseDomain(), Serializable {

    @Id(keyType = KeyType.Generator, value = "sUuid")
    var userId: String? = null

    @NotBlank(message = "账号不能为空")
    @Length(min = 1, max = 100, message = "账号长度必须在1-100之间")
    var account: String? = null

    @NotBlank(message = "用户名不能为空")
    @Length(min = 1, max = 100, message = "用户名长度必须在1-100之间")
    @Column("user_name")
    var userName: String? = null

    @Column("user_password")
    var userPassword: String? = null

    @NotBlank(message = "性别不能为空")
    @Length(max = 1, message = "性别长度为1")
    var sex: String? = null

    @NotBlank(message = "邮箱不能为空")
    @Column("user_email")
    var userEmail: String? = null

    @Length(max = 255, message = "地址长度不能超过255")
    @Column("home_address")
    var homeAddress: String? = null

    @Length(max = 255, message = "个人描述长度不能超过255")
    @Column("personal_description")
    var personalDescription: String? = null
}