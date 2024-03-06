package com.sochina.oauth.Authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.sochina.*"})
public class SochinaOauthAuthorizationApplication {
    public static void main(String[] args) {
        SpringApplication.run(SochinaOauthAuthorizationApplication.class, args);
    }
}
