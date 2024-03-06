package com.sochina.base.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cglib.beans.BeanMap

object MapUtils {
    private val LOGGER: Logger = LoggerFactory.getLogger(MapUtils::class.java)

    /**
     * 对象转换为map
     *
     * @param bean
     * @param <T>
     * @return
    </T> */
    fun <T> beanToMap(bean: T): Map<String, Any?> {
        return BeanMap.create(bean).mapValuesTo(mutableMapOf()) { it.value } as Map<String, Any?>
    }

    /**
     * map转换为对象
     *
     * @param map
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
    </T> */
    fun <T> mapToBean(map: Map<String?, Any?>?, clazz: Class<T>): T? {
        var bean: T? = null
        try {
            bean = clazz.newInstance()
        } catch (e: InstantiationException) {
            LOGGER.error("转换map为bean发生异常", e)
        } catch (e: IllegalAccessException) {
            LOGGER.error("转换map为bean发生异常", e)
        }
        val beanMap = BeanMap.create(bean)
        beanMap.putAll(map!!)
        return bean
    }

    /**
     * list<T>转换为Map<String></String>, Object>
     *
     * @param beanList
     * @param <T>
     * @return
    </T></T> */
    fun <T> beansToMaps(beanList: List<T>): List<Map<String, Any?>> {
        return beanList.map { beanToMap(it) }
    }

    /**
     * List<Map></Map><String></String>, Object>>转换为List<T>
     *
     * @param maps
     * @param clazz
     * @param <T>
     * @return
    </T></T> */
    fun <T> mapsToBeans(maps: List<Map<String?, Any?>?>, clazz: Class<T>): List<T?> {
       return maps.map { mapToBean(it, clazz) }
    }
}
