package com.sochina.base.exception;

public class HttpClientException extends RuntimeException {
    public HttpClientException() {
        super();
    }

    public HttpClientException(String msg) {
        super(msg);
    }

    public HttpClientException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public HttpClientException(String msg, Object... objects) {
        super(convertMsg(msg, objects));
    }

    public HttpClientException(Throwable throwable) {
        super(throwable);
    }

    public static String convertMsg(String msg, Object... objects) {
        if (msg.indexOf("{}") > 0) {
            String s = msg.replaceAll("\\{\\}", "%s");
            String format = null;
            try {
                format = String.format(s, objects);
            } catch (Exception e) {
                return msg;
            }
            return format;
        } else {
            return msg;
        }
    }
}
