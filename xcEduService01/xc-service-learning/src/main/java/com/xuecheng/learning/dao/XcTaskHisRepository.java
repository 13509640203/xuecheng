package com.xuecheng.learning.dao;

import com.xuecheng.framework.domain.task.XcTaskHis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface XcTaskHisRepository extends JpaRepository<XcTaskHis,String> {
    //添加历史记录
    //public XcTaskHis findXcTaskHisById(String id);
}
