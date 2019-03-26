package com.xuecheng.manage_cms.service;

<<<<<<< .mine


=======
import com.sun.deploy.util.StringUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;
>>>>>>> .theirs
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.QueryBySiteId;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
<<<<<<< .mine

import com.xuecheng.manage_cms.dao.CmsSiteRepository;
=======


>>>>>>> .theirs
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

<<<<<<< .mine
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


=======





>>>>>>> .theirs
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
}
