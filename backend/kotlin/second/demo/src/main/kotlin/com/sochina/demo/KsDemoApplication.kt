package com.sochina.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Indexed

// 在kotlin中{}意味着闭包
@SpringBootApplication(scanBasePackages = ["com.sochina"])
@Indexed
class KsDemoApplication

fun main(args: Array<String>) {
    runApplication<KsDemoApplication>(*args)
}
