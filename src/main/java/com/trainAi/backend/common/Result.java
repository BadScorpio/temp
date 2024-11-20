package com.trainAi.backend.common;

public class Result<T> {
    private int code;
    private String message;
    private T data;

    private Result(){

    }

    public static Result ok(){
        Result results = new Result();
        CommonResultCode resultCode = CommonResultCode.OK;
        results.code = resultCode.getCode();
        results.message = resultCode.name();
        return results;
    }

    public static <T> Result<T> ok(T data){
        Result<T> results = new Result<>();
        CommonResultCode resultCode = CommonResultCode.OK;
        results.code = resultCode.getCode();
        results.message = resultCode.name();
        results.data = data;
        return results;
    }

    public static Result error(ResultCode resultCode){
        Result result = new Result();
        result.code = resultCode.getCode();
        result.message = resultCode.name();
        return result;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
