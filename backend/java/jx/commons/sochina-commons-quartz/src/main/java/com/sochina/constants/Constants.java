package com.sochina.constants;

public class Constants {
    /**
     * RMI 远程方法调用
     */
    public static final String LOOKUP_RMI = "rmi:";
    /**
     * LDAP 远程方法调用
     */
    public static final String LOOKUP_LDAP = "ldap:";
    /**
     * LDAPS 远程方法调用
     */
    public static final String LOOKUP_LDAPS = "ldaps:";
    /**
     * 定时任务白名单配置（仅允许访问的包名，如其他需要可以自行添加）
     */
    public static final String[] JOB_WHITELIST_STR = {"com.sochina"};
    /**
     * 定时任务违规的字符
     */
    public static final String[] JOB_ERROR_STR = {"java.net.URL", "javax.naming.InitialContext", "org.yaml.snakeyaml",
            "org.springframework", "org.apache"};
}
