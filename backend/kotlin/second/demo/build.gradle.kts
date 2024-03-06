dependencies {
    implementation(project(":m2"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.baomidou:mybatis-plus-boot-starter:3.5.5") {
        exclude("org.mybatis", "mybatis-spring")
    }
    implementation("org.mybatis:mybatis-spring:3.0.3")
    implementation("com.mysql:mysql-connector-j:8.3.0")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}