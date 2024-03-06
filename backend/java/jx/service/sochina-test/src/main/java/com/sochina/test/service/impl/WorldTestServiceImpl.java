package com.sochina.test.service.impl;

import com.sochina.base.utils.web.AjaxResult;
import com.sochina.test.service.ITestService;
import org.springframework.stereotype.Service;

@Service
public class WorldTestServiceImpl implements ITestService {
    @Override
    public String getType() {
        return "3";
    }

    @Override
    public String doTest() {
        return "world test";
    }

    @Override
    public AjaxResult doAjax() {
        return AjaxResult.success("world test ajax");
    }
}
