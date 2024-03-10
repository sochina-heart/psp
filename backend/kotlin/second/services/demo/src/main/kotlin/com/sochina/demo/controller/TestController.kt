package com.sochina.demo.controller

import com.sochina.base.properties.CommonsProperties
import com.sochina.base.properties.httpClient.HttpClientRequestProperties
import com.sochina.base.properties.httpClient.HttpClientUploadProperties
import com.sochina.base.utils.HttpClientUtils
import com.sochina.base.utils.character.XssUtils
import com.sochina.base.utils.verification.code.impl.ChineseArithmeticVerificationCodeTool
import com.sochina.base.utils.web.AjaxResult
import com.sochina.mvc.utils.ServletUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.io.File
import java.util.Date
import javax.imageio.ImageIO

/**
 * @author sochina-heart
 */
@RestController
@RequestMapping("/test")

// 构造方法注入
// @Component
class TestController(
    private val commonsProperties: CommonsProperties,
    private val chineseArithmeticVerificationCodeTool: ChineseArithmeticVerificationCodeTool
) {

// class TestController {

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(TestController::class.java)
    }

    // 次级构造函数
    /* private constructor(
        commonsProperties: CommonsProperties,
        userServiceImpl: IUserServiceImpl
    ) : this() */

    // 字段注入
    // @Autowired
    // lateinit var commonsProperties: CommonsProperties

    // setter方法注入
    // private lateinit var commonsProperties: CommonsProperties

    // @Autowired
    // fun setCommonsProperties(commonsProperties: CommonsProperties) {
    //     this.commonsProperties = commonsProperties
    // }

    @PostMapping("/code/demo")
    fun createCodeImage(): AjaxResult {
        val verificationCode = chineseArithmeticVerificationCodeTool.createVerificationCodeImage(200, 150)
        ImageIO.write(verificationCode.image, "png", File("D:\\Users\\sochina\\Downloads\\1111\\1.png"))
        return AjaxResult.success()
    }

    @GetMapping("/cookie/demo")
    fun cookieDemo(
        a: String,
        b: String,
        c: String
    ): String {
        val httpClientUtils = HttpClientUtils(HttpClientRequestProperties(), HttpClientUploadProperties())
        val doGet = httpClientUtils.doGet(
            "http://localhost:8888/k/test/cookie/demo2",
            ServletUtils.getParamMap(ServletUtils.request),
            emptyMap()
        )
        println(doGet)
        return "hello world"
    }

    @GetMapping("/cookie/demo2")
    fun cookieDemo2(
        a: String,
        b: String,
        c: String
    ): AjaxResult {
        LOGGER.info("$a $b $c")
        return AjaxResult.success("hello world", "fdf")
    }


    @RequestMapping("/demo")
    fun demo(str: String) {
        // println("this is first controller about kotlin and springboot")
        LOGGER.info("this is first controller about kotlin and springboot")
        val xssUtils = XssUtils.getInstance(commonsProperties)
        val doCheck = xssUtils.doCheck(str)
        println(doCheck)
        println(xssUtils.recover(doCheck))
        println(xssUtils.clean(str))
    }

    @RequestMapping("/xss")
    fun xss() {
        LOGGER.info(commonsProperties.xssProperties.pattern.toString())
        LOGGER.info(commonsProperties.xssProperties.sensitiveData)
    }

    @RequestMapping("/responseResult")
    fun responseResult(): AjaxResult {
        return AjaxResult()
    }
}