package com.sochina.ksf

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Indexed

// 在kotlin中{}意味着闭包
@Indexed
@SpringBootApplication(scanBasePackages = ["com.sochina"])
class KsFApplication

fun main(args: Array<String>) {
    runApplication<KsFApplication>(*args)
}
