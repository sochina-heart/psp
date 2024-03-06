package com.sochina.test.preloading.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

@Component
public class TestContextStartedEvent implements ApplicationListener<ContextStartedEvent> {
    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        Thread thread = Thread.currentThread();
        System.out.println(this.getClass().getSimpleName() + " current thread is " + thread.getId());
        System.out.println("sochina test has already start");
    }
}
