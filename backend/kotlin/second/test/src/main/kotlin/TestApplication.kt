package com.sochina

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Indexed

@SpringBootApplication(scanBasePackages = ["com.sochina"])
@EnableTransactionManagement
@Indexed
class TestApplication

fun main(args: Array<String>) {
    runApplication<TestApplication>(*args)
}