dependencies {
    implementation("org.apache.logging.log4j:log4j-core")
    implementation("org.apache.logging.log4j:log4j-to-slf4j")
    api("org.projectlombok:lombok")
    implementation("org.bouncycastle:bcprov-jdk18on")
    implementation("org.bouncycastle:bcpkix-jdk18on")
    api("cn.hutool:hutool-all:5.8.26")
    implementation("commons-io:commons-io:2.15.1")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.3.1")
    implementation("org.apache.httpcomponents.core5:httpcore5:5.2.4")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.springframework.boot:spring-boot-starter")
    api("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")
}

tasks.bootJar {
    enabled = false
    archiveBaseName = "base"
}

tasks.jar {
    archiveBaseName = "base"
}