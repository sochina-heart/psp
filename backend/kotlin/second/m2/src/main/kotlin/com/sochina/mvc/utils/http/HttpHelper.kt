package com.sochina.mvc.utils.http

import jakarta.servlet.ServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

object HttpHelper {
    private val LOGGER: Logger = LoggerFactory.getLogger(HttpHelper::class.java)
    fun getBodyString(request: ServletRequest): String {
        val sb = StringBuilder()
        var reader: BufferedReader? = null
        try {
            request.inputStream.use { inputStream ->
                reader = BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8))
                var line: String? = ""
                while ((reader!!.readLine().also { line = it }) != null) {
                    sb.append(line)
                }
            }
        } catch (e: IOException) {
            LOGGER.warn("getBodyString出现问题！{}", e.message)
            throw IOException(e.message, e)
        } finally {
            if (reader != null) {
                try {
                    reader!!.close()
                } catch (e: IOException) {
                    LOGGER.error(e.message)
                    throw IOException(e.message, e)
                }
            }
        }
        return sb.toString()
    }
}