package com.xuecheng.manage_cms_client.dao;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.manage_cms_client.service.PageService;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFSTest {
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucket;
    @Autowired
    PageService pageService;
    @Test
    public void testGridFs() throws FileNotFoundException {
        String pageHtml = pageService.getPageHtml("5ca0d8ea640cb51dd4530ce8");
        System.out.println("哈哈: "+pageHtml);

    }
     @Test
    public void  queryFile() throws IOException {
      String fileId ="5ca6d22a19b90d1da4a042cb";
    //根据id文件查询
         GridFSFile gridFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
      //打开下载流对象
         GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFile.getObjectId());
         //创建gridFsResource，用于获取流对象
         GridFsResource gridFsResource = new GridFsResource(gridFile, gridFSDownloadStream);
         //获取数据流对象
         String tt = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
         System.out.println("结果： "+tt);

     }

    @Test
    public void  delete()  {
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is("5ca6d35219b90d2790d74d9c")));
    }
}
