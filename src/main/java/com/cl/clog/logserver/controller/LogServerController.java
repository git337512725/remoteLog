package com.cl.clog.logserver.controller;


import com.cl.clog.manage.dto.RemoteLog;
import com.cl.clog.manage.logserver.IManageLog;
import com.cl.clog.result.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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
