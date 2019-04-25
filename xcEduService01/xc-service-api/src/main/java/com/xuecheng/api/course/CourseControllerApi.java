package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.engine.jdbc.Size;

@Api(value="课程管理接口",description = "课程管理接口，专门为课程各种服务的接口")
public interface CourseControllerApi {
    @ApiOperation("查询课程计划")
    public TeachplanNode findCourseList(String courseId);
    @ApiOperation("添加课程计划")
    public ResponseResult addCourseTeachplan(Teachplan teachplan);

    @ApiOperation("查询我的课程列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value = "页码",required = true ,paramType = "path",dataType = "int"),
            @ApiImplicitParam(name="size",value = "一页显示数量",required = true ,paramType = "path",dataType = "int")
    })
    public QueryResponseResult findCourseList(int page, int size,
                         CourseListRequest courseListRequest);

    @ApiOperation("添加课程基础信息,")
    public ResponseResult addCourseBase(CourseBase courseBase);
    @ApiOperation("回显课程基础信息,")
    public CourseBase findCourseBaseByCourseId(String courseId);
}
