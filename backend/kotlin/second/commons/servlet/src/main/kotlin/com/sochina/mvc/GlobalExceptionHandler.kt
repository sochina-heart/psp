package com.sochina.mvc

import com.sochina.base.utils.web.AjaxResult
import jakarta.validation.ConstraintViolationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseBody
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): AjaxResult {
        val fieldErrors = ex.bindingResult.fieldErrors
        val msg = fieldErrors.joinToString(prefix = "参数校验失败:", separator = ",") { fieldError ->
            "${fieldError.field}:${fieldError.defaultMessage}"
        }
        return AjaxResult(400, msg)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseBody
    fun handleConstraintViolationException(ex: ConstraintViolationException): AjaxResult {
        val msg = ex.message?.substringAfter(":", missingDelimiterValue = "") ?: ""
        return AjaxResult(400, "参数校验失败:$msg")
    }
}