package com.sochina.demo.domain

import com.mybatisflex.annotation.Column
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.KeyType
import com.mybatisflex.annotation.Table
import java.io.Serializable

@Table("sochina_user")
class SochinaUser: BaseDomain(), Serializable {

    @Id(keyType = KeyType.Generator, value = "sUuid")
    var userId: String? = null

    var account: String? = null

    @Column("user_name")
    var userName: String? = null

    @Column("user_password")
    var userPassword: String? = null

    var age: Int? = null

    var sex: String? = null

    @Column("user_email")
    var userEmail: String? = null

    @Column("home_address")
    var homeAddress: String? = null

    @Column("personal_description")
    var personalDescription: String? = null
}