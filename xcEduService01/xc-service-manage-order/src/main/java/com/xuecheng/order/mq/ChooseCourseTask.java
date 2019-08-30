package com.xuecheng.order.mq;



import com.alibaba.fastjson.JSON;
import com.sun.jmx.snmp.tasks.TaskServer;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.order.config.RabbitMQConfig;
import com.xuecheng.order.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Component
public class ChooseCourseTask {

    private  static  final Logger LOGGER = LoggerFactory.getLogger(ChooseCourseTask.class);

  @Autowired
  TaskService taskServer;
    @Scheduled(cron="0/3 * * * * *")//每隔3秒执行一次
  public void sendChoosecourseTask(){

      Calendar calendar = new GregorianCalendar();
      calendar.setTime(new Date());
      calendar.add(GregorianCalendar.MINUTE,-1);
      Date time = calendar.getTime();
      List<XcTask> taskList = taskServer.findTaskList(time, 100);
         for(XcTask xcTask:taskList){
             int i = taskServer.updateTaskVersion(xcTask);
             if(i>0){//说明当前线程是第一次更改版本，之后线程无法修改了
                 LOGGER.info("发布消息了。。。");
                 taskServer.publish(xcTask,xcTask.getMqExchange(),xcTask.getMqRoutingkey());
             }

      }
  }

  @RabbitListener(queues = RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE)
  public  void reviceChooseCourse(XcTask xcTask){
      System.out.println("接收到learn服务恢复的消息了");
      if(xcTask!=null&&xcTask.getId()!=null){
          taskServer.reviceLearn(xcTask.getId());
      }
  }



    // @Scheduled(cron = "0/3 * * * * *")
    //@Scheduled(fixedDelay = 3000)//任务结束之后3s再执行
   //@Scheduled(fixedRate = 3000)//任务开始之后3s执行
    public void task1(){
        System.out.println(new Date()+"=========异步任务开始1========");
       try {
           Thread.sleep(5000);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
       System.out.println(new Date()+"=========异步任务结束1========");
    }
    //@Scheduled(fixedRate = 3000)//任务开始之后3s执行
    public void task2(){
        System.out.println(new Date()+"=========异步任务开始2========");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(new Date()+"=========异步任务结束2========");
    }
}
