dependencies {
    api(project(":commons:base"))
    implementation("org.springframework.boot:spring-boot-starter-aop")
    api("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-web")
    api("redis.clients:jedis")
    api("jakarta.validation:jakarta.validation-api:3.1.0-M1")
    api("org.hibernate.validator:hibernate-validator:8.0.1.Final")
}

tasks.bootJar {
    enabled = false
}