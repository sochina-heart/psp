package com.sochina.mvc.utils

import cn.hutool.core.convert.Convert
import com.sochina.base.constants.Constants
import com.sochina.base.utils.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*
import java.util.stream.Collectors
import javax.servlet.ServletRequest
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

class ServletUtils {
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(ServletUtils::class.java)

        /**
         * 获取String参数
         */
        fun getParameter(name: String?): String {
            return request.getParameter(name)
        }

        /**
         * 获取String参数
         */
        fun getParameter(name: String?, defaultValue: String?): String {
            return Convert.toStr(request.getParameter(name), defaultValue)
        }

        /**
         * 获取Integer参数
         */
        fun getParameterToInt(name: String?): Int {
            return Convert.toInt(request.getParameter(name))
        }

        /**
         * 获取Integer参数
         */
        fun getParameterToInt(name: String?, defaultValue: Int?): Int {
            return Convert.toInt(request.getParameter(name), defaultValue)
        }

        /**
         * 获取Boolean参数
         */
        fun getParameterToBool(name: String?): Boolean {
            return Convert.toBool(request.getParameter(name))
        }

        /**
         * 获取Boolean参数
         */
        fun getParameterToBool(name: String?, defaultValue: Boolean?): Boolean {
            return Convert.toBool(request.getParameter(name), defaultValue)
        }

        /**
         * 获得所有请求参数
         *
         * @param request 请求对象[ServletRequest]
         * @return Map
         */
        fun getParams(request: ServletRequest): Map<String?, Array<String>?> {
            val map = request.parameterMap
            return Collections.unmodifiableMap(map)
        }

        /**
         * 获得所有请求参数
         *
         * @param request 请求对象[ServletRequest]
         * @return Map
         */
        fun getParamMap(request: ServletRequest): Map<String?, String?> {
            return getParams(request).entries
                .associateBy({ it.key }, {
                    it.value?.fold("") { acc, item ->
                        if (acc.isEmpty()) {
                            item
                        } else {
                            "$acc,$item"
                        }
                    }
                })
        }

        val request: HttpServletRequest
            /**
             * 获取request
             */
            get() = requestAttributes.request
        val response: HttpServletResponse?
            /**
             * 获取response
             */
            get() = requestAttributes.response
        val session: HttpSession
            /**
             * 获取session
             */
            get() = request.session
        val requestAttributes: ServletRequestAttributes
            get() {
                val attributes = RequestContextHolder.getRequestAttributes()
                return attributes as ServletRequestAttributes
            }

        /**
         * 将字符串渲染到客户端
         *
         * @param response 渲染对象
         * @param string   待渲染的字符串
         */
        fun renderString(response: HttpServletResponse, string: String?) {
            try {
                response.status = 200
                response.contentType = "application/json"
                response.characterEncoding = "utf-8"
                response.writer.print(string)
            } catch (e: IOException) {
                LOGGER.error("将字符串渲染到客户端发生异常", e)
            }
        }

        /**
         * 是否是Ajax异步请求
         *
         * @param request
         */
        fun isAjaxRequest(request: HttpServletRequest): Boolean {
            val accept = request.getHeader("accept")
            if (accept != null && accept.contains("application/json")) {
                return true
            }
            val xRequestedWith = request.getHeader("X-Requested-With")
            if (xRequestedWith != null && xRequestedWith.contains("XMLHttpRequest")) {
                return true
            }
            val uri = request.requestURI
            if (StringUtils.inStringIgnoreCase(uri, ".json", ".xml")) {
                return true
            }
            val ajax = request.getParameter("__ajax")
            return StringUtils.inStringIgnoreCase(ajax, "json", "xml")
        }

        /**
         * 内容编码
         *
         * @param str 内容
         * @return 编码后的内容
         */
        fun urlEncode(str: String?): String {
            return try {
                URLEncoder.encode(str, Constants.UTF8)
            } catch (e: UnsupportedEncodingException) {
                ""
            }
        }

        /**
         * 内容解码
         *
         * @param str 内容
         * @return 解码后的内容
         */
        fun urlDecode(str: String?): String {
            return try {
                URLDecoder.decode(str, Constants.UTF8)
            } catch (e: UnsupportedEncodingException) {
                ""
            }
        }

        val headers: Map<String?, Collection<String?>?>
            /**
             * 获取请求头中所有header
             *
             * @return
             */
            get() {
                val request = request
                var headers: Map<String?, Collection<String?>?> = HashMap()
                val headerNames = request.headerNames ?: return headers
                headers = Collections.list(headerNames).stream()
                    .filter { name: String ->
                        !name.equals(
                            HttpHeaders.CONTENT_LENGTH,
                            ignoreCase = true
                        )
                    }
                    .collect(
                        Collectors.toMap(
                            { name: String? -> name },
                            { name: String? ->
                                Collections.list(
                                    request.getHeaders(name)
                                )
                            }
                        ))
                return headers
            }
        val cookies: Map<String, String>
            /**
             * 获取请求头中所有cookie
             *
             * @return
             */
            get() = request.cookies.associateBy({ it.name }, { it.value })
    }
}