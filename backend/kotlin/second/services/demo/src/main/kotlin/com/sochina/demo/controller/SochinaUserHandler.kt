package com.sochina.demo.controller

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.TypeReference
import com.mybatisflex.core.paginate.Page
import com.mybatisflex.core.query.QueryWrapper
import com.mybatisflex.kotlin.extensions.db.query
import com.sochina.base.utils.PasswordUtils
import com.sochina.base.utils.encrypt.gm.SM3Utils
import com.sochina.base.utils.web.AjaxResult
import com.sochina.demo.domain.SochinaUser
import com.sochina.demo.service.impl.SochinaUserServiceImpl
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.*

@Component
class SochinaUserHandler(
    private val baseServiceImpl: SochinaUserServiceImpl
) {
    fun listUser(request: ServerRequest): Mono<ServerResponse> {
        val user = request.bodyToMono(SochinaUser::class.java)

        return user.flatMap { sochinaUser ->
            val queryWrapper = QueryWrapper()
                .eq("state", "0")
                .eq("delete_flag", "0")
                .apply {
                    sochinaUser.account?.let { like("account", it) }
                    sochinaUser.userName?.let { like("user_name", it) }
                    sochinaUser.userEmail?.let { like("user_email", it) }
                    sochinaUser.homeAddress?.let { like("home_address", it) }
                    sochinaUser.sex?.let { eq("sex", it) }
                }
                .orderBy("create_time desc")
                .select("user_id", "account", "user_name", "sex", "user_email", "home_address", "personal_description")

            val list = baseServiceImpl.page(
                Page(sochinaUser.page!!.pageNumber, sochinaUser.page!!.pageSize),
                queryWrapper
            )
            ServerResponse.ok().bodyValue(AjaxResult.success(list))
        }
    }

    fun addUser(request: ServerRequest): Mono<ServerResponse> {
        val user = request.bodyToMono(SochinaUser::class.java)
        return user.flatMap { sochinaUser ->
            val count = baseServiceImpl.count(QueryWrapper().eq("account", sochinaUser.account))
            when {
                (count > 0) -> ServerResponse.ok().bodyValue(AjaxResult.error("user account is exist"))
                sochinaUser.userPassword.isNullOrEmpty() -> ServerResponse.ok().bodyValue(AjaxResult.error("user password is empty"))
                (PasswordUtils.validate(sochinaUser.userPassword!!) < 5) -> ServerResponse.ok().bodyValue(AjaxResult.error("user password is weak password"))
                else -> {
                    sochinaUser.createTime = Date()
                    sochinaUser.userPassword = SM3Utils.encrypt(sochinaUser.userPassword!!)
                    baseServiceImpl.save(sochinaUser)
                    ServerResponse.ok().bodyValue(AjaxResult.success("add user success"))
                }
            }
        }
    }

    fun removeUser(request: ServerRequest): Mono<ServerResponse> {
        val str = request.bodyToMono(JSONObject::class.java)
        return str.flatMap { json ->
            val ids = JSON.parseObject(json.getString("ids"), object : TypeReference<List<String>>() {})
            if (ids.isEmpty()) {
                ServerResponse.ok().bodyValue(AjaxResult.success())
            } else {
                val list = mutableListOf<SochinaUser>()
                ids.forEach {
                    list.add(SochinaUser().apply {
                        this.userId = it
                        this.deleteFlag = "1"
                    })
                }
                ServerResponse.ok().bodyValue(AjaxResult.toAjax(baseServiceImpl.updateBatch(list)))
            }
        }
    }

    fun getUser(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id")
        // val queryWrapper = QueryWrapper()
        //     .eq("state", "0")
        //     .eq(SochinaUser::deleteFlag, "0")
        //     .eq(SochinaUser::userId, id)
        // val user = baseServiceImpl.getOne(queryWrapper)
         query {
            select(SochinaUser::id)
        }
        return ServerResponse.ok().bodyValue(user)
    }
}
