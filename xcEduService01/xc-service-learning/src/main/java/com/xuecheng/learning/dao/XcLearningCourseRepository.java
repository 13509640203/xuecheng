package com.xuecheng.learning.dao;

import com.xuecheng.framework.domain.learning.XcLearningCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface XcLearningCourseRepository extends JpaRepository<XcLearningCourse,String> {
    //根据用户和课程查询 决定是否更新还是添加
    public  XcLearningCourse findXcLearningCourseByCourseIdAndUserId(String courseId,String UserId);
}
