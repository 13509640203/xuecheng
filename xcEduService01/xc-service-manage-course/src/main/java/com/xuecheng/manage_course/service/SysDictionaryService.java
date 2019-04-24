package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_course.dao.SysDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysDictionaryService {
    @Autowired
    SysDictionaryRepository sysDictionaryRepository;
    //课程等级
    public SysDictionary findCourseGrade(String type){
        SysDictionary byDType = sysDictionaryRepository.findSysDictionaryByDType(type);
        return  byDType;
    }

}
