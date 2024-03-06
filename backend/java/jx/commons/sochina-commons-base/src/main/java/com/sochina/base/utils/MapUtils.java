package com.sochina.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanMap;

import java.util.*;
import java.util.stream.Collectors;

public class MapUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapUtils.class);

    /**
     * 对象转换为map
     *
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        BeanMap beanMap = BeanMap.create(bean);
        for (Object key : beanMap.keySet()) {
            map.put(String.valueOf(key), beanMap.get(key));
        }
        return map;
    }

    /**
     * map转换为对象
     *
     * @param map
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) {
        T bean = null;
        try {
            bean = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("转换map为bean发生异常", e);
        }
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * list<T>转换为Map<String, Object>
     *
     * @param beanList
     * @param <T>
     * @return
     */
    public static <T> List<Map<String, Object>> beansToMaps(List<T> beanList) {
        return Optional.ofNullable(beanList)
                .orElse(Collections.emptyList())
                .stream()
                .map(MapUtils::beanToMap)
                .collect(Collectors.toList());
    }

    /**
     * List<Map<String, Object>>转换为List<T>
     *
     * @param maps
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> mapsToBeans(List<Map<String, Object>> maps, Class<T> clazz) {
        return Optional.ofNullable(maps)
                .orElse(Collections.emptyList())
                .stream()
                .map(map -> mapToBean(map, clazz))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
