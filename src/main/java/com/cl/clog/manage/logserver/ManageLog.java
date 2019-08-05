package com.cl.clog.manage.logserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author Administrator
 */
@Slf4j
@Component
public class ManageLog  implements IManageLog{

    @Value("${remoteLog.logPath}")
    private String basePATH;

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private  LogFileWriter logFileWriter;

    @Autowired
    private LogFileReader logFileReader;

    @Autowired
    private LogFormat  logFormat;

    @Autowired
    private DiscoverTopicFile discoverTopicFile;

    @Override
    public void writeStr(String fullPath, String content) {
        content = logFormat.format(content);
        logFileWriter.writeStr(fullPath,content);
    }

    @Override
    public String basePath() {
        return basePATH;
    }

    @Override
    public String getTopicPath(String topic) {
        if("".equals(topic) || null == topic){
            topic = "default";
        }
        return basePath() + "/" +  dateFormat.format(new Date()) + "/" +  topic + ".log";
    }

    @Override
    public void writeStr(String path, String content, HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        content = sb.append("[")
                .append(request.getRemoteHost())
                .append(":" )
                .append(request.getRemotePort())
                .append("]")
                .append(content).toString();
        writeStr(path,content);
    }


    @Override
    public String getByDateAndTopic(LocalDate date , String topic){
        topic = topic + ".log";
        Map<String, Map<String, String>> dirFileMap = discoverTopicFile.getDateDirFileMap();
        Map<String, String> map = dirFileMap.get(date.toString());
        if(Objects.isNull(map)){
            return null;
        }
        return map.get(topic);
    }
    @Override
    public String readSkip(File file, long skip, long limit){
        return logFileReader.readSkip(file,skip,limit);
    }
    @Override
    public String readSkip(File file,long skip){
        return logFileReader.readSkip(file,skip);
    }

    @Override
    public String readFile(File file){
        return   logFileReader.readFile(file);
    }
    @Override
    public String readFromBottom(File file,long limit){
        return logFileReader.readFromBottom(file,limit);
    }
}
