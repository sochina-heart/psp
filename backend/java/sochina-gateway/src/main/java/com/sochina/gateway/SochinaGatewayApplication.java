package com.sochina.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Indexed;

@SpringBootApplication
@Indexed
public class SochinaGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SochinaGatewayApplication.class, args);
    }

}
