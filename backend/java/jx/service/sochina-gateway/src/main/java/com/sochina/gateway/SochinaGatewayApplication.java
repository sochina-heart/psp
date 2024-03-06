package com.sochina.gateway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Indexed;

@SpringBootApplication(scanBasePackages = {"com.sochina.*"})
@EnableDiscoveryClient
@Indexed
@MapperScan(basePackages = {"com.sochina.gateway.mapper"})
public class SochinaGatewayApplication {
    public static void main(String[] args) {
        System.setProperty("csp.sentinel.app.type", "1");
        SpringApplication.run(SochinaGatewayApplication.class, args);
    }
}
