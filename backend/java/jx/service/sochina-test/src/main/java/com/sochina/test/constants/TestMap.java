package com.sochina.test.constants;

import com.sochina.base.utils.web.AjaxResult;
import com.sochina.test.service.ITestService;
import com.sochina.test.service.impl.CustomTestServiceImpl;
import com.sochina.test.service.impl.HelloTestServiceImpl;
import com.sochina.test.service.impl.WorldTestServiceImpl;

import java.util.HashMap;
import java.util.Map;

public class TestMap {
    private static final Map<Integer, String> USER_MAP = new HashMap<>();
    private static final Map<String, ITestService> AJAX_MAP = new HashMap<>();

    static {
        USER_MAP.put(1, "hello 1");
        USER_MAP.put(2, "hello 2");
        USER_MAP.put(3, "hello 3");
        USER_MAP.put(4, "hello 4");
        AJAX_MAP.put("1", new CustomTestServiceImpl());
        AJAX_MAP.put("2", new WorldTestServiceImpl());
        AJAX_MAP.put("3", new HelloTestServiceImpl());
    }

    public static String getUser(Integer i) {
        // return USER_MAP.get(i);
        return USER_MAP.getOrDefault(i, "sochina");
    }

    public static AjaxResult getAjax(String i) {
        return AJAX_MAP.getOrDefault(i, new CustomTestServiceImpl()).doAjax();
    }
}
