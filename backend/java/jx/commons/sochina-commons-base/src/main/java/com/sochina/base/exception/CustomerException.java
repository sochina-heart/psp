package com.sochina.base.exception;

public class CustomerException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误提示
     */
    private String message;
    /**
     * 错误明细，内部调试错误
     */
    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public CustomerException() {
    }

    public CustomerException(String message) {
        this.message = message;
    }

    public CustomerException(String message, Throwable throwable) {
        super(message, throwable);
        this.message = message;
    }

    public CustomerException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public CustomerException setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public CustomerException setMessage(String message) {
        this.message = message;
        return this;
    }

    public Integer getCode() {
        return code;
    }
}