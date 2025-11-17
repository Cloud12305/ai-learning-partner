package com.digitalhorsepower.learnbuddy.common;

import lombok.Data;

@Data
public class Result<T> {
    private boolean success;
    private String message;
    private T data;
    private String error;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setData(data);
        result.setMessage("操作成功");
        return result;
    }

    public static <T> Result<T> success(T data, String message) {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setData(data);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> error(String error) {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setError(error);
        return result;
    }

    public static <T> Result<T> error(String error, String message) {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setError(error);
        result.setMessage(message);
        return result;
    }
}