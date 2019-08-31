package com.xuecheng.learning.service;

import com.xuecheng.framework.domain.learning.XcLearningCourse;
import com.xuecheng.framework.domain.learning.respones.LearningCode;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.learning.dao.XcLearningCourseRepository;
import com.xuecheng.learning.dao.XcTaskHisRepository;
import net.bytebuddy.asm.Advice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class AddCourseService {
   @Autowired
    XcLearningCourseRepository xcLearningCourseRepository;
   @Autowired
    XcTaskHisRepository xcTaskHisRepository;
      //开启事务课程添加和任务历史记录同时生效
    @Transactional
    public ResponseResult addCourse(String userId, String courseId, String valid, Date
            startTime, Date endTime, XcTask xcTask){
           if(StringUtils.isEmpty(userId)){
               ExceptionCast.cast(LearningCode.LEARNING_UserId_NULL);
           }
          if(StringUtils.isEmpty(courseId)){
            ExceptionCast.cast(LearningCode.LEARNING_CourseId_NULL);
           }
           int m=7;
            int j=11;         //sdadkoadk
         //dadkald
       //dasdka 
       //山东矿机爱可登adkkjad
       //到达adkf
        XcLearningCourse xcLearningCourse = xcLearningCourseRepository.findXcLearningCourseByCourseIdAndUserId(courseId, userId);
       if(xcLearningCourse!=null){//更新
           xcLearningCourse.setStartTime(startTime);
           xcLearningCourse.setEndTime(endTime);
           xcLearningCourse.setStatus("501001");
           xcLearningCourse.setValid(valid);
           xcLearningCourseRepository.save(xcLearningCourse);
       }else {//添加课程
           XcLearningCourse xcLearningCourse1 = new XcLearningCourse();
           xcLearningCourse1.setCourseId(courseId);
           xcLearningCourse1.setUserId(userId);
           xcLearningCourse1.setStartTime(startTime);
           xcLearningCourse1.setEndTime(endTime);
           xcLearningCourse1.setStatus("501001");
           xcLearningCourse1.setValid(valid);
           xcLearningCourseRepository.save(xcLearningCourse1);
       }

        Optional<XcTaskHis> byId = xcTaskHisRepository.findById(xcTask.getId());
           if(!byId.isPresent()){
               XcTaskHis taskHis = new XcTaskHis();
               taskHis.setCreateTime(xcTask.getCreateTime());
               taskHis.setDeleteTime(xcTask.getDeleteTime());
               taskHis.setMqExchange(xcTask.getMqExchange());
               taskHis.setMqRoutingkey(xcTask.getMqRoutingkey());
               taskHis.setRequestBody(xcTask.getRequestBody());
               taskHis.setStatus(xcTask.getStatus());
               taskHis.setTaskType(xcTask.getTaskType());
               taskHis.setUpdateTime(xcTask.getUpdateTime());
               taskHis.setVersion(xcTask.getVersion());
               taskHis.setId(xcTask.getId());
               xcTaskHisRepository.save(taskHis);
           }

        return  new ResponseResult(CommonCode.SUCCESS);
    }

}
