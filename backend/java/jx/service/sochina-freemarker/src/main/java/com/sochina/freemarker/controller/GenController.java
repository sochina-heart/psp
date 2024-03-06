package com.sochina.freemarker.controller;

import com.alibaba.fastjson2.JSONObject;
import com.sochina.base.constants.Constants;
import com.sochina.base.utils.StringUtils;
import com.sochina.base.utils.web.AjaxResult;
import com.sochina.freemarker.domain.properties.DependencyProperties;
import com.sochina.mvc.annotation.log.LogMvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/gen")
public class GenController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenController.class);

    @LogMvc(title = "生成pom.xml")
    @PostMapping("/pom/xml")
    public AjaxResult genPomXml(@RequestBody JSONObject json) {
        String path = json.getString("path");
        if (StringUtils.isBlank(path)) {
            return AjaxResult.error("path不能为空");
        }
        ArrayList<DependencyProperties> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(Constants.COMMA);
                DependencyProperties dependency = new DependencyProperties();
                dependency.setGroupId(split[0]);
                dependency.setArtifactId(split[1]);
                dependency.setVersion(split[2]);
                list.add(dependency);
            }
        } catch (IOException e) {
            LOGGER.error("读取文件内容发生未知异常-{}", e.getMessage());
            return AjaxResult.error("读取文件内容发生错误");
        }
        return AjaxResult.success();
    }
}
