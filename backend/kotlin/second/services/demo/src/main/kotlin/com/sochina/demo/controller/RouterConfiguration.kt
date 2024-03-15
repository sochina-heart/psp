package com.sochina.demo.controller

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class RouterConfiguration(
    private val sochinaUserHandler: SochinaUserHandler) {

    @Bean
    fun userRoutes(): RouterFunction<ServerResponse> {
        return RouterFunctions.route()
            .path("/user") { userRouter ->
                userRouter
                    .POST("/list", sochinaUserHandler::listUser)
                    .POST("/add", sochinaUserHandler::addUser)
                    .POST("/remove", sochinaUserHandler::removeUser)
                    .GET("/get/{id}", sochinaUserHandler::getUser)
            }
            .build()
    }
}