package com.cl.clog.manage.dto;

import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class RemoteLog {

    /**主题*/
    private String topic;

    private String content;
    /**1000 覆盖  1001 追加  */
    private int append;

    /**日志添加时间*/
    private long appendTime;
}
