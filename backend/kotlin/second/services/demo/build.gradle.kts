dependencies {
    implementation(project(":commons:servlet"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.alibaba:easyexcel:3.3.3") {
        exclude("org.apache.commons", "commons-compress")
    }
    implementation("org.apache.commons:commons-compress:1.26.0")
    implementation("com.mybatis-flex:mybatis-flex-spring-boot3-starter:${property("mybatis-flex")}")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("com.mysql:mysql-connector-j:8.3.0")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}