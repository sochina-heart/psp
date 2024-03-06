package com.sochina.test.service;

import com.sochina.base.utils.web.AjaxResult;

public interface ITestService {
    public String getType();

    public String doTest();

    public AjaxResult doAjax();
}
