package com.sochina.base.utils.thread;

public @interface Pooled {
    boolean async() default true;

    long timeout() default 500;

    String executor() default "ciToolExecutor";

    PoolOverAct poolOverAct() default PoolOverAct.REJECT;

    enum PoolOverAct {
        REJECT, BLOCK, RUN, NEW_THREAD;
    }
}
