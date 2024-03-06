package com.sochina.test;

import com.sochina.base.utils.thread.Pooled;
import com.sochina.base.utils.thread.TaskToolExecutor;
import com.sochina.base.utils.thread.Worker;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

@SpringBootTest(classes = SochinaTestApplicaiont.class)
@RunWith(SpringRunner.class)
public class Test2 extends TestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(Test2.class);

    @Autowired
    private Map<String, TaskToolExecutor> executorMap;

    @Test
    public void testDispatchTask() {
        TaskToolExecutor executor = executorMap.get("ciToolExecutor");
        final CountDownLatch countDownLatch = new CountDownLatch(3);
        for (int i = 0; i < 3; i++) {
            Worker<Object> worker = createTestWorker(i, countDownLatch);
            executor.execute(worker);
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("所有线程执行完毕");
    }

    /**
     * @param i
     * @param countDownLatch
     * @return
     */
    private Worker<Object> createTestWorker(int i, CountDownLatch countDownLatch) {
        return new Worker<>(new Thread(() -> {
            LOGGER.info("我是任务 - " + i + "---------------" + Thread.currentThread());
            countDownLatch.countDown();
        }), Pooled.PoolOverAct.NEW_THREAD);
    }

    /**
     * 测试有返回结果线程
     */
    @Test
    public void testHasResultTask() {
        TaskToolExecutor executor = executorMap.get("ciToolExecutor");
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Worker<Object> worker = createHasResultTestWorker(countDownLatch);
        Object result = executor.submit(worker);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("线程执行返回结果为: " + result);
    }

    private Worker<Object> createHasResultTestWorker(CountDownLatch countDownLatch) {
        Worker<Object> worker = new Worker<>();
        Runnable runnable = () -> {
            int count = 1;
            // 设置返回值
            worker.setThreadResult(count);
            countDownLatch.countDown();
        };
        worker.setTimeout(500);
        worker.setCommand(runnable);
        return worker;
    }

    @Test
    public void notifyEmail() {
        LOGGER.info("我是通知线程");
    }

    @Test
    public void ceshi() {
        for (int i = 7; i < 7; i++) {
            notifyEmail();
        }
    }
}
