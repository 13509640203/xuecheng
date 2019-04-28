package com.xuecheng.test.fastDFS;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FastDFS01 {
    //文件上传
@Test
 public void test01(){
 //加载配置文件
    try {
        ClientGlobal.initByProperties("config/fastdfs-client.properties");
        //创建一个tracker客户端
        TrackerClient trackerClient = new TrackerClient();
        //获得连接tracker服务器
        TrackerServer trackerServer = trackerClient.getConnection();
        //获得stroage
        StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
        //获得storageClient
        StorageClient1 storageClient1 = new StorageClient1(trackerServer,storeStorage);
        //文件上传
        String filePath="d:/chartDemo.jpeg";
        String fileId=  storageClient1.upload_file1(filePath,"jpeg",null);
        System.out.println("fileId= "+fileId);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
 //文件下载
 @Test
 public void test02(){
     try {
         //加载fastdfs-client.properties配置文件
         ClientGlobal.initByProperties("config/fastdfs-client.properties");
         //定义TrackerClient，用于请求TrackerServer
         TrackerClient trackerClient = new TrackerClient();
         //连接tracker
         TrackerServer trackerServer = trackerClient.getConnection();
         //获取Stroage
         StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
         //创建stroageClient
         StorageClient1 storageClient1 = new StorageClient1(trackerServer,storeStorage);
         //下载文件
         //文件id
         String fileId = "group1/M00/00/00/wKgZhVzD9l-AOryTAAAzLbRUrGw21.jpeg";
         byte[] bytes = storageClient1.download_file1(fileId);
         //使用输出流保存文件
         FileOutputStream fileOutputStream = new FileOutputStream(new File("c:/p2opo.jpg"));
         //  FileOutputStream fileOutputStream = new FileOutputStream(new File("c:/popo.jpeg"));
         //使用输出流保存文件
         fileOutputStream.write(bytes);
     } catch (IOException e) {
         e.printStackTrace();
     } catch (MyException e) {
         e.printStackTrace();
     }
 }
    //文件查询
    @Test
    public void test03(){
        try {
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer,
                    storageServer);
            FileInfo fileInfo = storageClient.query_file_info("group1",
                    "M00/00/00/wKgZhVzD9l-AOryTAAAzLbRUrGw21.jpeg");
            System.out.println("fileInfo= "+fileInfo);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

    }
}
