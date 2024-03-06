package com.sochina.test.service.impl;

import com.sochina.base.utils.web.AjaxResult;
import com.sochina.test.service.ITestService;
import org.springframework.stereotype.Service;

@Service
public class HelloTestServiceImpl implements ITestService {
    @Override
    public String getType() {
        return "2";
    }

    @Override
    public String doTest() {
        return "hello test";
    }

    @Override
    public AjaxResult doAjax() {
        return AjaxResult.success("hello test ajax");
    }
}
