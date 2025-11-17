package com.digitalhorsepower.learnbuddy.dto;

import lombok.Data;

@Data
public class BaseResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public BaseResponse() {}

    public BaseResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // 成功响应
    public static <T> BaseResponse<T> success(String message, T data) {
        return new BaseResponse<>(true, message, data);
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(true, "操作成功", data);
    }

    public static BaseResponse<Object> success(String message) {
        return new BaseResponse<>(true, message, null);
    }

    // 错误响应
    public static <T> BaseResponse<T> error(String message) {
        return new BaseResponse<>(false, message, null);
    }

    public static <T> BaseResponse<T> error(String message, T data) {
        return new BaseResponse<>(false, message, data);
    }
}