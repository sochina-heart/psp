package com.sochina.test.domain.bing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public abstract class Bing {

    protected String name;

    /**
     * 准备工作
     */
    public void prepare() {
        log.info("揉面-切葱-完成准备工作");
    }

    /**
     * 使用自己专用袋-包装
     */
    public void pack() {
        log.info("烧饼-专用袋-包装");
    }

    /**
     * 制作设备-烘烤
     */
    public void fire() {
        log.info("烧饼-专用设备-烘烤");
    }
}
