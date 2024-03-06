package com.sochina.test.service.impl;

import com.sochina.base.utils.web.AjaxResult;
import com.sochina.test.service.ITestService;
import org.springframework.stereotype.Service;

@Service
public class CustomTestServiceImpl implements ITestService {
    @Override
    public String getType() {
        return "1";
    }

    @Override
    public String doTest() {
        return "custom test";
    }

    @Override
    public AjaxResult doAjax() {
        return AjaxResult.success("custom test ajax");
    }
}
