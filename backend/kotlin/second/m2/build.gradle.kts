dependencies {
    api(project(":base"))
    implementation("org.springframework.boot:spring-boot-starter-aop")
    api("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-web")
    api("redis.clients:jedis")
}

tasks.bootJar {
    enabled = false
    archiveBaseName = "mvc"
}

tasks.jar {
    archiveBaseName = "mvc"
}