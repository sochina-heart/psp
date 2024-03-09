package com.sochina.demo.controller

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.sochina.base.utils.uuid.UuidUtils
import com.sochina.base.utils.web.AjaxResult
import com.sochina.demo.domain.SochinaUser
import com.sochina.demo.service.impl.SochinaUserServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping("/user")
class SochinaUserController(
    private val baseServiceImpl: SochinaUserServiceImpl
) {

    private val logger = LoggerFactory.getLogger(SochinaUserController::class.java)

    @PostMapping("/list")
    fun listUser(@RequestBody user: SochinaUser): AjaxResult {
        val queryWrapper = QueryWrapper<SochinaUser>()
            .eq("state", "0")
            .eq("delete_flag", "0")
        user.account.takeIf { !it.isNullOrBlank() }?.let { queryWrapper.like("account", it) }
        user.userName.takeIf { !it.isNullOrBlank() }?.let { queryWrapper.like("user_name", it) }
        user.userEmail.takeIf { !it.isNullOrBlank() }?.let { queryWrapper.like("user_email", it) }
        user.homeAddress.takeIf { !it.isNullOrBlank() }?.let { queryWrapper.like("home_address", it) }
        user.sex.takeIf { it.isNullOrBlank() }?.let { queryWrapper.eq("sex", it) }
        val list = baseServiceImpl.list(queryWrapper)
        return AjaxResult.success(list)
    }

    @PostMapping("add")
    fun addUser(@RequestBody user: SochinaUser): AjaxResult {
        val count = baseServiceImpl.count(QueryWrapper<SochinaUser>().eq("account", user.account))
        if (count > 0) {
            return AjaxResult.success("this user is exist")
        }
        user.userId = UuidUtils.fastSimpleUUID()
        user.createTime = Date()
        baseServiceImpl.save(user)
        return AjaxResult.success("add user success")
    }
}