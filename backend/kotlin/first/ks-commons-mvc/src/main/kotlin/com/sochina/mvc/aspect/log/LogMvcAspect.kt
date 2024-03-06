package com.sochina.mvc.aspect.log

import com.sochina.base.domain.OperLog
import com.sochina.base.utils.GsonUtils.toJson
import com.sochina.mvc.annotation.log.LogMvc
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Aspect
@Component
class LogAspect {
    @AfterReturning(pointcut = "@annotation(log)", returning = "jsonResult")
    fun afterReturning(joinPoint: JoinPoint, log: LogMvc, jsonResult: Any?) {
        handleLog(joinPoint, log, null, jsonResult)
    }

    @AfterThrowing(pointcut = "@annotation(log)", throwing = "e")
    fun afterThrowing(joinPoint: JoinPoint, log: LogMvc, e: Exception?) {
        handleLog(joinPoint, log, e, null)
    }

    private fun handleLog(joinPoint: JoinPoint, log: LogMvc, e: Exception?, jsonResult: Any?) {
        // val operLog: OperLog = OperLog()
        // getLogAnnotationDescription(joinPoint, log, operLog, jsonResult)
        // getMethodDescription(joinPoint, log, operLog, e, jsonResult)
        // LOGGER.info(toJson<Any>(operLog))
    }

    private fun getLogAnnotationDescription(joinPoint: JoinPoint, log: LogMvc, operLog: OperLog, jsonResult: Any?) {
        operLog.businessType = log.businessType.ordinal
        operLog.title = log.title
        operLog.operatorType = log.operatorType.ordinal
    }

    /* private fun getMethodDescription(
        joinPoint: JoinPoint,
        log: LogMvc,
        operLog: OperLog,
        e: Exception?,
        jsonResult: Any?
    ) {
        operLog.operTime = Date()
        operLog.status = BusinessStatus.SUCCESS.ordinal
        val methodName = joinPoint.signature.name
        val className = joinPoint.target.javaClass.name
        operLog.method = className + Constants.HALF_STOP + methodName + Constants.HALF_BRACKET
        val request: HttpServletRequest = ServletUtils.getRequest()
        operLog.setRequestMethod(request.method)
        operLog.setOperIp(MvcIpUtils.getHostIp())
        val path: String = ServletUtils.getRequest().getRequestURI()
        operLog.setOperUrl(StringUtils.substring(path, 0, 255))
        if (e != null) {
            operLog.status = BusinessStatus.FAIL.ordinal
            operLog.errorMsg = StringUtils.substring(e.message, 0, 2000)
        }
        if (log.isSaveRequestHeaders) {
            setRequestHeaders(operLog, request)
        }
        if (log.isSaveResponseHeaders) {
            setResponseHeaders(operLog)
        }
        if (log.isSaveResponseData) {
            operLog.jsonResult = StringUtils.substring(toJson(jsonResult), 0, 2000)
        }
    } */

    private fun setRequestHeaders(operLog: OperLog, request: HttpServletRequest) {
        val headerNames = request.headerNames
        val map = HashMap<String, String>()
        while (headerNames.hasMoreElements()) {
            val headerName = headerNames.nextElement()
            val value = request.getHeader(headerName)
            map[headerName] = value
        }
        operLog.requestHeaders = toJson(map)
    }

    /* private fun setResponseHeaders(operLog: OperLog) {
        val response: HttpServletResponse = ServletUtils.getResponse()
        val headerNames = response.headerNames
        val map = HashMap<String, String>()
        for (headerName in headerNames) {
            val value = response.getHeader(headerName)
            map[headerName] = value
        }
        operLog.setResponseHeader(toJson(map))
    } */

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(LogAspect::class.java)
    }
}