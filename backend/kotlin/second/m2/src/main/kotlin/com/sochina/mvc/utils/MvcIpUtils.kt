package com.sochina.mvc.utils

import com.sochina.base.constants.Constants
import com.sochina.base.utils.StringUtils
import com.sochina.base.utils.StringUtils.Companion.isEmpty
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.regex.Pattern

object MvcIpUtils {
    private val LOGGER: Logger = LoggerFactory.getLogger(MvcIpUtils::class.java)
    private const val REGX_0_255 = "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)"

    // 匹配 ip
    private const val REGX_IP = "(($REGX_0_255\\.){3}$REGX_0_255)"

    // 匹配网段
    private const val REGX_IP_SEG = "($REGX_IP\\-$REGX_IP)"
    private const val REGX_IP_WILDCARD =
        "(((\\*\\.){3}\\*)|($REGX_0_255(\\.\\*){3})|($REGX_0_255\\.$REGX_0_255)(\\.\\*){2}|(($REGX_0_255\\.){3}\\*))"
    private val IP_PATTERN: Pattern =
        Pattern.compile("((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}")
    val ipAddr: String
        /**
         * 获取客户端IP
         *
         * @return IP地址
         */
        get() = getIpAddr(ServletUtils.request)

    /**
     * 获取客户端IP
     *
     * @param request 请求对象
     * @return IP地址
     */
    fun getIpAddr(request: HttpServletRequest?): String {
        val ip = request?.let { req ->
            listOf(
                "x-forwarded-for",
                "Proxy-Client-IP",
                "X-Forwarded-For",
                "WL-Proxy-Client-IP",
                "X-Real-IP"
            ).firstNotNullOfOrNull { headerName ->
                req.getHeader(headerName).takeIf { it.isNotBlank() && it.lowercase() != "unknown" }
            } ?: req.remoteAddr
        }?: "unknown"
        return when {
            ip.isNotBlank() -> if (ip.contains(Constants.COMMA)) ip.split(",", limit = 2)[0] else ip
            ip == "0:0:0:0:0:0:0:1" -> "127.0.0.1"
            else -> getMultistageReverseProxyIp(ip)
        }
    }

    /**
     * 检查是否为内部IP地址
     *
     * @param ip IP地址
     * @return 结果
     */
    fun internalIp(ip: String): Boolean {
        val addr = textToNumericFormatV4(ip)
        return internalIp(addr) || "127.0.0.1" == ip
    }

    /**
     * 检查是否为内部IP地址
     *
     * @param addr byte地址
     * @return 结果
     */
    private fun internalIp(addr: ByteArray?): Boolean {
        return addr?.let { array ->
            if (array.size < 2) return true
            val b0 = array[0]
            val b1 = array[1]
            // 定义私有IP范围
            val privateRanges = listOf(
                Pair(0x0A.toByte(), null), // 10.x.x.x/8
                Pair(0xAC.toByte(), 0x1F.toByte()), // 172.16.x.x/12
                Pair(0xC0.toByte(), 0xA8.toByte()) // 192.168.x.x/16
            )
            val SECTION_3 = 0x10.toByte()
            privateRanges.any { (section0, section1) ->
                when {
                    section0 == b0 -> section1 == null || section1 == b1
                    section0 == b0 && section1 !=null -> section1 <= b1 && b1 >= SECTION_3.toByte() // 添加此行以考虑172.16.x.x范围的起始字节
                    else -> false
                }
            }
        } ?: true
    }

    /**
     * 将IPv4地址转换成字节
     *
     * @param text IPv4地址
     * @return byte 字节
     */
    fun textToNumericFormatV4(text: String): ByteArray? {
        if (text.isEmpty()) {
            return null
        }
        val bytes = ByteArray(4)
        val elements = text.split("\\.".toRegex()).toTypedArray()
        try {
            var l: Long
            var i: Int
            when (elements.size) {
                1 -> {
                    l = elements[0].toLong()
                    if ((l < 0L) || (l > 4294967295L)) {
                        return null
                    }
                    bytes[0] = (l shr 24 and 0xFFL).toInt().toByte()
                    bytes[1] = ((l and 0xFFFFFFL) shr 16 and 0xFFL).toInt().toByte()
                    bytes[2] = ((l and 0xFFFFL) shr 8 and 0xFFL).toInt().toByte()
                    bytes[3] = (l and 0xFFL).toInt().toByte()
                }

                2 -> {
                    l = elements[0].toInt().toLong()
                    if ((l < 0L) || (l > 255L)) {
                        return null
                    }
                    bytes[0] = (l and 0xFFL).toInt().toByte()
                    l = elements[1].toInt().toLong()
                    if ((l < 0L) || (l > 16777215L)) {
                        return null
                    }
                    bytes[1] = (l shr 16 and 0xFFL).toInt().toByte()
                    bytes[2] = ((l and 0xFFFFL) shr 8 and 0xFFL).toInt().toByte()
                    bytes[3] = (l and 0xFFL).toInt().toByte()
                }

                3 -> {
                    i = 0
                    while (i < 2) {
                        l = elements[i].toInt().toLong()
                        if ((l < 0L) || (l > 255L)) {
                            return null
                        }
                        bytes[i] = (l and 0xFFL).toInt().toByte()
                        ++i
                    }
                    l = elements[2].toInt().toLong()
                    if ((l < 0L) || (l > 65535L)) {
                        return null
                    }
                    bytes[2] = (l shr 8 and 0xFFL).toInt().toByte()
                    bytes[3] = (l and 0xFFL).toInt().toByte()
                }

                4 -> {
                    i = 0
                    while (i < 4) {
                        l = elements[i].toInt().toLong()
                        if ((l < 0L) || (l > 255L)) {
                            return null
                        }
                        bytes[i] = (l and 0xFFL).toInt().toByte()
                        ++i
                    }
                }

                else -> return null
            }
        } catch (e: NumberFormatException) {
            LOGGER.error("类型转换发生异常", e)
            return null
        }
        return bytes
    }

    val hostIp: String
        /**
         * 获取IP地址
         *
         * @return 本地IP地址
         */
        get() {
            try {
                return InetAddress.getLocalHost().hostAddress
            } catch (e: UnknownHostException) {
                LOGGER.error("host解析异常", e)
            }
            return "127.0.0.1"
        }
    val hostName: String
        /**
         * 获取主机名
         *
         * @return 本地主机名
         */
        get() {
            try {
                return InetAddress.getLocalHost().hostName
            } catch (e: UnknownHostException) {
                LOGGER.error("host解析异常", e)
            }
            return "未知"
        }

    /**
     * 从多级反向代理中获得第一个非unknown IP地址
     *
     * @param ip 获得的IP地址
     * @return 第一个非unknown IP地址
     */
    fun getMultistageReverseProxyIp(str: String?): String {
        // 多级反向代理检测
        var ip = str
        if (ip != null && ip.indexOf(",") > 0) {
            val ips = ip.trim { it <= ' ' }.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            for (subIp in ips) {
                if (!isUnknown(subIp)) {
                    ip = subIp
                    break
                }
            }
        }
        return StringUtils.substring(ip, 0, 255)
    }

    /**
     * 检测给定字符串是否为未知，多用于检测HTTP请求相关
     *
     * @param checkString 被检测的字符串
     * @return 是否未知
     */
    fun isUnknown(checkString: String?): Boolean {
        return checkString.isNullOrBlank() || "unknown".equals(checkString, ignoreCase = true)
    }

    /**
     * 是否为IP
     */
    fun isIP(ip: String): Boolean {
        return ip.isNotBlank() && ip.matches(REGX_IP.toRegex())
    }

    /**
     * 是否为IP，或 *为间隔的通配符地址
     */
    fun isIpWildCard(ip: String): Boolean {
        return ip.isNotBlank() && ip.matches(REGX_IP_WILDCARD.toRegex())
    }

    /**
     * 检测参数是否在ip通配符里
     */
    fun ipIsInWildCardNoCheck(ipWildCard: String, ip: String): Boolean {
        val s1 = ipWildCard.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val s2 = ip.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var isMatchedSeg = true
        var i = 0
        while (i < s1.size && s1[i] != "*") {
            if (s1[i] != s2[i]) {
                isMatchedSeg = false
                break
            }
            i++
        }
        return isMatchedSeg
    }

    /**
     * 是否为特定格式如:“10.10.10.1-10.10.10.99”的ip段字符串
     */
    fun isIPSegment(ipSeg: String): Boolean {
        return ipSeg.isNotBlank() && ipSeg.matches(REGX_IP_SEG.toRegex())
    }

    /**
     * 判断ip是否在指定网段中
     */
    fun ipIsInNetNoCheck(iparea: String, ip: String): Boolean {
        val range = iparea.split("-").filter { it.isNotBlank() }.mapIndexed { index, part ->
            part.split("\\.".toRegex()).map { it.toIntOrNull() ?: return false }.toIntArray()
                .let { if (index == 0) it.copyOf(4) else it.copyOfRange(1, 4) }
                .foldIndexed(0L) { i, acc, octet -> acc shl 8 or octet.toLong() }
        }

        if (range.size != 2) {
            return false
        }

        val ipt = ip.split("\\.".toRegex()).map { it.toIntOrNull() ?: return false }.toIntArray()
            .foldIndexed(0L) { i, acc, octet -> acc shl 8 or octet.toLong() }

        return ipt in range.first()..range.last()
    }

    /**
     * 校验ip是否符合过滤串规则
     *
     * @param filter 过滤IP列表,支持后缀'*'通配,支持网段如:`10.10.10.1-10.10.10.99`
     * @param ip     校验IP地址
     * @return boolean 结果
     */
    fun isMatchedIp(filter: String, ip: String): Boolean {
        if (isEmpty(filter) || isEmpty(ip)) {
            return false
        }
        val ips = filter.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return ips.any {
            when {
                isIP(it) -> it == ip
                isIpWildCard(it) -> ipIsInWildCardNoCheck(it, ip)
                isIPSegment(it) -> ipIsInNetNoCheck(it, ip)
                else -> false
            }
        }
    }

    /**
     * 验证IP字符串是否合规
     *
     * @param ip ip字符串
     * @return true标识合规
     */
    fun validIp(ip: String?): Boolean {
        val m = IP_PATTERN.matcher(ip)
        return m.matches()
    }
}