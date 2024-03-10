package com.sochina.demo.config

import com.mybatisflex.core.keygen.KeyGeneratorFactory
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration

@Configuration
class StartupRunner {
    @PostConstruct
    fun registerKeyGenerator() {
        KeyGeneratorFactory.register("sUuid", UUIDKeyGenerator())
    }
}