package com.sochina.test.preloading.listener;

import com.sochina.mvc.utils.ServletUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.RequestHandledEvent;

import javax.servlet.http.HttpServletRequest;

@Component
public class TestRequestHandledEvent implements ApplicationListener<RequestHandledEvent> {
    @Override
    public void onApplicationEvent(RequestHandledEvent event) {
        Thread thread = Thread.currentThread();
        System.out.println(this.getClass().getSimpleName() + " current thread is " + thread.getId());
        HttpServletRequest request = ServletUtils.getRequest();
        System.out.println("sochina test " + request.getRequestURI());
    }
}
