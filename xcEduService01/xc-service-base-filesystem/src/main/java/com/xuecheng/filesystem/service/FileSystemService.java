package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileSystemService {
    @Autowired
    FileSystemRepository fileSystemRepository;
    @Value("${xuecheng.fastdfs.tracker_servers}")
    String tracker_servers;
    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    int connect_timeout_in_seconds;

    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    int network_timeout_in_seconds;

    @Value("${xuecheng.fastdfs.charset}")
    String charset;
    public UploadFileResult upload(MultipartFile multipartFile, String filetag,
                                    String businesskey, String metedata) {
        if(multipartFile==null){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
       //文件上传
        String fileId = getFileId(multipartFile);
        if(StringUtils.isEmpty(fileId)){//为空返回去，上传失败
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEIDISNULL);
        }
        //将文件id等信息存储到mongodb的文件数据库中
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(fileId);
        fileSystem.setFilePath(fileId);
        //业务标签
        fileSystem.setFiletag(filetag);
        fileSystem.setFileName(multipartFile.getOriginalFilename());
        fileSystem.setFileSize(multipartFile.getSize());
        //业务key
        fileSystem.setBusinesskey(businesskey);
        //文件类型
        fileSystem.setFileType(multipartFile.getContentType());
        if(StringUtils.isNotEmpty(metedata)){ //文件元信息
            //string转成map
            Map map = JSON.parseObject(metedata, Map.class);
            fileSystem.setMetadata(map);
        }
        fileSystemRepository.save(fileSystem);
        return  new UploadFileResult(CommonCode.SUCCESS,fileSystem);
    }
     //上传文件获得文件id
    private String getFileId(MultipartFile multipartFile){
        initFdfsConfig();
        TrackerClient trackerClient = new TrackerClient();
        try {

            TrackerServer  trackerServer = trackerClient.getConnection();
            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storeStorage);
            //
            byte[] bytes = multipartFile.getBytes();
            //获得原始的名称
            String originalFilename = multipartFile.getOriginalFilename();
            //获取文件的扩展名
            String ex = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileId = storageClient1.upload_file1(bytes, ex, null);
            return fileId;
        } catch (Exception e) {
            e.printStackTrace();
           ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }

        return  null;
    }

    //文件上传环境初始化
    private void initFdfsConfig(){
        try {
            ClientGlobal.initByTrackers(tracker_servers);
            ClientGlobal.setG_charset(charset);
            ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
            ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_JINGTAIHUAISNULL);
        }
    }

}
