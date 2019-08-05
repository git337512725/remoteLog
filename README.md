# remoteLog
just send log to a api and write into a logfile on a remote computer . attempt to collect   dispersed logs  to one place
1.for  test  you can just call LogClientController mapping /logClient/testLog  to write logs and see logfile in the path remoteLog.logPath where  configure  by application.yml 
2.you can call LogServerController mapping  /logServer/readLogs  to see logs on browser or  download file by /logServer/readLogs2
