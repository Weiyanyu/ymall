package com.ymall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FTPUtil {

    private static Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp = PropertiesUtil.getStringProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getStringProperty("ftp.user");
    private static String ftpPassword = PropertiesUtil.getStringProperty("ftp.pass");

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    //todo 文件上传工具类需要反思
    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    public static boolean uploadFile(List<File> fileList) throws IOException{
        logger.info("开始连接FTP服务器");
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPassword);
        boolean isSuccess = ftpUtil.uploadFile("img/", fileList);
        logger.info("上传文件完毕 : {}", isSuccess);
        return isSuccess;
    }

    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException{
        FileInputStream inputStream = null;
        boolean isUpload = true;
        if (connectFTP(ip,port, user, pwd)) {
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                logger.info(ftpClient.printWorkingDirectory());
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //TODO 本地被动模式，还有一个远程被动模式，区别不知
                ftpClient.enterLocalPassiveMode();

                for (File file : fileList) {
                    inputStream = new FileInputStream(file);
                    ftpClient.storeFile(file.getName(),inputStream);
                }

            } catch (IOException e) {
                isUpload = false;
                logger.error("上传文件异常",e);
            } finally {
                ftpClient.disconnect();
                inputStream.close();
            }
        }
        return isUpload;
    }

    private boolean connectFTP(String ip, int port, String user, String pwd) {
        boolean isSuccess;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user, pwd);
        } catch (IOException e) {
            isSuccess = false;
            logger.error("登录FTP服务器错误", e);
        }
        return isSuccess;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
