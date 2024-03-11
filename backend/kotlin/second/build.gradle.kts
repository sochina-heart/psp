import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

extra["log4j"] = "2.23.0"
extra["bouncy"] = "1.77"
extra["jedis"] = "5.1.1"
extra["mybatis-flex"] = "1.8.2"
extra["springCloud"] = "2023.0.0"
extra["springCloudAlibaba"] = "2022.0.0.0"

plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
}

group = "com.sochina"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

tasks.bootJar{
    enabled = false
}

allprojects {
    tasks.withType<JavaCompile> {
        options.encoding = "utf-8"
    }

    tasks.withType<JavaExec> {
        systemProperty("file.encoding", "utf-8")
    }
}

subprojects {
    if (project.name != "commons" && project.name != "services") {
        apply{
            plugin("org.springframework.boot")
            plugin("io.spring.dependency-management")
            plugin("org.jetbrains.kotlin.jvm")
            plugin("org.jetbrains.kotlin.plugin.spring")
        }

        dependencyManagement {
            dependencies {
                dependency("org.apache.logging.log4j:log4j-core:${property("log4j")}")
                dependency("org.apache.logging.log4j:log4j-to-slf4j:${property("log4j")}")
                dependency("org.bouncycastle:bcprov-jdk18on:${property("bouncy")}")
                dependency("org.bouncycastle:bcpkix-jdk18on:${property("bouncy")}")
                dependency("redis.clients:jedis:${property("jedis")}")
            }
            imports {
                mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloud")}")
                mavenBom( "com.alibaba.cloud:spring-cloud-alibaba-dependencies:${property("springCloudAlibaba")}")
            }
        }

        dependencies {
            implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
            implementation("org.jetbrains.kotlin:kotlin-reflect")
        }

        tasks.withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs += "-Xjsr305=strict"
                jvmTarget = "21"
            }
        }

        tasks.withType<Test> {
            useJUnitPlatform()
        }

        tasks.bootJar {
            archiveAppendix = "0.0.1-SNAPSHOT"
        }
        tasks.jar {
            archiveAppendix = "0.0.1-SNAPSHOT"
        }
    }
}