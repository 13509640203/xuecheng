package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsPageParam;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.QueryBySiteId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-09-12 18:11
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    CmsSiteRepository cmsSiteRepository;
    @Test
    public void testFindAll(){
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println("dd: "+all);

    }

    //分页查询
    @Test
    public void testFindPage(){
        //分页参数
        int page = 1;//从0开始
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }

    //修改
    @Test
    public void testUpdate() {
        //查询对象
        Optional<CmsPage> optional = cmsPageRepository.findById("5a754adf6abb500ad05688d9");
        System.out.println("哈哈= "+optional);
        if(optional.isPresent()){
            CmsPage cmsPage = optional.get();
            //设置要修改值
            cmsPage.setPageAliase("test01");
            //...
            //修改
            CmsPage save = cmsPageRepository.save(cmsPage);
            System.out.println(save);
        }

    }
    @Test
    public void add() {
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName("popo");
        cmsPage.setPageCreateTime(new Date());
        cmsPage.setPageId("1234sa5555dada");
        cmsPage.setPageStatus("00888");
        cmsPage.setSiteId("09888888");
        CmsPageParam cmsPageParam = new CmsPageParam();
        cmsPageParam.setPageParamName("lan");
        cmsPageParam.setPageParamValue("lupo");
        List<CmsPageParam> cmsPageParams = new ArrayList<>();
        cmsPageParams.add(cmsPageParam);
        cmsPage.setPageParams(cmsPageParams);
        cmsPageRepository.save(cmsPage);
    }

    //根据页面名称查询
    @Test
    public void testfindByPageName(){
        CmsPage cmsPage = cmsPageRepository.findByPageName("index.html");
        System.out.println(cmsPage);
    }

    @Test
    public void findByPageNameAndPageType(){
        CmsPage cmsPage = cmsPageRepository.findByPageNameAndPageType("index.html","0");
        System.out.println(cmsPage);
    }
////根据页面名称和类型查询
//CmsPage findByPageNameAndPageType(String pageName,String pageType);
    @Test
    public void deletedById(){
        cmsPageRepository.deleteById("1234sa5555dada");
    }

    //自定义查询
    @Test
    public void findAll01(){
        //条件匹配器
        ExampleMatcher matching = ExampleMatcher.matching();
        matching=  matching.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageAliase("课程详情");
       // cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        //创建条件实例
        Example<CmsPage> example = Example.of(cmsPage, matching);
        Pageable pageable =PageRequest.of (0,10);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        System.out.println("结果："+all);
    }


    //查询所有站点id
    @Test
    public void queryBySiteId() {
        List<CmsSite> all = cmsSiteRepository.findAll();
        System.out.println("结果："+all);

    }

    @Test
    public void fun01() {
      List<Object> list = new ArrayList<>();
        QueryBySiteId bySiteId = new QueryBySiteId();
        List<QueryBySiteId> queryBySiteIds = new ArrayList<>();
        for(int i=0;i<4;i++){
            queryBySiteIds.add(bySiteId);
        }
    }

    //自定义查询
    @Test
    public void findAll02(){
        //条件匹配器
        ExampleMatcher matching = ExampleMatcher.matching();
        matching=  matching.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageAliase("课程详情");
       // cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        //创建条件实例
        Example<CmsPage> example = Example.of(cmsPage, matching);
        Pageable pageable =PageRequest.of (0,10);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        System.out.println("结果："+all);
    }



















}
