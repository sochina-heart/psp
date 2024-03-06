package com.sochina.base.utils.thread;

import cn.hutool.core.date.SystemClock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Worker<T> implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Worker.class);
    // 默认超时时间
    private static final long DEFAULT_TIMEOUT = 500;
    // 执行指令
    private Runnable command;
    // 返回结果
    private ThreadResult<T> ThreadResult = new ThreadResult<>();
    // 超时
    private long timeout;
    // 策略
    private Pooled.PoolOverAct poolOverAct = Pooled.PoolOverAct.REJECT;
    // 预备执行时间
    private volatile long prepareExecutionTime;
    // 开始执行时间
    private volatile long startExecutionTime;
    // 结束执行时间
    private volatile long endExecutionTime;
    // 执行的线程池名称
    private String executorName;

    public Worker(Runnable command) {
        this.command = command;
        this.timeout = DEFAULT_TIMEOUT;
    }

    public Worker(Runnable command, Pooled.PoolOverAct poolOverAct) {
        this.command = command;
        this.timeout = DEFAULT_TIMEOUT;
        this.poolOverAct = poolOverAct;
    }

    public Worker(Runnable command, T ThreadResult) {
        this.command = command;
        this.ThreadResult = new ThreadResult<>(ThreadResult);
        this.timeout = DEFAULT_TIMEOUT;
    }

    public Worker(Runnable command, T ThreadResult, long timeout) {
        this.command = command;
        this.ThreadResult = new ThreadResult<>(ThreadResult);
        this.timeout = timeout;
    }

    @Override
    public void run() {
        startExecution();
        try {
            command.run();
        } finally {
            endExecution();
        }
    }

    /**
     * 开始执行（预备执行耗时）
     */
    private void startExecution() {
        this.startExecutionTime = SystemClock.now();
        LOGGER.info("POOL_DISPATCH_TIME, EXECUTOR: {}, TIME: {} ms", this.executorName, this.getPrepareTime());
    }

    /**
     * 结束执行（执行耗时）
     */
    private void endExecution() {
        this.endExecutionTime = SystemClock.now();
        LOGGER.info("POOL_EXECUTE_TIME, EXECUTOR: {}, TIME: {} ms", this.executorName, this.getExecutionTime());
    }

    /**
     * 预备耗时
     *
     * @return
     */
    public long getPrepareTime() {
        return this.startExecutionTime - this.prepareExecutionTime;
    }

    /**
     * 执行耗时
     *
     * @return
     */
    public long getExecutionTime() {
        return this.endExecutionTime - this.startExecutionTime;
    }

    /**
     * callable执行线程
     *
     * @return
     */
    public Callable<ThreadResult<T>> callable() {
        return Executors.callable(command, ThreadResult);
    }

    public void setThreadResult(T ThreadResult) {
        if (null != this.ThreadResult) {
            this.ThreadResult.value = ThreadResult;
        }
    }
}
