package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
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
    //添加课程信息
    @Override
    @PostMapping("/coursebase/add")
    public ResponseResult addCourseBase(@RequestBody CourseBase courseBase) {
        return courseService.addCourseBase(courseBase);
    }

    @Override
    @GetMapping("/courseBase/list/{courseId}")
    public CourseBase findCourseBaseByCourseId(@PathVariable("courseId") String courseId) {
        return courseService.findCourseBaseByCourseId(courseId);
    }

    @Override
    //通过课程id查询课程营销
    @GetMapping("/getCourseMarketById/{courseId}")
    public CourseMarket getCourseMarketById(@PathVariable("courseId") String courseId) {
        System.out.println("查询了。getCourseMarketById。");
        return courseService.getCourseMarketById(courseId);
    }

    @Override
//通过课程id更新课程营销
    @PutMapping("/updateCourseMarket/{courseId}")
    public ResponseResult updateCourseMarket(@PathVariable("courseId") String courseId, @RequestBody CourseMarket courseMarket) {
        System.out.println("更新了。getCourseMarketById。");
        return courseService.updateCourseMarket(courseId,courseMarket);
    }

    //更新基础信息回显
    @PutMapping("/updateByCourseId/{courseId}")
    public ResponseResult updateByCourseId(@RequestBody CourseBase courseBase ,@PathVariable("courseId") String courseId){
        return courseService.updateByCourseId(courseBase,courseId);
    }
}
