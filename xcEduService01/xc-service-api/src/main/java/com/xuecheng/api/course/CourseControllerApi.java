package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="课程管理接口",description = "课程管理接口，专门为课程各种服务的接口")
public interface CourseControllerApi {
    @ApiOperation("查询课程计划")
    public TeachplanNode findCourseList(String courseId);
    @ApiOperation("添加课程计划")
    public ResponseResult addCourseTeachplan(Teachplan teachplan);
}
