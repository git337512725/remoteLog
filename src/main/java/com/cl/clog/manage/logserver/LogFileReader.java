package com.cl.clog.manage.logserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author Administrator
 */
@Slf4j
@Component
public class LogFileReader {


    public static long MAX_LIMIT = (long)(Integer.MAX_VALUE/10) ;

    public static long MAX_SKIP = (long)(Integer.MAX_VALUE/10) ;


    /**
     * 在文件中从读取日志
     * @param file 文件
     * @param skip 跳过的字数
     * @param limit  读取的长度
     */
    public String readSkip(File file,long skip,long limit) {
        if(skip<0 || limit<=0){
            return null;
        }
        long fLength = file.length();
        skip = getSkip(skip, fLength);
        limit = getLimit(limit, fLength);
        StringBuilder sb = null;
        RandomAccessFile randomFile = null;
        try {
            randomFile = new RandomAccessFile(file, "r");
            randomFile.seek(skip);
            sb = new StringBuilder();
            byte[] bs = new byte[1024];
            long fullTimes  = limit/bs.length;
            int lastRead = (int)limit%bs.length;
            long count = 0 ;
            long last = 0 ;
            int len = -1 ;
            while( (len=randomFile.read(bs))!=-1){
                if(count == fullTimes ) {
                    last = 1;
                }
                if(last==1){
                    String str = new String(bs,0,lastRead,"utf-8");
                    sb.append(str);
                    break;
                }else{
                    String str = new String(bs,0,len,"utf-8");
                    sb.append(str);
                }
                count++;
            }
        } catch (IOException e) {
            log.info("LogFileReader: 在文件中从读取日志异常",e);
        } finally {
            try {
                if (randomFile != null) {
                    randomFile.close();
                }
            } catch (IOException e) {
                log.info("",e);
            }
        }
        return sb.toString();
    }

    /***
     *  在文件中从读取日志
     * @param file 文件
     * @param skip 跳过的字数
     * @return
     */
    public String readSkip(File file,long skip){
        if(skip<0){
            return null;
        }
        return  readSkip(file, skip, MAX_LIMIT);
    }

    /***
     * 在文件中从读取日志
     * @param file
     * @return
     */
    public String readFile(File file){
        return   readSkip(file, 0L, MAX_LIMIT);
    }

    /***
     * 在文件中从从后往前读取日志
     * @param file 文件路径
     * @param limit 读取的长度
     * @return
     */
    public String readFromBottom(File file,long limit){
        if(limit<=0){
            return null;
        }
        long fLength = file.length();
        limit = getLimit(limit, fLength);
        long skip = (fLength - limit);
        return readSkip(file,skip,limit);
    }

    private long getLimit(long limit, long fLength) {
        long canRead = 0 ;
        if( MAX_LIMIT > fLength){
            canRead = fLength;
        }else{
            canRead = MAX_LIMIT;
        }
        if(limit > canRead){
            limit = canRead;
        }
        return limit;
    }

    private long getSkip(long skip ,long fLength){
        long canSkip = 0;
        if(MAX_SKIP <= fLength){
            canSkip = MAX_SKIP;
        }else{
            canSkip = fLength;
        }
        if(skip>canSkip) {
            skip = canSkip;
        }
        return skip;
    }

}
