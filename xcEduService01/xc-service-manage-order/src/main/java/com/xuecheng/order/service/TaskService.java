package com.xuecheng.order.service;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.order.dao.XcTaskHisRepository;
import com.xuecheng.order.dao.XcTaskRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    XcTaskRepository xcTaskRepository;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    XcTaskHisRepository xcTaskHisRepository;
  //取出q前n条任务
    public List<XcTask> findTaskList(Date updeTime, int size ){
        Pageable pageable = new PageRequest(0, size);
        Page<XcTask> byUpdateTimeBefore = xcTaskRepository.findByUpdateTimeBefore(pageable, updeTime);
        List<XcTask> content = byUpdateTimeBefore.getContent();
        return  content;
    }

    //发布消息
   //@Transactional
    public void publish(XcTask xcTask,String ex,String routingKey){
        Optional<XcTask> byId = xcTaskRepository.findById(xcTask.getId());
        if(byId.isPresent()){
            rabbitTemplate.convertAndSend(ex,routingKey,xcTask);
            //发送消息之后更新
            XcTask xcTask1 = byId.get();
            xcTask1.setUpdateTime(new Date());
            xcTaskRepository.save(xcTask1);
        }

    }
    @Transactional
    public int updateTaskVersion(XcTask xcTask){
        int i = xcTaskRepository.updateTaskVersion(xcTask.getId(), xcTask.getVersion());
        return  i;
    }

    //接收到添加课程成功之后删除课程任务XcTask  同时将XcTask 添加到XcTaskHis
    @Transactional
    public void reviceLearn(String taskId){
        Optional<XcTask> byId = xcTaskRepository.findById(taskId);
        if(byId.isPresent()){
            XcTask xcTask = byId.get();
            XcTaskHis xcTaskHis = new XcTaskHis();
            BeanUtils.copyProperties(xcTask,xcTaskHis);
            xcTaskRepository.delete(xcTask);
            xcTaskHisRepository.save(xcTaskHis);
        }
    }
}
