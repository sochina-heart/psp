package com.sochina.demo

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Indexed
import org.springframework.transaction.annotation.EnableTransactionManagement


// 在kotlin中{}意味着闭包
@SpringBootApplication(scanBasePackages = ["com.sochina"])
@EnableTransactionManagement
@Indexed
@MapperScan("com.sochina")
class KsDemoApplication

fun main(args: Array<String>) {
    runApplication<KsDemoApplication>(*args)
}
