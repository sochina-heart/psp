package com.sochina.test.preloading.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class TestContextClosedEvent implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        Thread thread = Thread.currentThread();
        System.out.println(this.getClass().getSimpleName() + " current thread is " + thread.getId());
        System.out.println("sochina test has already close");
    }
}
