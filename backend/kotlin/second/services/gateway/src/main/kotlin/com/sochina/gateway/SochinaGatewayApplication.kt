package com.sochina.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Indexed

@SpringBootApplication(scanBasePackages = ["com.sochina"])
@Indexed
class SochinaGatewayApplication

fun main(args: Array<String>) {
    runApplication<SochinaGatewayApplication>(*args)
}