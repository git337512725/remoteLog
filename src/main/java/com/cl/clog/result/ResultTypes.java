package com.cl.clog.result;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public interface ResultTypes {
    public final String DEFAULT = "1";
    //分页列表
    public final String PAGE = "2";
    //不分页列表
    public final String LIST = "3";
    //不分页列表
    public final String RESULTVO = "4";
}
