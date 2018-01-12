package com.ymall.service.Impl;

import com.google.common.collect.Lists;
import com.ymall.service.IFileService;
import com.ymall.util.FTPUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Service("fileService")
@Slf4j
public class FileServiceImpl implements IFileService{



    @Override
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+ 1);
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        log.info("开始上传文件， 文件名为：{}， 路径为 ：{}", uploadFileName, path);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
            log.info("创建upload文件夹");
        }

        File targetFile = new File(path, uploadFileName);

        try {
            file.transferTo(targetFile);

            //上传到FTP服务器
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));

            //删除web服务器上的文件
            targetFile.delete();

        } catch (IOException e) {
            log.error("上传文件异常",e);
            return null;
        }
        return targetFile.getName();
    }
}
