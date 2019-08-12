package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
@Data
@ToString
@NoArgsConstructor //空参构造
public class CourseView implements Serializable {
 //基礎課程信息
    CourseBase courseBase;
    //课程图片
    CoursePic coursePic;
    //课程营销
    CourseMarket courseMarket;
    //教学计划
    TeachplanNode teachplanNode;

}
