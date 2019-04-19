package com.xuecheng.manage_cms_client.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.*;


import com.xuecheng.framework.domain.cms.QueryBySiteId;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms_client.config.RabbitmqConfig;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;

import com.xuecheng.manage_cms_client.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-09-12 18:32
 **/
@Service
public class PageService {
    private  static  final Logger LOGGER = LoggerFactory.getLogger(PageService.class);
    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    CmsSiteRepository cmsSiteRepository;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    //存贮和获取文件用的
    @Autowired
    GridFsTemplate gridFsTemplate;
    //用户打开下载流对象的
    @Autowired
    GridFSBucket gridFSBucket;
     @Autowired
    RabbitTemplate rabbitTemplate;
    /**
     * 页面查询方法
     * @param page 页码，从1开始记数
     * @param size 每页记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest){
      if(queryPageRequest==null){
          queryPageRequest=new QueryPageRequest();
      }
        //条件匹配器
       //页面名称模糊查询，需要自定义字符串的匹配器实现模糊查询
        ExampleMatcher matching = ExampleMatcher.matching();
        matching=  matching.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains())
                             .withMatcher("pageName",ExampleMatcher.GenericPropertyMatchers.contains());
        CmsPage cmsPage = new CmsPage();
        //模糊查询别名
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
        cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        //条件查询站点id
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //页面名称 模糊查询
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(queryPageRequest.getPageName())){
            cmsPage.setPageName(queryPageRequest.getPageName());
        }

        //页面类型
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(queryPageRequest.getPageType())){
            cmsPage.setPageType(queryPageRequest.getPageType());
        }
        //模板id
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        //创建条件实例
        Example<CmsPage> example = Example.of(cmsPage, matching);

        if(page<=0){//初始化为
            page=1;
        }
        page=page-1;
        if(size<=0){
            size=10;
        }
        Pageable Pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(example,Pageable);
      //  cmsPageRepository.f
        QueryResult<CmsPage> queryResult = new QueryResult<>();
        if(all.getSize()>0) {
            queryResult.setList(all.getContent());
            queryResult.setTotal(all.getTotalElements());
            return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        }
        return new QueryResponseResult(CommonCode.FAIL, queryResult);
    }

      //查询所有站点id
    public QueryResponseResult queryBySiteId(){
        List<QueryBySiteId> queryBySiteIds = new ArrayList<>();
        List<CmsSite> list = cmsSiteRepository.findAll();
        for(CmsSite cmsSite:list){//从CmsSite获取站点id站点名称保存到QueryBySiteId
            QueryBySiteId queryBySiteId = new QueryBySiteId();
            queryBySiteId.setSiteId(cmsSite.getSiteId());
            queryBySiteId.setSiteName(cmsSite.getSiteName());
            queryBySiteIds.add(queryBySiteId);

        }
        QueryResult<QueryBySiteId> queryResult = new QueryResult<>();
        queryResult.setTotal(list.size());
        queryResult.setList(queryBySiteIds);
        return  new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }

    public CmsPageResult add(CmsPage cmsPage){

        if(cmsPage!=null){
            CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(),
                    cmsPage.getSiteId(), cmsPage.getPageWebPath());

            if(cmsPage1!=null){//CmsCode是枚举，实现ResultCode
                ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
            }

            if(cmsPage1==null){
                //添加页面主键由spring data 自动生成
                cmsPage.setPageId(null);
                cmsPageRepository.save(cmsPage);
                return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
            }
        }

          return new CmsPageResult(CommonCode.FAIL,null);
    }
     //更新前通过id查找再回显
    public CmsPage findById(String id) {
        Optional<CmsPage> byId = cmsPageRepository.findById(id);
        if(byId.isPresent()){//不为空
            return byId.get();
        }
        return null;
    }
    //更新页面
    public CmsPageResult update(CmsPage cmsPage, String id) {
        CmsPage cmsPage1 = findById(id);
        if(cmsPage1!=null){
            cmsPage1.setSiteId(cmsPage.getSiteId());
            cmsPage1.setTemplateId(cmsPage.getTemplateId());
            cmsPage1.setPageName(cmsPage.getPageName());
            cmsPage1.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            cmsPage1.setPageWebPath(cmsPage.getPageWebPath());
            cmsPage1.setPageAliase(cmsPage.getPageAliase());
            cmsPage1.setPageCreateTime(cmsPage.getPageCreateTime());
            cmsPage1.setDataUrl(cmsPage.getDataUrl());
            cmsPageRepository.save(cmsPage1);
            return new CmsPageResult(CommonCode.SUCCESS,cmsPage1);
        }

        return new CmsPageResult(CommonCode.FAIL,null);
    }
     //通过id删除
    public CmsPageResult deleteById(String id) {
        if(id!=null){
            cmsPageRepository.deleteById(id);
            return new CmsPageResult(CommonCode.SUCCESS,null);
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

   //页面静态化
    public String getPageHtml(String pageId){
        //获取页面模板数据
        Map model = getModelByPageId(pageId);
        if(model==null){
            //获取页面模板为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        //获取页面模板
        String content = getTemplateByPageId(pageId);
        //执行静态化
        if(StringUtils.isEmpty(content)){
            //获取页面模板为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        String html = getPageHtml(model, content);
        if(StringUtils.isEmpty(html)){
            //获取页面模板为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        return  html;
    }
    //获取模板数据
    private Map getModelByPageId(String pageId){
        CmsPage byId = this.findById(pageId);
        if(byId==null){
          ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXIST);
        }
        //获取dataUrl cms_page中有有dataUrl
        String dataUrl = byId.getDataUrl();
        if(StringUtils.isEmpty(dataUrl)){
         ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        //dataUrl 就是请求新的服务
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return  body;
    }
    //获取页面模板
    private String getTemplateByPageId(String pageId ) {
        CmsPage byId = this.findById(pageId);
        if(byId==null){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        //这个id 就是 cms_template 集合中的_id
        String templateId = byId.getTemplateId();
        //通过这个 templateId 去数据库中cms_template集合找到templateFileId
        if(StringUtils.isEmpty(templateId)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        Optional<CmsTemplate> byId1 = cmsTemplateRepository.findById(templateId);
        if(byId1.isPresent()){
            CmsTemplate cmsTemplate = byId1.get();
            //得到了模板文件templateFileId 就是fs_chunks集合中的files_id
            String templateFileId = cmsTemplate.getTemplateFileId();

            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            //打开下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //创建gridFsResource用于获取下载流对象
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
            //获取流中的数据
            try {
                String contest = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
                return  contest;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return  null;
    }

    //基于模板字符串生成静态化文件
    private String getPageHtml(Map model,String content){
        Configuration configuration=new Configuration(Configuration.getVersion());
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",content);
        //配置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        //得到模板
        try {
            Template template = configuration.getTemplate("template");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            return html;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

   //页面发布
    public ResponseResult postPage(String pageId){
          //执行静态化
        String pageHtml = this.getPageHtml(pageId);
        //保存静态文件
         saveHtml(pageId,pageHtml);
        //发布消息
        sendpostPage(pageId);
        return  new ResponseResult(CommonCode.SUCCESS);
    }

      //发送消息
    private void sendpostPage(String pageId){
        Optional<CmsPage> byId = cmsPageRepository.findById(pageId);
        if(!byId.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_PAGE_ISNULL);
        }
        CmsPage cmsPage = byId.get();
        //siteId 是作为routingKey的
        String siteId = cmsPage.getSiteId();
        Map<String, String> map = new HashMap<>();
        map.put("pageId",pageId);
        String msg = JSON.toJSONString(map);
        //参数1：交换机 参数2：routingKey 参数3 消息pageId
         rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE,siteId,msg);
    }


    //保存静态文件
    private void saveHtml(String pageId,String pageHtml ){
        Optional<CmsPage> byId = cmsPageRepository.findById(pageId);
        if(!byId.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_PAGE_ISNULL);
        }
        CmsPage cmsPage = byId.get();
        //将文件内容变成输入流
        InputStream inputStream=null;
        try {
             inputStream = IOUtils.toInputStream(pageHtml, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //保存成功会得到文件id 也就是htmlFileId  也是templateFileId
        ObjectId objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());
        //经文件id保存到数据库
        cmsPage.setHtmlFileId(String.valueOf(objectId));
        cmsPageRepository.save(cmsPage);

    }
}
