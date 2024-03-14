package com.sochina.demo

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Indexed
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.web.reactive.config.EnableWebFlux


// 在kotlin中{}意味着闭包
@SpringBootApplication(scanBasePackages = ["com.sochina"])
@EnableTransactionManagement
@Indexed
@MapperScan("com.sochina")
@EnableEncryptableProperties
@EnableWebFlux
class KsDemoApplication

fun main(args: Array<String>) {
    runApplication<KsDemoApplication>(*args)
}
