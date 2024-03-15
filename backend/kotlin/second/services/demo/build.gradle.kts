dependencies {
    implementation(project(":commons:base"))
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.mybatis-flex:mybatis-flex-spring-boot3-starter:${property("mybatis-flex")}")
    implementation("com.mybatis-flex:mybatis-flex-kotlin-extensions:1.0.7")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("com.mysql:mysql-connector-j:8.3.0")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("com.alibaba.fastjson2:fastjson2:2.0.47")
    implementation("jakarta.validation:jakarta.validation-api:3.1.0-M1")
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
}