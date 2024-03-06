package com.sochina.test.preloading;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestInitBean {

    @Bean(initMethod = "init")
    // hello: ENC(00c2363dc7055cf0d74d5594bf49b2e1)
    public void demoInitBean() {
        System.out.println("postConstruct init bean");
    }
}
