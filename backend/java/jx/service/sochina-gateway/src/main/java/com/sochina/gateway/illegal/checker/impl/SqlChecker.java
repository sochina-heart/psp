package com.sochina.gateway.illegal.checker.impl;

import com.sochina.base.utils.SqlUtils;
import com.sochina.gateway.illegal.checker.IllegalChecker;

public class SqlChecker implements IllegalChecker {
    private static volatile SqlChecker singletonInstance;

    public static synchronized SqlChecker getInstance() {
        if (singletonInstance == null) {
            synchronized (SqlChecker.class) {
                if (singletonInstance == null) {
                    singletonInstance = new SqlChecker();
                }
            }
        }
        return singletonInstance;
    }

    @Override
    public String doCheck(String str) {
        return SqlUtils.processSql(str);
    }

    @Override
    public String recover(String str) {
        return str;
    }
}
