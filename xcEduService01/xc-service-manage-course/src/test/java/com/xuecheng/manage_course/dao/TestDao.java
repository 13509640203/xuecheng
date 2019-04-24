package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.system.SysDictionary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.midi.Soundbank;
import java.util.List;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    TeachplanMapper teachplanMapper;
    @Autowired
    SysDictionaryRepository sysDictionaryRepository;
    @Test
    public void testCourseBaseRepository(){
        Optional<CourseBase> optional = courseBaseRepository.findById("402885816240d276016240f7e5000002");
        if(optional.isPresent()){
            CourseBase courseBase = optional.get();
            System.out.println(courseBase);
        }

    }

    @Test
    public void testCourseMapper(){
        CourseBase courseBase = courseMapper.findCourseBaseById("402885816240d276016240f7e5000002");
        System.out.println(courseBase);

    }
    @Test
    public void teachplanMapper(){
        TeachplanNode courseList = teachplanMapper.findCourseList("4028e581617f945f01617f9dabc40000");
        System.out.println(courseList);

    }


    @Test
    public void findCourseMapper(){
        PageHelper.startPage(1,8);
        Page<CourseInfo> courseBases = courseMapper.findCourseList();
        List<CourseInfo> result = courseBases.getResult();
        long total = courseBases.getTotal();
        System.out.println("result: "+result);
        System.out.println("===============");
        System.out.println("total: "+total);
    }

    @Test
    public void findCourseGrade(){
        SysDictionary byDType = sysDictionaryRepository.findSysDictionaryByDType("200");
        System.out.println("byDType: "+byDType);
    }
}
