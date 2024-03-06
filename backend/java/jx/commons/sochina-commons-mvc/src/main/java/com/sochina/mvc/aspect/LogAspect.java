package com.sochina.mvc.aspect;

import com.sochina.base.constants.Constants;
import com.sochina.base.enums.log.BusinessStatus;
import com.sochina.base.utils.GsonUtils;
import com.sochina.base.utils.StringUtils;
import com.sochina.mvc.annotation.log.LogMvc;
import com.sochina.mvc.domain.log.OperLog;
import com.sochina.mvc.utils.MvcIpUtils;
import com.sochina.mvc.utils.ServletUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

@Aspect
@Component
public class LogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @AfterReturning(pointcut = "@annotation(log)", returning = "jsonResult")
    public void afterReturning(JoinPoint joinPoint, LogMvc log, Object jsonResult) {
        handleLog(joinPoint, log, null, jsonResult);
    }

    @AfterThrowing(pointcut = "@annotation(log)", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, LogMvc log, Exception e) {
        handleLog(joinPoint, log, e, null);
    }

    private void handleLog(final JoinPoint joinPoint, LogMvc log, final Exception e, Object jsonResult) {
        OperLog operLog = new OperLog();
        getLogAnnotationDescription(joinPoint, log, operLog, jsonResult);
        getMethodDescription(joinPoint, log, operLog, e, jsonResult);
        LOGGER.info(GsonUtils.toJson(operLog));
    }

    private void getLogAnnotationDescription(JoinPoint joinPoint, LogMvc log, OperLog operLog, Object jsonResult) {
        operLog.setBusinessType(log.businessType().ordinal());
        operLog.setTitle(log.title());
        operLog.setOperatorType(log.operatorType().ordinal());
    }

    private void getMethodDescription(JoinPoint joinPoint, LogMvc log, OperLog operLog, final Exception e, Object jsonResult) {
        operLog.setOperTime(new Date());
        operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        operLog.setMethod(className + Constants.HALF_STOP + methodName + Constants.HALF_BRACKET);
        HttpServletRequest request = ServletUtils.getRequest();
        operLog.setRequestMethod(request.getMethod());
        operLog.setOperIp(MvcIpUtils.getHostIp());
        String path = ServletUtils.getRequest().getRequestURI();
        operLog.setOperUrl(StringUtils.substring(path, 0, 255));
        if (e != null) {
            operLog.setStatus(BusinessStatus.FAIL.ordinal());
            operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
        }
        if (log.isSaveRequestHeaders()) {
            setRequestHeaders(operLog, request);
        }
        if (log.isSaveResponseHeaders()) {
            setResponseHeaders(operLog);
        }
        if (log.isSaveResponseData()) {
            operLog.setJsonResult(StringUtils.substring(GsonUtils.toJson(jsonResult), 0, 2000));
        }
    }

    private void setRequestHeaders(OperLog operLog, HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        HashMap<String, String> map = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String value = request.getHeader(headerName);
            map.put(headerName, value);
        }
        operLog.setRequestHeaders(GsonUtils.toJson(map));
    }

    private void setResponseHeaders(OperLog operLog) {
        HttpServletResponse response = ServletUtils.getResponse();
        Collection<String> headerNames = response.getHeaderNames();
        HashMap<String, String> map = new HashMap<>();
        for (String headerName : headerNames) {
            String value = response.getHeader(headerName);
            map.put(headerName, value);
        }
        operLog.setResponseHeader(GsonUtils.toJson(map));
    }
}
