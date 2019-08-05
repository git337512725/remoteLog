package com.cl.clog.result;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ResultCode {
    SUCCESS("0000", "成功"),//成功
    FAIL("9999", "请求失败"),//失败
    NOTOKEN("0001", "无效的访问令牌");//未认证（签名错误）
    private String code;
    private String message;

    ResultCode(String code, String messagee) {
        this.code = code;
        this.message = messagee;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
