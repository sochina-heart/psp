package com.sochina.demo.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class DurationAspect {
    @Around("execution(public void com.jly.controller.SalariesController.exportExcel*(..))")
    fun exportExcel(joinPoint: ProceedingJoinPoint) {
        val startTime = System.nanoTime()
        LOGGER.info("开始导出：" + joinPoint.signature.name)
        try {
            joinPoint.proceed()
        } catch (e: Throwable) {
            throw RuntimeException(e)
        } finally {
            val time = Duration.ofNanos(System.nanoTime() - startTime)
            LOGGER.info("导出结束，消耗了：" + time.seconds + "s")
        }
    }

    @Around("execution(public void com.jly.controller.SalariesController.importExcel*(..))")
    fun importExcel(joinPoint: ProceedingJoinPoint) {
        val startTime = System.nanoTime()
        LOGGER.info("开始导入：" + joinPoint.signature.name)
        try {
            joinPoint.proceed()
        } catch (e: Throwable) {
            throw RuntimeException(e)
        } finally {
            val time = Duration.ofNanos(System.nanoTime() - startTime)
            LOGGER.info("导入结束，消耗了：" + time.seconds + "s")
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(DurationAspect::class.java)
    }
}
