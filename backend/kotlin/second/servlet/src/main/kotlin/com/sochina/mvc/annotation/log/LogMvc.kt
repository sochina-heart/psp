package com.sochina.mvc.annotation.log

import com.sochina.base.enum.log.BusinessType
import com.sochina.base.enum.log.OperatorType

@Target(
    AnnotationTarget.FUNCTION,
)
@Retention(
    AnnotationRetention.RUNTIME
)
@MustBeDocumented
annotation class LogMvc(
    /**
     * 模块
     */
    val title: String = "",
    /**
     * 功能
     */
    val businessType: BusinessType = BusinessType.OTHER,
    /**
     * 操作人类别
     */
    val operatorType: OperatorType = OperatorType.DEV,
    /**
     * 是否保存请求头
     */
    val isSaveRequestHeaders: Boolean = true,
    /**
     * 是否保存请求的参数
     */
    val isSaveRequestData: Boolean = true,
    /**
     * 是否保存响应头
     */
    val isSaveResponseHeaders: Boolean = true,
    /**
     * 是否保存响应的参数
     */
    val isSaveResponseData: Boolean = true,
    /**
     * 排除指定的请求参数
     */
    val excludeParamNames: Array<String> = []
)