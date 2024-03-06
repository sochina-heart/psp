package com.sochina.test.preloading.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

@Component
public class TestContextStoppedEvent implements ApplicationListener<ContextStoppedEvent> {
    @Override
    public void onApplicationEvent(ContextStoppedEvent event) {
        Thread thread = Thread.currentThread();
        System.out.println(this.getClass().getSimpleName() + " current thread is " + thread.getId());
        System.out.println("sochina test has already stop");
    }
}
