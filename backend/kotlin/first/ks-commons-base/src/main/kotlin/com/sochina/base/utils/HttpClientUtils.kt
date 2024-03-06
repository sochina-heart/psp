package com.sochina.base.utils

import com.sochina.base.properties.httpClient.HttpClientRequestProperties
import com.sochina.base.properties.httpClient.HttpClientUploadProperties
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.classic.methods.HttpPost
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.ClassicHttpResponse
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.apache.hc.core5.http.io.entity.StringEntity
import org.apache.hc.core5.http.message.BasicNameValuePair
import org.apache.hc.core5.net.URIBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.TimeUnit


class HttpClientUtils(requestProperties: HttpClientRequestProperties, uploadProperties: HttpClientUploadProperties) {

    private val LOGGER: Logger = LoggerFactory.getLogger(HttpClientUtils::class.java)

    private var REQUEST_CONFIG: RequestConfig = RequestConfig.custom() // 客户端和服务端建立连接超时时间
        .setConnectionRequestTimeout(requestProperties.connectRequestTimeout, TimeUnit.SECONDS) // 客户端从服务器读取数据的超时时间
        .setResponseTimeout(uploadProperties.responseTimeout, TimeUnit.SECONDS)
        .build()

    private var UPLOAD_CONFIG = RequestConfig.custom() // 客户端和服务端建立连接超时时间
        .setConnectionRequestTimeout(uploadProperties.connectRequestTimeout, TimeUnit.SECONDS) // 客户端从服务器读取数据的超时时间
        .setResponseTimeout(uploadProperties.responseTimeout, TimeUnit.SECONDS)
        .build()

    /**
     * 发送get请求，接收json响应数据
     *
     * @param url     访问地址，无query参数
     * @param params  query参数
     * @param headers 请求头
     * @return
     */
    fun doGet(url: String?, params: Map<String?, Any?>, headers: Map<String?, Collection<String>?>): Any {
        val httpClient: CloseableHttpClient = HttpClients.createDefault()
        var response: CloseableHttpResponse? = null
        var resultStr = ""
        try {
            val builder: URIBuilder = URIBuilder(url)
            handleParamsOfGet(params, builder)
            val uri: URI = builder.build()
            LOGGER.debug("http 请求地址：{}", url)
            if (params.isNotEmpty()) {
                LOGGER.debug("http 请求参数：{}", params)
            }
            // 创建http get请求
            val httpGet = HttpGet(uri)
            handleHeadersOfGet(headers, httpGet)
            httpGet.config = REQUEST_CONFIG
            // 执行请求
            response = httpClient.execute(httpGet) { res ->
                resultStr = responseHandle(res)
                res
            } as CloseableHttpResponse?
        } catch (e: URISyntaxException) {
            LOGGER.error("http 发送请求异常 url：{}", url, e)
            throw Exception(e)
        } catch (e: IOException) {
            LOGGER.error("http 发送请求异常 url：{}", url, e)
            throw IOException(e)
        } finally {
            streamClose(response, httpClient)
        }
        return resultStr
    }

    /**
     * 发送post请求，上传byte
     *
     * @param url       访问地址，无query参数
     * @param queryName 参数名
     * @param bytes
     * @param fileName  文件名
     * @param headers   请求头
     * @return
     */
    fun doPostBinaryBody(
        url: String?,
        queryName: String?,
        bytes: ByteArray?,
        fileName: String?,
        headers: Map<String?, Collection<String>?>
    ): Any {
        val httpClient: CloseableHttpClient = HttpClients.createDefault()
        var response: CloseableHttpResponse? = null
        var resultStr = ""
        try {
            LOGGER.debug("http 请求地址：{}", url)
            val httpPost = HttpPost(url)
            handleHeadersOfPost(headers, httpPost)
            httpPost.config = UPLOAD_CONFIG
            val multipartEntityBuilder: MultipartEntityBuilder = MultipartEntityBuilder.create()
            multipartEntityBuilder.addBinaryBody(queryName, bytes, ContentType.MULTIPART_FORM_DATA, fileName)
            httpPost.entity = multipartEntityBuilder.build()
            response = httpClient.execute(httpPost) {res ->
                resultStr = responseHandle(res)
                res
            } as CloseableHttpResponse
        } catch (e: IOException) {
            LOGGER.error("http 发送请求异常 url：{}", url, e)
            throw IOException(e)
        } finally {
            streamClose(response, httpClient)
        }
        return resultStr
    }

    /**
     * 发送post请求，form-data数据传输
     *
     * @param url      访问地址，无query参数
     * @param formData query参数
     * @param headers  请求头
     * @return
     */
    fun doPostFormData(
        url: String?,
        formData: Map<String?, Any>,
        headers: Map<String?, Collection<String>?>
    ): Any {
        val httpClient: CloseableHttpClient = HttpClients.createDefault()
        var response: CloseableHttpResponse? = null
        var resultStr = ""
        try {
            LOGGER.debug("http 请求地址：{}", url)
            val httpPost: HttpPost = HttpPost(url)
            handleHeadersOfPost(headers, httpPost)
            httpPost.config = REQUEST_CONFIG
            val multipartEntityBuilder: MultipartEntityBuilder = MultipartEntityBuilder.create()
            formData.keys.stream()
                .filter { obj: String? -> Objects.nonNull(obj) }
                .forEach { key: String? ->
                    multipartEntityBuilder.addTextBody(
                        key,
                        formData[key].toString(),
                        ContentType.MULTIPART_FORM_DATA
                    )
                }
            httpPost.entity = multipartEntityBuilder.build()
            response = httpClient.execute(httpPost) {res ->
                resultStr = responseHandle(res)
                res
            } as CloseableHttpResponse
        } catch (e: IOException) {
            LOGGER.error("http 发送请求异常 url：{}", url, e)
            throw IOException(e)
        } finally {
            streamClose(response, httpClient)
        }
        return resultStr
    }

    /**
     * 发送post请求，接收json响应数据
     * post application/x-www-form-urlencoded
     *
     * @param url     访问地址，无query参数
     * @param params  query参数
     * @param headers 请求头
     * @return
     */
    fun doPost(url: String?, params: Map<String?, Any>?, headers: Map<String?, Collection<String>?>): Any {
        val httpClient: CloseableHttpClient = HttpClients.createDefault()
        var response: CloseableHttpResponse? = null
        var resultStr: String =""
        try {
            LOGGER.debug("http 请求地址：{}", url)
            val httpPost = HttpPost(url)
            handleHeadersOfPost(headers, httpPost)
            httpPost.config = REQUEST_CONFIG
            if (params != null) {
                val paramList = params.entries.map { BasicNameValuePair(it.key, it.value.toString()) }.toList()
                httpPost.entity = UrlEncodedFormEntity(paramList)
            }
            response = httpClient.execute(httpPost) { res ->
                resultStr = responseHandle(res)
                res
            } as CloseableHttpResponse
        } catch (e: IOException) {
            LOGGER.error("http 发送请求异常 url：{}", url, e)
            throw IOException(e)
        } finally {
            streamClose(response, httpClient)
        }
        return resultStr
    }

    /**
     * 发送post请求，接收json响应数据
     * post application/json
     *
     * @param url     访问路径，无query参数
     * @param json    query参数
     * @param headers 请求头
     * @return
     */
    fun doPostJson(url: String?, json: String?, headers: Map<String?, Collection<String>?>): Any {
        val httpClient: CloseableHttpClient = HttpClients.createDefault()
        var response: CloseableHttpResponse? = null
        var resultStr = ""
        try {
            LOGGER.debug("http 请求路径：{}", url)
            val httpPost: HttpPost = HttpPost(url)
            handleHeadersOfPost(headers, httpPost)
            httpPost.config = REQUEST_CONFIG
            if (json != null && json.trim() != "") {
                val entity = StringEntity(json, ContentType.APPLICATION_JSON)
                httpPost.entity = entity
            }
            response = httpClient.execute(httpPost) {res ->
                resultStr = responseHandle(res)
                res
            } as CloseableHttpResponse
        } catch (e: IOException) {
            LOGGER.error("http 发送请求异常 url：{}", url, e)
            throw IOException(e)
        } finally {
            streamClose(response, httpClient)
        }
        return resultStr
    }

    /**
     * http流关闭
     *
     * @param response
     * @param httpClient
     */
    private fun streamClose(response: CloseableHttpResponse?, httpClient: CloseableHttpClient) {
        try {
            response?.close()
            httpClient.close()
        } catch (e: IOException) {
            LOGGER.error("http 关闭流异常", e)
        }
    }

    /**
     * http响应处理
     *
     * @param response
     * @return
     */
    private fun responseHandle(response: ClassicHttpResponse): String {
        var resultStr = ""
        if (response.code == 200) {
            try {
                resultStr = EntityUtils.toString(response.entity, StandardCharsets.UTF_8)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
            LOGGER.debug("http 响应内容：{}", resultStr)
        } else {
            LOGGER.error("http 响应状态码：{}", response.code)
            throw IOException("http 请求失败 状态码：${response.code}")
        }
        return resultStr
    }

    /**
     * 处理http get请求headers
     *
     * @param headers 请求头
     * @param httpGet httpGet
     */
    private fun handleHeadersOfGet(headers: Map<String?, Collection<String>?>, httpGet: HttpGet) {
        headers.entries.forEach {
            httpGet.setHeader(it.key, it.value)
        }
    }

    /**
     * 处理http post请求headers
     *
     * @param headers  请求头
     * @param httpPost httpPost
     */
    private fun handleHeadersOfPost(headers: Map<String?, Collection<String>?>, httpPost: HttpPost) {
        headers.entries.forEach {
            httpPost.setHeader(it.key, it.value)
        }
    }

    /**
     * 处理http get请求参数
     *
     * @param params  参数
     * @param builder
     */
    private fun handleParamsOfGet(params: Map<String?, Any?>, builder: URIBuilder) {
        params.entries.forEach {
            builder.addParameter(it.key, it.value.toString())
        }
    }
}