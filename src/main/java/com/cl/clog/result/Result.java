package com.cl.clog.result;


import com.alibaba.fastjson.JSON;

/**
 * 统一API响应结果封装
 */
public class Result {
    private String msgCode;
    private String msg;
    private Object result;

    public String getMsg() {
        return msg;
    }

    public Result setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Result setMsgCode(ResultCode resultCode) {
        this.msgCode = resultCode.getCode();
        return this;
    }

    public Result setMsgCode(String msgCode) {
        this.msgCode = msgCode;
        return this;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public Result setCode(String code) {
        this.msgCode = code;
        return this;
    }

    public Object getResult() {
        return result;
    }

    public Result setResult(Object result) {
        this.result = result;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
