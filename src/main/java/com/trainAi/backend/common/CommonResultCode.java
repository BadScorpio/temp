package com.trainAi.backend.common;


public enum CommonResultCode implements ResultCode{
    OK(200, "success"),
    LOGIN_ERROR(401, "Not logged in"),
    AUTH_ERROR(403, "no permissions"),
    AUT_ERROR(405,"Illegal request"),
    INTERNAL_ERROR(500, "The system is busy, please try again later"),
    ;


    private final int code;

    private final String message;

    CommonResultCode(int code, String message) {
        this.code = code;
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
