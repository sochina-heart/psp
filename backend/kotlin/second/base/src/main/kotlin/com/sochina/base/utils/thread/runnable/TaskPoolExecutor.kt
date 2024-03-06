package com.sochina.base.utils.thread.runnable

import cn.hutool.core.date.SystemClock
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

class TaskToolExecutor {
    /**
     * 线程池
     */
    private var pool: ExecutorService? = null

    /**
     * 线程工厂
     */
    private var threadFactory: ThreadFactory? = null

    /**
     * 任务较多时暂存队列
     */
    private var workQueue: BlockingQueue<Runnable>? = null

    /**
     * 核心线程数
     */
    private var coreSize = 0

    /**
     * 最大线程数
     */
    private var maxSize = 0

    /**
     * 空闲线程存活时间
     */
    private var aLiveTime: Long = 0

    /**
     * 队列数量
     */
    var queueSize: Int = 0
        set(value) {
            field = if (value <= 0) {
                DEFAULT_QUEUE_SIZE
            } else {
                value
            }
        }

    /**
     * 线程池名称
     */
    var name: String? = null

    /**
     * 初始化线程池
     */
    fun init() {
        if (null == pool) {
            if (null == workQueue) {
                queueSize = if (queueSize > 0) queueSize else DEFAULT_QUEUE_SIZE
                workQueue = LinkedBlockingQueue(queueSize)
            }
            if (null == threadFactory) {
                threadFactory = defaultThreadFactory()
            }
            coreSize = if (coreSize > 0) coreSize else DEFAULT_CORE_SIZE
            maxSize = if (maxSize > 0) maxSize else DEFAULT_MAX_SIZE
            aLiveTime = if (aLiveTime > 0) aLiveTime else DEFAULT_ALIVE_TIME
            pool = ThreadPoolExecutor(coreSize, maxSize, aLiveTime, TimeUnit.SECONDS, workQueue, threadFactory)
        }
    }

    /**
     * 销毁线程池
     */
    fun destroy() {
        pool!!.shutdown()
    }

    /**
     * 执行Task
     *
     * @param worker
     */
    fun execute(worker: Worker<*>) {
        try {
            worker.executorName = this.name
            worker.prepareExecutionTime = SystemClock.now()
            pool!!.execute(worker)
        } catch (e: RejectedExecutionException) {
            // 拒绝策略
            dealWhenPoolFull(worker, e)
        }
    }

    /**
     * 提交Task，可获取线程返回结果
     *
     * @param worker
     * @param <T>
     * @return
    </T> */
    fun <T> submit(worker: Worker<T>): T? {
        try {
            val future = pool!!.submit(worker.callable())
            val threadResult = future[worker.timeout, TimeUnit.MILLISECONDS]
            return threadResult.value
        } catch (e: RejectedExecutionException) {
            LOGGER.error("Rejected worker: Perhaps thread pool is full!", e)
        } catch (e: InterruptedException) {
            LOGGER.error("Interrupted worker:", e)
        } catch (e: ExecutionException) {
            LOGGER.error("Attempting to retrieve the ThreadResult of a task that aborted!", e)
        } catch (e: TimeoutException) {
            LOGGER.error("Timeout worker: get ThreadResult timeout", e)
        }
        return worker.ThreadResult.value
    }

    /**
     * 线程池占满之后拒绝策略
     *
     * @param worker
     * @param e
     */
    private fun dealWhenPoolFull(worker: Worker<*>, e: RejectedExecutionException) {
        when (worker.poolOverAct) {
            Pooled.PoolOverAct.RUN -> worker.run()
            Pooled.PoolOverAct.BLOCK -> try {
                workQueue!!.put(worker)
            } catch (interruptedException: InterruptedException) {
                LOGGER.error("queue put worker: Perhaps block queue is full!", e)
            }

            Pooled.PoolOverAct.NEW_THREAD -> {
                val newThreadOutOfPool = threadFactory!!.newThread(worker)
                newThreadOutOfPool.name = "outOfPool-" + newThreadOutOfPool.name
                newThreadOutOfPool.start()
            }

            Pooled.PoolOverAct.REJECT -> LOGGER.error("Rejected worker: Perhaps thread pool is full!", e)
        }
    }

    /**
     * 默认线程工厂
     */
    class DefaultThreadFactory : ThreadFactory {
        private val group: ThreadGroup = Thread.currentThread().threadGroup
        private val threadNumber: AtomicInteger = AtomicInteger(1)
        private val namePrefix: String = "taskTool-" + poolNumber.getAndIncrement() + "-thread-"

        override fun newThread(runnable: Runnable): Thread {
            val thread = Thread(group, runnable, namePrefix + threadNumber.getAndIncrement(), 0)
            // 守护线程
            if (thread.isDaemon) thread.isDaemon = false
            // 线程优先级
            if (thread.priority != Thread.NORM_PRIORITY) thread.priority = Thread.NORM_PRIORITY
            return thread
        }

        companion object {
            val poolNumber: AtomicInteger = AtomicInteger(1)
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(TaskToolExecutor::class.java)

        private val cpuSize = Runtime.getRuntime().availableProcessors()
        /**
         * 默认核心线程数
         */
        private val DEFAULT_CORE_SIZE = cpuSize * 2 + 1

        /**
         * 默认最大线程数
         */
        private const val DEFAULT_MAX_SIZE = 50

        /**
         * 默认空闲线程存活时间
         */
        private const val DEFAULT_ALIVE_TIME: Long = 60

        /**
         * 默认队列数量
         */
        private const val DEFAULT_QUEUE_SIZE = 1024
        fun defaultThreadFactory(): ThreadFactory {
            return DefaultThreadFactory()
        }
    }
}