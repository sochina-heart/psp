package com.sochina.freemarker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.sochina.*"})
public class SochinaFreemarkerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SochinaFreemarkerApplication.class, args);
    }
}
