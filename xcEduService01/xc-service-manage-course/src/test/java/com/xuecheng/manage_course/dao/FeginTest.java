package com.xuecheng.manage_course.dao;


import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_course.client.CmsPageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class FeginTest {
    @Autowired
    CmsPageClient  cmsPageClient;

    @Test
    public void testCourseBaseRepository() {
        CmsPage byId = cmsPageClient.findById("5ad92f5468db52404cad0f7c");
     System.out.println("byI= "+byId);
    }
}
