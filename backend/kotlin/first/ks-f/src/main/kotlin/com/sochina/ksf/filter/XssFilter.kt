package com.sochina.ksf.filter

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Component
class XssFilter : Filter {

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(XssFilter::class.java)
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpServletRequest: HttpServletRequest? = request as? HttpServletRequest
        val requestURI = httpServletRequest?.requestURI
        if (requestURI.equals("/k/test/demo")) {
            LOGGER.info("this request url is $requestURI")
        }
        chain?.doFilter(httpServletRequest, response)
    }
}