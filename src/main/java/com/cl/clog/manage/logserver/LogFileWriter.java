package com.cl.clog.manage.logserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Administrator
 */
@Slf4j
@Component
public class LogFileWriter {


    public  void writeStr( String path,String content){
        File file = checkFile(path);
        if(file!=null){
            write(file,content);
        }else{
            log.info("writeStr 文件路径出错");
        }
    }


    private   File checkFile(String filePath){
        boolean flag = true;
        File file  = new File(filePath);
        log.info(" filePath:{},开始检查文件路径...",filePath);
        if(!file.exists()){
            try {
                File parentFile = file.getParentFile();
                if(!parentFile.exists()){
                    parentFile.mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                log.info("文件路径出错",e);
                flag = false;
            }
        }
        return (flag? file : null );
    }


    private   void write(File file ,String content) {
        FileOutputStream fos = null;
        ByteBuffer buf = null;
        try {
            fos = new FileOutputStream(file,true);
            FileChannel fc = fos.getChannel();
            buf = ByteBuffer.allocate((content.length()+100));
            buf.put(content.getBytes());
            buf.flip();
            fc.write(buf);
            buf.clear();
        } catch (Exception e) {
            log.info("{}",e);
        }finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                 log.info("{}",e);
            }
            if(buf!=null){
                buf = null;
            }
        }
    }

}
