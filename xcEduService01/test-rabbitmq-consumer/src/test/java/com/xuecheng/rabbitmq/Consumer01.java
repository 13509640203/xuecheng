package com.xuecheng.rabbitmq;

import com.rabbitmq.client.*;


import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer01 {

    private static  final String QUEUE="hellolanpo";
    public static void main(String[] args) throws IOException, TimeoutException {
       
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setPort(5672);
           factory.setUsername("guest");
           factory.setPassword("guest");
        //设置虚拟机，一个mq服务可以设置多个虚拟机，每个虚拟机就相当于一个独立的mq
          factory.setVirtualHost("/");
          Connection  connection = factory.newConnection();//创建与rabbitMq连接的TCP连接
            //创建Exchange的通道，每一个；连接可以有多个通道，每一个通道代表一个
            //回话任务
           Channel channel = connection.createChannel();
        /**
             * queueDeclare(String var1, boolean var2, boolean var3, boolean var4, Map<String, Object> var5)
             * 声明队列: 如果Rabbit中没有此队列将自动创建
             * param1:队列名称
             * param2:是否持久化
             * param3:队列是否独占此连接
             * param4:队列不再使用时是否自动删除此队列
             * param5:队列参数
             */
          channel.queueDeclare(QUEUE, true, false, false, null);
         //定义消费者
        DefaultConsumer consumer = new DefaultConsumer(channel){

            /**
             * 当接收到消息后此方法将被调用
             * @param consumerTag  消费者标签，用来标识消费者的，
             *    在监听队列时设置channel.basicConsume
             * @param envelope 信封，通过envelope
             * @param properties 消息属性
             * @param body 消息内容
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
            AMQP.BasicProperties properties, byte[] body) throws IOException {
             //交换机
                String exchange = envelope.getExchange();
                //消息id，mq在channel 中用来标识的消息，用来确认消息已经接收
                long deliveryTag = envelope.getDeliveryTag();
                //消息内容
                String message = new String(body, "utf-8");
                System.out.println("receive message= "+message);
            }
        };
        //监听队列
        //参数：String queue, boolean autoAck, Consumer callback
        /**
         * 参数明细：
         * 1、queue 队列名称
         * 2、autoAck 自动回复，当消费者接收到消息后要告诉mq消息已接收，
         * 如果将此参数设置为tru表示会自动回复mq，如果设置为false要通过编程实现回复
         * 3、callback，消费方法，当消费者接收到消息要执行的方法
         */
         channel.basicConsume(QUEUE,true,consumer);
    }

}
