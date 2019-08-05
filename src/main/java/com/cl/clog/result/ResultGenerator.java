package com.cl.clog.result;

/**
 * 响应结果生成工具
 */
public class ResultGenerator {
    private static final String defaultMsg = "OK";

    public static Result genSuccessResult(Object data) {
        return new Result()
                .setMsgCode(ResultCode.SUCCESS)
                .setMsg(defaultMsg)
                .setResult(data);
    }

    public static Result genSuccessMessage(String message) {
        return new Result()
                .setMsgCode(ResultCode.SUCCESS)
                .setMsg(message);
    }

    public static Result genSuccessResult(String message, Object data) {
        return new Result()
                .setMsgCode(ResultCode.SUCCESS)
                .setMsg(message)
                .setResult(data);
    }

    public static Result genFailResult(String message) {
        return new Result()
                .setMsg(message)
                .setMsgCode(ResultCode.FAIL);
    }

    public static Result genFailResult(String message, Object data) {
        return new Result()
                .setMsgCode(ResultCode.FAIL)
                .setMsg(message)
                .setResult(data);
    }

    public static Result genFailResult(ResultCode resultCode, String message, Object data) {
        return new Result()
                .setMsgCode(resultCode)
                .setMsg(message)
                .setResult(data);
    }

}
