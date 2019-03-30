package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.QueryBySiteId;


import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.QueryBySiteId;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-09-12 18:32
 **/
@Service
public class PageService {

    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    CmsSiteRepository cmsSiteRepository;

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
        matching=  matching.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        CmsPage cmsPage = new CmsPage();
        //模糊查询别名
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
        cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        //条件查询站点id
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
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
            cmsPageRepository.save(cmsPage1);
            return new CmsPageResult(CommonCode.SUCCESS,cmsPage1);
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }
}
