package com.xuecheng.manage_cms_client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

@Service
public class PageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private CmsSiteRepository cmsSiteRepository;
    @Autowired
    private GridFSBucket gridFSBucket;
    @Autowired
    private GridFsTemplate gridFsTemplate;

    public void savePageToServicePath(String pageId) {
        CmsPage cmsPage = this.findById(pageId);
        if (cmsPage == null) {//页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXIST);
        }
        CmsSite cmsSite = this.findCmsSiteById(cmsPage.getSiteId());
       //页面物理路径
        String pagePath =cmsSite.getSitePhysicalPath()+cmsPage.getPagePhysicalPath() +cmsPage.getPageName();
         String fileId= cmsPage.getHtmlFileId();
         if(fileId==null){
             ExceptionCast.cast(CmsCode.CMS_PAGE_HTNLFILEId_ISNULL);
         }
        InputStream inputStream = getFileById(fileId);
        FileOutputStream fileOutputStream = null;
        try {//文件输出流
              fileOutputStream = new FileOutputStream(new File(pagePath));
            IOUtils.copy(inputStream,fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            try {
                fileOutputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    //通过文件htmlFileId获取文件内容
    public InputStream getFileById(String fileId){
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        try {
            return  gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    //通过页面id获取CmsPage
    public CmsPage findById(String pageId) {
        Optional<CmsPage> byId = cmsPageRepository.findById(pageId);
        if (byId.isPresent()) {
            CmsPage cmsPage = byId.get();
            return cmsPage;
        }
        return null;
    }

    //通过页面id获取cmsSite
    public CmsSite findCmsSiteById(String siteId) {
        Optional<CmsSite> byId = cmsSiteRepository.findById(siteId);
        if (byId.isPresent()) {
            CmsSite cmsSite = byId.get();
            return cmsSite;
        }
        return null;

    }
}
