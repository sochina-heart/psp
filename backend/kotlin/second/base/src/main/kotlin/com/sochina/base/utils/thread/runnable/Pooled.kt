package com.sochina.base.utils.thread.runnable

annotation class Pooled(
    val async: Boolean = true,
    val timeout: Long = 500,
    val executor: String = "ciToolExecutor",
    val poolOverAct: PoolOverAct = PoolOverAct.REJECT
) {
    enum class PoolOverAct {
        REJECT, BLOCK, RUN, NEW_THREAD
    }
}