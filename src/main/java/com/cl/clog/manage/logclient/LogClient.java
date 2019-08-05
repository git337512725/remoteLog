package com.cl.clog.manage.logclient;

import com.cl.clog.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author Administrator
 */
@Slf4j
@Component
public class LogClient {

    @Value("${logClient.remoteUrl}")
    private String remoteUrl;

    private URI remoteUri;

    private ScheduledExecutorService executorService;

    public URI getRemoteUri() {
        if(remoteUri == null){
            return URI.create(remoteUrl);
        }else{
            return remoteUri;
        }
    }

    public  void send(String logContent)   {
        sendExecute(logContent);
    }


    public  void send(Exception e) {
        StringBuilder sb = putStringBuilder(e);
        send(sb.toString());
    }


    public void send(Object...args){
        send(null,args);
    }


    public void  send(Exception e ,Object...args){
        StringBuilder sb = putStringBuilder(e);
        Arrays.stream(args).forEach(o -> {
            if(!Objects.isNull(o)){
                sb.append("\t\t")
                        .append(o.toString())
                        .append(",");
            }else {
                sb.append("\t\t")
                        .append(o.toString())
                        .append("null");
            }
        });
        sb.append("\n");
        send(sb.toString());
    }


    private void sendExecute(final String content){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("content",content));
                URI uri2 = getRemoteUri();
                log.info(uri2.getScheme()+ "://" + uri2.getHost() + ":" + uri2.getPort()  + uri2.getPath());
                String httpRet = null;
                try {
                    httpRet = HttpUtil.postForm(uri2.getScheme(),uri2.getHost(),uri2.getPort(),uri2.getPath(), nameValuePairs);
                } catch (URISyntaxException e) {
                    log.info("",e);
                } catch (IOException e) {
                    log.info("",e);
                }
                log.info(httpRet);
            }
        });
    }


    private StringBuilder putStringBuilder(Exception e) {
        StringBuilder sb = new StringBuilder();
        try {
            if(e!=null){
                String logContent = e.toString();
                sb.append(logContent).append("\n");
            }
            StackTraceElement[] stackTrace = e.getStackTrace();
            Arrays.stream(stackTrace).forEach(element -> {
                sb.append("\t\t at ")
                        .append(element.getClassName())
                        .append(element.getMethodName())
                        .append("(")
                        .append(element.getFileName())
                        .append(":")
                        .append(element.getLineNumber()).append(")").append("\n");
            });
        } catch (Exception e1) {
            log.info("", e1);
        }
        return sb;
    }

    @PostConstruct
    public  void getPool(){
         log.info("开始创建写日志调度器 methodName：getPool");
         this.executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("log-schedule-pool-%d").daemon(true).build());

    }

    @PreDestroy
    public void  destroy(){
        try {
            executorService.shutdown();
        } catch (Exception e) {
            log.info("LogClient 销毁方法，停止executorService线程池" ,e);
            executorService = null;
        }
    }
}
