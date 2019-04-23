package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi{

    @Autowired
    CourseService courseService;

    //课程计划
    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findCourseList(@PathVariable("courseId") String courseId) {
        return courseService.findTeachplanList(courseId);
    }
    //添加课程计划
    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addCourseTeachplan(@RequestBody  Teachplan teachplan) {
        return courseService.addCourseTeachplan(teachplan);
    }

    @Override
    @GetMapping("/coursebase/list/{page}/{size}")
    public QueryResponseResult findCourseList(@PathVariable("page") int page,@PathVariable("size") int size, CourseListRequest courseListRequest) {
        return courseService.findCourseList(page,size,courseListRequest);
    }
}
