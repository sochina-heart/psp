package com.sochina.test.preloading.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class TestApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Thread thread = Thread.currentThread();
        System.out.println(this.getClass().getSimpleName() + " current thread is " + thread.getId());
        System.out.println("sochina test has already refresh");
    }
}
