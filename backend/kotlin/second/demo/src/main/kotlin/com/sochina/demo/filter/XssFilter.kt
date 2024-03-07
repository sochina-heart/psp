// package com.sochina.demo.filter
//
// import jakarta.servlet.Filter
// import jakarta.servlet.FilterChain
// import jakarta.servlet.ServletRequest
// import jakarta.servlet.ServletResponse
// import jakarta.servlet.http.HttpServletRequest
// import org.slf4j.Logger
// import org.slf4j.LoggerFactory
// import org.springframework.stereotype.Component
//
// @Component
// class XssFilter : Filter {
//
//     companion object {
//         private val LOGGER: Logger = LoggerFactory.getLogger(XssFilter::class.java)
//     }
//
//     override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
//         val httpServletRequest: HttpServletRequest? = request as? HttpServletRequest
//         val requestURI = httpServletRequest?.requestURI
//         if (requestURI.equals("/k/test/demo")) {
//             LOGGER.info("this request url is $requestURI")
//         }
//         chain?.doFilter(httpServletRequest, response)
//     }
// }