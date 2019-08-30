package com.xuecheng.learning.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.learning.config.RabbitMQConfig;
import com.xuecheng.learning.service.AddCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class ChooseCourseTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChooseCourseTask.class);

    @Autowired
    AddCourseService addCourseService;
    @Autowired
    RabbitTemplate rabbitTemplate;
    //接收消息
    @RabbitListener(queues = RabbitMQConfig.XC_LEARNING_ADDCHOOSECOURSE)
    public  void reviceChooseCourseTask(XcTask xcTask){
        System.out.println("learn服务接收到消息了。。。");
        String requestBody = xcTask.getRequestBody();
        Map map = JSON.parseObject(requestBody, Map.class);
        //{"userId":"49","courseId":"4028e581617f945f01617f9dabc40000"}
        String  courseId = (String) map.get("courseId");
        String  userId = (String) map.get("userId");
        ResponseResult responseResult = addCourseService.addCourse(userId, courseId, null, new Date(), null, xcTask);
        if(responseResult.isSuccess()){//成功给order服务恢复消息吧
            rabbitTemplate.convertAndSend(RabbitMQConfig.EX_LEARNING_ADDCHOOSECOURSE
                    ,RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE_KEY,xcTask );

        }
    }
}
