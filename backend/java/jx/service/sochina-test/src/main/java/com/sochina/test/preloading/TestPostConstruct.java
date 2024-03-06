package com.sochina.test.preloading;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TestPostConstruct {

    @PostConstruct
    public void demoPostConstruct() {
        System.out.println("sochina test demoPostConstruct");
    }
}
