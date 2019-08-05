package com.cl.clog.logserver.controller;


import com.cl.clog.manage.dto.RemoteLog;
import com.cl.clog.manage.logserver.IManageLog;
import com.cl.clog.result.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Objects;

/**
 * @author Administrator
 */
@Slf4j
@Controller
@RequestMapping("/logServer/")
public class LogServerController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IManageLog manageLog;

    /**
     * 接受远程调用此方法，接受远程传递过来的日志
     */
    @RequestMapping("/acceptRemote")
    @ResponseBody
    public Object acceptRemote(RemoteLog remoteLog){
        if(validate(remoteLog)){
            manageLog.writeStr(manageLog.getTopicPath(remoteLog.getTopic()) ,remoteLog.getContent());
        }
        return ResultGenerator.genSuccessResult("写入日志成功！");
    }


    /**
     * browser  readLogs
     * @param response
     */
    @RequestMapping("/readLogs")
    public void readLogs(HttpServletResponse response){
        try {
            response.setCharacterEncoding("gbk");
            PrintWriter writer = response.getWriter();
            String topic = manageLog.getByDateAndTopic(LocalDate.now(), "default");
            File f  = new File(topic);
            String s = manageLog.readSkip(f ,100,1000000);
            writer.write(s);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            log.info("",e);
        } finally {
        }
    }

    /***
     * download logfile
     * @param response
     */
    @RequestMapping("/readLogs2")
    public void readLogs2(HttpServletResponse response){
        try {
            String topic = manageLog.getByDateAndTopic(LocalDate.now(), "default");
            File f  = new File(topic);
            ServletOutputStream outputStream = response.getOutputStream();
            response.reset();
            response.setHeader("Content-Disposition","attachment;fileName=default.log");
            response.setContentType("application/octet-stream;charset=utf-8");
            String s = manageLog.readSkip(f ,100,1000000);
            response.addHeader("Content-Length", ""+ s.length());
            IOUtils.write(s.getBytes(), outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.info("",e);
        } finally {
        }
    }



    private boolean validate(RemoteLog remoteLog){
        if(Objects.isNull(remoteLog.getContent())){
            return false;
        }else{
            remoteLog.setAppendTime(System.currentTimeMillis());
            StringBuilder sb = new StringBuilder();
            String content = sb.append("[")
                    .append(request.getRemoteHost())
                    .append(":" )
                    .append(request.getRemotePort())
                    .append("]")
                    .append(remoteLog.getContent()).toString();
            remoteLog.setContent(content);
            return true;
        }
    }


}
