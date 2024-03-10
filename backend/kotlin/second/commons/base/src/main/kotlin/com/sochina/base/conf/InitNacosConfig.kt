// package com.sochina.base.conf
//
// import com.sochina.base.utils.encrypt.gm.sm4.SM4Utils
// import java.io.InputStreamReader
// import java.nio.charset.StandardCharsets
// import java.util.Date
//
// class InitNacosConfig {
//
//     companion object {
//
//         fun get() {
//             val properties = Properties()
//                 val inputStreamReader = InputStreamReader(
//                     this::class.java.classLoader.getResourceAsStream("application.yml")!!, StandardCharsets.UTF_8
//                 )
//                 properties.load(inputStreamReader)
//             val key = System.getProperty("gs4k")
//             decryptConfig(key, "username", "spring.cloud.nacos.username", properties)
//             decryptConfig(key, "password", "spring.cloud.nacos.password", properties)
//         }
//
//         private fun decryptConfig(key: String, name: String, keyName: String, properties: Properties) {
//             val value = properties.getProperty(name)
//             if (value.startsWith("ENC(") && value.endsWith(")")) {
//                 System.setProperty(keyName, SM4Utils.decryptCbc(key, value.substring(4, value.length - 1)))
//             }
//         }
//     }
// }