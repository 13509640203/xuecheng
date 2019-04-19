package com.xuecheng.manage_cms_client.dao;


import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CmsPageRepository extends MongoRepository<CmsPage,String> {
    //根据页面名称查询
    CmsPage findByPageName(String pageName);
    //根据页面名称和类型查询
    CmsPage findByPageNameAndPageType(String pageName,String PageType);
    //根据页面名称 ，站点iD  页面访问路径
    CmsPage  findByPageNameAndSiteIdAndPageWebPath(String pageName,String siteId,String pageWebPath);

}
