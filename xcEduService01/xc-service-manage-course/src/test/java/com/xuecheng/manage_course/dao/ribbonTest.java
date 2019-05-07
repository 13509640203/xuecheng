package com.xuecheng.manage_course.dao;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class ribbonTest {
    @Autowired
    RestTemplate restTemplate;

    @Test
    public void testCourseBaseRepository() {
        String serviceId = "xc-service-manage-cms";
        for (int i=0;i<=5;i++) {

            ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://"+serviceId+"/cms/page/get/5ad92f5468db52404cad0f7c", Map.class);
            Map body = forEntity.getBody();
            System.out.println("body= " + body);
        }
    }
}
