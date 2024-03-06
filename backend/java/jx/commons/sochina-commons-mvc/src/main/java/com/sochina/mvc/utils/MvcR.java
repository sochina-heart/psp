package com.sochina.mvc.utils;

import cn.hutool.http.ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sochina.base.utils.web.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.nio.charset.Charset;

public class MvcR extends R implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MvcR.class);

    public static void failRender(int code, String msg, HttpServletResponse response, int status) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            response.setContentType(ContentType.build(ContentType.JSON.getValue(), Charset.defaultCharset()));
            response.setStatus(status);
            response.getWriter().write(mapper.writeValueAsString(R.fail(code, msg)));
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
    }

    public static void failRender(HttpStatus resultCode, HttpServletResponse response, int status) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            response.setContentType(ContentType.build(ContentType.JSON.getValue(), Charset.defaultCharset()));
            response.setStatus(status);
            response.getWriter().write(mapper.writeValueAsString(R.fail(resultCode)));
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
    }
}
