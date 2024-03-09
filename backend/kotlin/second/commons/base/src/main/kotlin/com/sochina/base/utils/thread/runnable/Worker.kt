package com.sochina.base.utils.thread.runnable

import cn.hutool.core.date.SystemClock
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import kotlin.concurrent.Volatile

class Worker<T> : Runnable {
    // 执行指令
    var command: Runnable

    // 返回结果
    var ThreadResult: ThreadResult<T> = ThreadResult()

    // 超时
    var timeout: Long

    // 策略
    var poolOverAct: Pooled.PoolOverAct = Pooled.PoolOverAct.REJECT

    // 预备执行时间
    @Volatile
    var prepareExecutionTime: Long = 0

    // 开始执行时间
    @Volatile
    var startExecutionTime: Long = 0

    // 结束执行时间
    @Volatile
    var endExecutionTime: Long = 0

    // 执行的线程池名称
    var executorName: String? = null

    constructor(command: Runnable) {
        this.command = command
        this.timeout = DEFAULT_TIMEOUT
    }

    constructor(command: Runnable, poolOverAct: Pooled.PoolOverAct) {
        this.command = command
        this.timeout = DEFAULT_TIMEOUT
        this.poolOverAct = poolOverAct
    }

    constructor(command: Runnable, threadResult: ThreadResult<T>) {
        this.command = command
        this.ThreadResult = threadResult
        this.timeout = DEFAULT_TIMEOUT
    }

    constructor(command: Runnable, threadResult: ThreadResult<T>, timeout: Long) {
        this.command = command
        this.ThreadResult = threadResult
        this.timeout = timeout
    }

    override fun run() {
        startExecution()
        try {
            command.run()
        } finally {
            endExecution()
        }
    }

    /**
     * 开始执行（预备执行耗时）
     */
    fun startExecution() {
        this.startExecutionTime = SystemClock.now()
        LOGGER.info(
            "POOL_DISPATCH_TIME, EXECUTOR: {}, TIME: {} ms", this.executorName,
            prepareTime
        )
    }

    /**
     * 结束执行（执行耗时）
     */
    fun endExecution() {
        this.endExecutionTime = SystemClock.now()
        LOGGER.info(
            "POOL_EXECUTE_TIME, EXECUTOR: {}, TIME: {} ms", this.executorName,
            executionTime
        )
    }

    val prepareTime: Long
        /**
         * 预备耗时
         *
         * @return
         */
        get() = this.startExecutionTime - this.prepareExecutionTime
    val executionTime: Long
        /**
         * 执行耗时
         *
         * @return
         */
        get() = this.endExecutionTime - this.startExecutionTime

    /**
     * callable执行线程
     *
     * @return
     */
    fun callable(): Callable<ThreadResult<T>> {
        return Executors.callable<ThreadResult<T>>(command, ThreadResult)
    }

    fun setThreadResult(ThreadResult: T) {
        this.ThreadResult.value = ThreadResult
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(Worker::class.java)

        // 默认超时时间
        private const val DEFAULT_TIMEOUT: Long = 500
    }
}