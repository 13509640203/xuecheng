package com.xuecheng.rabbitmq.mq;

import com.xuecheng.rabbitmq.config.RabbitmqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import sun.applet.Main;

@Component
public class ReceiveHandler {
    //监听email队列
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_EMAIL})
    public  void reces_email(String msg,Message Message){
     System.out.println("email: "+msg);
    }

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_SMS})
    public  void reces_sms(String msg,Message Message){
        System.out.println("sms: "+msg);
    }

}
