package com.cl.clog.manage.logserver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDate;

/**
 * @author Administrator
 */
public interface IManageLog {

    /**
     * 将日志写入到文件中
     * @param path 文件路径
     * @param content  日志内容
     */
    void writeStr(String path, String content);

    /**
     * 返回日志基础路径
     * @return
     */
    String basePath();

    /**
     * 返回主题路径
     * @param topic
     * @return
     */
    String produceTopicPath(String topic);

    /**
     * 将日志写入到文件中
     * @param path 文件路径
     * @param content  日志内容
     * @param request  本次请求信息对象
     */
    void writeStr(String path, String content, HttpServletRequest request);

    /**
     * 根据日期和主题获取主题文件路径
     * @param date
     * @param topic
     * @return
     */
    String getTopicPath(LocalDate date, String topic);

    /**
     * 在文件中从读取日志
     * @param file 文件
     * @param skip 跳过的字数
     * @param limit  读取的长度
     */
    String readSkip(File file, long skip, long limit);


    /***
     *  在文件中从读取日志
     * @param file 文件
     * @param skip 跳过的字数
     * @return
     */
    String readSkip(File file, long skip);



    /***
     * 在文件中从读取日志
     * @param file
     * @return
     */
    String readFile(File file);


    /***
     * 在文件中从从后往前读取日志
     * @param file 文件路径
     * @param limit 读取的长度
     * @return
     */
    String readFromBottom(File file, long limit);

}
