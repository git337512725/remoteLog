package com.cl.clog.manage.logserver;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author Administrator
 */
@Component
public class LogFormat {

    private String currentTime = (LocalDate.now().toString()) + " " + (LocalTime.now().toString());

    private String formatHead = "[" +  currentTime +"] ";

    public  String format(String content){
        content = content.replaceAll("\n","\r\n").replaceAll("\r","\r\n");
        StringBuilder sb = new StringBuilder();
        content = sb.append(formatHead)
                    .append(content)
                    .append("\r\n")
                    .toString();
        return content;
    }



}
