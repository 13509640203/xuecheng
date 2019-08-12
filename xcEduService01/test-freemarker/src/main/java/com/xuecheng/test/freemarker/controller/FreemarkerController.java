package com.xuecheng.test.freemarker.controller;


import com.xuecheng.test.freemarker.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Controller
@RequestMapping("/freemarker")
public class FreemarkerController {
    @Autowired
    RestTemplate restTemplate;
    @RequestMapping("/test1")
    public String freemarker(Map<String, Object> map){
        map.put("name","lanpo");
        Student stu1 = new Student();
        stu1.setName("小明");
        stu1.setAge(18);
        stu1.setMoney(1000.86f);
        stu1.setBirthday(new Date());

        Student stu2 = new Student();
        stu2.setName("小红");
        stu2.setMoney(200.1f);
        stu2.setAge(19);
        stu2.setBirthday(new Date());
        //加了朋友集合
        List<Student> friends = new ArrayList<>();
        friends.add(stu1);
        //第二个同学有一个朋友
        stu2.setFriends(friends);
        stu2.setBestFriend(stu1);//最好的朋友
        //学生集合两个人
       List<Student> stus = new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);

        //向数据模型放数据 两个
       map.put("stus",stus);
        //准备map数据
        HashMap<String,Student> stuMap = new HashMap<>();
       // stuMap.put("stu1",stu1);
        stuMap.put("stu2",stu2);
       //向数据模型放数据
        map.put("stu1",stu1);
       //向数据模型放数据
        map.put("stuMap",stuMap);
        map.put("friends",friends);
        map.put("point", 102920122);
       //返回模板文件名称
        return "test1";
    }
    @RequestMapping("/banner")
    public String index_banner(Map<String,Object> map){
        //获取页面的dataUrl
       String dataUrl="http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f";
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        map.putAll(body);
        return  "index_banner";
    }

    @RequestMapping("/course")
    public String course(Map<String,Object> map){
        //获取页面的dataUrl
        String dataUrl="http://localhost:31200/course/courseview/4028858162e0bc0a0162e0bfdf1a0000";
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        map.putAll(body);
        return  "course";
    }
}
