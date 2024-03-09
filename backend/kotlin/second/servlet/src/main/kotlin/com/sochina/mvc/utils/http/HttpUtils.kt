package com.sochina.mvc.utils.http

import com.sochina.base.constants.Constants
import com.sochina.base.constants.Headers
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.URL
import java.net.URLConnection
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*

object HttpUtils {
    private val LOGGER: Logger = LoggerFactory.getLogger(HttpUtils::class.java)
    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url         发送请求的 URL
     * @param param       请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param contentType 编码类型
     * @return 所代表远程资源的响应结果
     */
    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url 发送请求的 URL
     * @return 所代表远程资源的响应结果
     */
    @JvmOverloads
    fun sendGet(url: String, param: String = "", contentType: String? = Constants.UTF8): String {
        val result = StringBuilder()
        try {
            val urlNameString = if (param.isNotBlank()) "$url?$param" else url
            LOGGER.info("sendGet - {}", urlNameString)
            val conn=URL(urlNameString).openConnection()
            conn.setRequestProperty(Headers.ACCEPT, "*/*")
            conn.setRequestProperty(Headers.CONNECTION, "Keep-Alive")
            conn.setRequestProperty(Headers.USER_AGENT, "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
            conn.connect()
            BufferedReader(InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)).useLines { lines ->
                result.append(lines.joinToString("\n"))
            }
            LOGGER.info("recv - {}", result)
        } catch (e: ConnectException) {
            LOGGER.error("调用HttpUtils.sendGet ConnectException, url=$url,param=$param", e)
        } catch (e: SocketTimeoutException) {
            LOGGER.error("调用HttpUtils.sendGet SocketTimeoutException, url=$url,param=$param", e)
        } catch (e: IOException) {
            LOGGER.error("调用HttpUtils.sendGet IOException, url=$url,param=$param", e)
        } catch (e: Exception) {
            LOGGER.error("调用HttpsUtil.sendGet Exception, url=$url,param=$param", e)
        }
        return result.toString()
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    fun sendPost(url: String, param: String): String {
        val result = StringBuilder()
        try {
            LOGGER.info("sendPost - {}", url)
            val conn = URL(url).openConnection() as HttpsURLConnection
            postConnCommon(conn)
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
            conn.outputStream.use { out ->
                out.write(param.toByteArray(StandardCharsets.UTF_8))
            }
            BufferedReader(InputStreamReader(conn.inputStream, StandardCharsets.UTF_8)).useLines { lines ->
                result.append(lines.joinToString("\n"))
            }
            LOGGER.info("recv - {}", result)
        } catch (e: ConnectException) {
            LOGGER.error("调用HttpUtils.sendPost ConnectException, url=$url,param=$param", e)
        } catch (e: SocketTimeoutException) {
            LOGGER.error("调用HttpUtils.sendPost SocketTimeoutException, url=$url,param=$param", e)
        } catch (e: IOException) {
            LOGGER.error("调用HttpUtils.sendPost IOException, url=$url,param=$param", e)
        } catch (e: Exception) {
            LOGGER.error("调用HttpsUtil.sendPost Exception, url=$url,param=$param", e)
        }
        return result.toString()
    }

    private fun  postConnCommon(conn: URLConnection) {
        conn.setRequestProperty(Headers.ACCEPT, "*/*")
        conn.setRequestProperty(Headers.CONNECTION, "Keep-Alive")
        conn.setRequestProperty(Headers.USER_AGENT, "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
        conn.setRequestProperty(Headers.ACCEPT_CHARSET, "utf-8")
        conn.setRequestProperty(Headers.CONTENT_TYPE, "utf-8")
        conn.doOutput = true
        conn.doInput = true
    }

    fun sendSSLPost(url: String, param: String): String {
        val result = StringBuilder()
        val urlNameString = "$url?$param"
        try {
            LOGGER.info("sendSSLPost - {}", urlNameString)
            val sc = SSLContext.getInstance("SSL")
            sc.init(null, arrayOf<TrustManager>(TrustAnyTrustManager()), SecureRandom())
            val conn =URL(urlNameString).openConnection() as HttpsURLConnection
            postConnCommon(conn)
            conn.sslSocketFactory = sc.socketFactory
            conn.hostnameVerifier = TrustAnyHostnameVerifier()
            BufferedReader(InputStreamReader(conn.inputStream, StandardCharsets.UTF_8)).useLines { lines ->
                result.append(lines.joinToString("\n"))
            }
            LOGGER.info("recv - {}", result)
        } catch (e: ConnectException) {
            LOGGER.error("调用HttpUtils.sendSSLPost ConnectException, url=$url,param=$param", e)
        } catch (e: SocketTimeoutException) {
            LOGGER.error("调用HttpUtils.sendSSLPost SocketTimeoutException, url=$url,param=$param", e)
        } catch (e: IOException) {
            LOGGER.error("调用HttpUtils.sendSSLPost IOException, url=$url,param=$param", e)
        } catch (e: Exception) {
            LOGGER.error("调用HttpsUtil.sendSSLPost Exception, url=$url,param=$param", e)
        }
        return result.toString()
    }

    /**
     * 获取请求方法
     *
     * @param request
     * @return 方法
     */
    fun getMethod(request: HttpServletRequest): String {
        return request.method
    }

    /**
     * 获取所有headerName
     *
     * @param request
     * @return headerName列表
     */
    fun getHeaderNames(request: HttpServletRequest): List<String> {
        val headerNames = request.headerNames
        return Collections.list(headerNames)
    }

    /**
     * 判断指定请求头是否存在
     *
     * @param request
     * @param header
     * @return true/false
     */
    fun headerIsExist(request: HttpServletRequest, header: String?): Boolean {
        var result = false
        val headerNames = getHeaderNames(request)
        if (headerNames.stream().anyMatch { item: String ->
                item.equals(
                    header,
                    ignoreCase = true
                )
            }) {
            result = true
        }
        return result
    }

    /**
     * 获取指定header
     *
     * @param request
     * @param header
     * @return header
     */
    fun getHeader(request: HttpServletRequest, header: String?): String {
        return request.getHeader(header)
    }

    private class TrustAnyTrustManager : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    }

    private class TrustAnyHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            return true
        }
    }
}