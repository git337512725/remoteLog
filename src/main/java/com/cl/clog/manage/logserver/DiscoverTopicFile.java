package com.cl.clog.manage.logserver;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
@Slf4j
@Component
public class DiscoverTopicFile implements  Runnable{

    @Value("${remoteLog.logPath}")
    private String basePath;

    private ScheduledExecutorService discoverService;

    private Map<String,Map<String,String>> dateDirFileMap = new TreeMap<String,Map<String,String>>();

    private static  final Integer MAX_CACHE_DAYS_FILE = 30 ;

    @PostConstruct
    public  void getPool(){
        log.info("DiscoverTopicFile methodName：getPool");
        this.discoverService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("discoverTopicFile-schedule-pool-%d").daemon(true).build());

        discoverService.schedule(new Runnable() {
            @Override
            public void run() {
                log.info("run discoverBefore");
                discoverBefore();
            }
        }, 2, TimeUnit.SECONDS);
        discoverService.scheduleAtFixedRate(this,5,10, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void  destroy(){
        try {
            discoverService.shutdown();
        } catch (Exception e) {
            log.info(" DiscoverTopicFile 销毁方法，停止discoverService线程池" ,e);
            discoverService = null;
        }
    }


    public void discoverBefore(){
        log.info("discoverBefore");
        File dir = new File(this.basePath);
        int len = dir.list().length;
        for(int i=1;i<MAX_CACHE_DAYS_FILE && i<len;i++){
            String beforeDate = LocalDate.now().plusDays((0-i)).toString();
            log.info("{}",beforeDate);
            discoverByDate(beforeDate);
        }
    }

    public void discoverByDate(String date){
        File dir = new File(this.basePath);
        File[] files = dir.listFiles();
        Arrays.stream(files).forEach(file -> {
            if(file.isDirectory()){
                if(file.getName().equals(date)){
                    Map<String, String> dateMap = dateDirFileMap.get(date);
                    if(Objects.isNull(dateMap)){
                        dateMap = new HashMap<String,String>(16);
                        dateDirFileMap.put(date,dateMap);
                    }
                    final Map<String, String> dateMapFinal = dateDirFileMap.get(date);
                    File[] subFiles = file.listFiles();
                    Arrays.stream(subFiles).forEach(subf -> {
                        dateMapFinal.put(subf.getName(),subf.getAbsolutePath());
                    });
                }
            }
        });
    }


    public void discoverNowDate(){
        log.info("discoverNowDate");
        String nowDate = LocalDate.now().toString();
        discoverByDate(nowDate);
    }


    public Map<String, Map<String, String>> getDateDirFileMap() {
        return dateDirFileMap;
    }

    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now().plusDays(-1);
        System.out.println(localDate.toString());
    }

    @Override
    public void run() {
       discoverNowDate();
    }
}
