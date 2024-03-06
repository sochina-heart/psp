package com.sochina.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Indexed;

@SpringBootApplication(scanBasePackages = {"com.sochina.*"})
@Indexed
public class SochinaTestApplicaiont {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SochinaTestApplicaiont.class, args);
        // run.start();
        // run.stop();
        // run.close();
    }
}
