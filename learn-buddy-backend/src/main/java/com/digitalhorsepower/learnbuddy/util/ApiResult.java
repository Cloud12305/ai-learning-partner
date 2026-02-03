package com.digitalhorsepower.learnbuddy.util;

import lombok.Data;

@Data
public class ApiResult<T> {

    private int code;
    private String message;
    private T data;

    public static <T> ApiResult<T> success(T data) {
        ApiResult<T> r = new ApiResult<>();
        r.code = 200;
        r.message = "success";
        r.data = data;
        return r;
    }

    public static <T> ApiResult<T> fail(String msg) {
        ApiResult<T> r = new ApiResult<>();
        r.code = 500;
        r.message = msg;
        r.data = null;
        return r;
    }
}
