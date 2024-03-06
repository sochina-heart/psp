package com.sochina.base.constants;

public class HttpStatus {
    /**
     * 操作成功
     */
    public static final int SUCCESS = 200;
    /**
     * 系统内部错误
     */
    public static final int ERROR = 500;
    /**
     * 对象创建成功
     */
    public static final int CREATED = 201;
    /**
     * 请求已经被接受
     */
    public static final int ACCEPTED = 202;
    /**
     * 操作已经执行成功，但是没有返回数据
     */
    public static final int NO_CONTENT = 204;
    /**
     * 资源已被移除
     */
    public static final int MOVED_PERM = 301;
    /**
     * 重定向
     */
    public static final int SEE_OTHER = 303;
    /**
     * 资源没有被修改
     */
    public static final int NOT_MODIFIED = 304;
    /**
     * 参数列表错误（缺少，格式不匹配）
     */
    public static final int BAD_REQUEST = 400;
    /**
     * 未授权
     */
    public static final int UNAUTHORIZED = 401;
    /**
     * 访问受限，授权过期
     */
    public static final int FORBIDDEN = 403;
    /**
     * 资源，服务未找到
     */
    public static final int NOT_FOUND = 404;
    /**
     * 不允许的http方法
     */
    public static final int BAD_METHOD = 405;
    /**
     * 资源冲突，或者资源被锁
     */
    public static final int CONFLICT = 409;
    /**
     * 不支持的数据，媒体类型
     */
    public static final int UNSUPPORTED_TYPE = 415;
    /**
     * 接口未实现
     */
    public static final int NOT_IMPLEMENTED = 501;
    /**
     * 系统警告消息
     */
    public static final int WARN = 601;
    /**
     * 接口限流
     */
    public static final int SENTINEL_FLOW = 110;
    /**
     * 服务降级
     */
    public static final int SENTINEL_DEGRADE = 111;
    /**
     * 热点参数限流
     */
    public static final int SENTINEL_PARAM_FLOW = 112;
    /**
     * 系触发统保护规则
     */
    public static final int SENTINEL_SYSTEM_BLOCK = 113;
    /**
     * 授权规则不通过
     */
    public static final int SENTINEL_AUTHORITY = 114;
    /**
     * ip黑名单
     */
    public static final int BLACK_IP = 109;
    /**
     * url黑名单
     */
    public static final int BLACK_URL = 108;
}
