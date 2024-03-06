package com.sochina.mvc.annotation.log;

import com.sochina.base.enums.log.BusinessType;
import com.sochina.base.enums.log.OperatorType;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogMvc {
    /**
     * 模块
     */
    public String title() default "";

    /**
     * 功能
     */
    public BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类别
     */
    public OperatorType operatorType() default OperatorType.DEV;

    /**
     * 是否保存请求头
     */
    public boolean isSaveRequestHeaders() default true;

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;

    /**
     * 是否保存响应头
     */
    public boolean isSaveResponseHeaders() default true;

    /**
     * 是否保存响应的参数
     */
    public boolean isSaveResponseData() default true;

    /**
     * 排除指定的请求参数
     */
    public String[] excludeParamNames() default {};
}
