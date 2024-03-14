package com.sochina.demo.controller

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.TypeReference
import com.mybatisflex.core.paginate.Page
import com.mybatisflex.core.query.QueryWrapper
import com.sochina.base.utils.encrypt.gm.SM3Utils
import com.sochina.base.utils.web.AjaxResult
import com.sochina.demo.domain.SochinaUser
import com.sochina.demo.service.impl.SochinaUserServiceImpl
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*


@RestController
@RequestMapping("/user")
class SochinaUserController(
    private val baseServiceImpl: SochinaUserServiceImpl
) {

    private val logger = LoggerFactory.getLogger(SochinaUserController::class.java)

    @PostMapping("/list")
    fun listUser(@RequestBody user: SochinaUser): Flux<AjaxResult> {
        val queryWrapper = QueryWrapper()
            .eq("state", "0")
            .eq("delete_flag", "0")
            .apply {
                user.account?.let { like("account", it) }
                user.userName?.let { like("user_name", it) }
                user.userEmail?.let { like("user_email", it) }
                user.homeAddress?.let { like("home_address", it) }
                user.sex?.let { eq("sex", it) }
            }
        val list = baseServiceImpl.page(Page(user.page!!.pageNumber, user.page!!.pageSize),queryWrapper)
        return Flux.defer { Flux.just(AjaxResult.success(list)) }
    }

    @PostMapping("/add")
    fun addUser(@RequestBody @Valid user: SochinaUser): Mono<AjaxResult> {
        return Mono.defer {
            val count = baseServiceImpl.count(QueryWrapper().eq("account", user.account))
            if (count > 0) {
                Mono.just(AjaxResult.error("user account is exist"))
            } else {
                if (user.userPassword.isNullOrEmpty()) {
                    Mono.just(AjaxResult.error("user password is empty"))
                } else {
                    user.createTime = Date()
                    user.userPassword = SM3Utils.encrypt(user.userPassword!!)
                    baseServiceImpl.save(user)
                    Mono.just(AjaxResult.success("add user success"))
                }
            }
        }
    }

    @PostMapping("/remove")
    fun removeUser(@RequestBody json: JSONObject): Mono<AjaxResult> {
        return Mono.fromCallable {
            val ids = JSON.parseObject(json.getString("ids"), object : TypeReference<List<String>>() {})
            if (ids.isEmpty()) {
                AjaxResult.success()
            } else {
                val list = mutableListOf<SochinaUser>()
                ids.forEach {
                    list.add(SochinaUser().apply {
                        this.userId = it
                        this.deleteFlag = "1"
                    })
                }
                AjaxResult.toAjax(baseServiceImpl.updateBatch(list))
            }
        }
    }
}