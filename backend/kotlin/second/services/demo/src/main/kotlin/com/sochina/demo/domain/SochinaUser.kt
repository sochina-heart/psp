package com.sochina.demo.domain

import com.mybatisflex.annotation.Column
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.KeyType
import com.mybatisflex.annotation.Table

@Table("sochina_user")
class SochinaUser: BaseDomain() {

    @Id(keyType = KeyType.None, value = "user_id")
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