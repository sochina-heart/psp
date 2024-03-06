package com.sochina.gateway.illegal.checker;

public interface IllegalChecker {
    public String doCheck(String str);

    public String recover(String str);
}
