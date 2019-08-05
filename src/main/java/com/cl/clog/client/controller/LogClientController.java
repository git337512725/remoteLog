package com.cl.clog.client.controller;


import com.cl.clog.manage.logclient.LogClient;
import com.cl.clog.result.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Administrator
 */
@Slf4j
@Controller
@RequestMapping("/logClient/")
public class LogClientController {

    @Autowired
    private LogClient logC;


    @RequestMapping("/testLog")
    @ResponseBody
    public Object testLog(){
        for(int i=0;i<10;i++){
            try {
                int m = 1/0;
            } catch (Exception e) {
                log.info( "第" + i + "次除数不能为0{}",e);
                try {
                    Thread.sleep(i*10);
                } catch (InterruptedException e1) {
                }
                logC.send(e,"第" + i + "次除数不能为0");
            }
        }
        return ResultGenerator.genSuccessResult("成功！");
    }


}
