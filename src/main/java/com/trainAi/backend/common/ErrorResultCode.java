package com.trainAi.backend.common;


public enum ErrorResultCode implements ResultCode{
    E000001( "数据异常"),
    E000002( "账号或密码错误"),
    ;


    private final int code;

    private final String message;

    ErrorResultCode(String message) {
        this.code = 500;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
