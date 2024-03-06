package com.sochina.gateway.illegal.checker.impl;

import com.sochina.base.utils.XssUtils;
import com.sochina.gateway.illegal.checker.IllegalChecker;

public class XssChecker implements IllegalChecker {
    private static volatile XssChecker singletonInstance;

    public static synchronized XssChecker getInstance() {
        if (singletonInstance == null) {
            synchronized (XssChecker.class) {
                if (singletonInstance == null) {
                    singletonInstance = new XssChecker();
                }
            }
        }
        return singletonInstance;
    }

    @Override
    public String doCheck(String str) {
        return XssUtils.escape(str);
    }

    @Override
    public String recover(String str) {
        return XssUtils.recover(str);
    }
}
