package com.xuecheng.manage_course.dao;

public class ThreadDemo extends Thread {
  private Thread thread;
  private String threadName;//线程名称

    public ThreadDemo(String threadName) {
        this.threadName = threadName;
        System.out.println("create "+threadName);
    }

    @Override
    public void run() {
        System.out.println("running "+threadName);
            //线程睡一会
            try {
                for(int i=1;i<=5;i++) {
                    System.out.println("getCPU：" + threadName + ": " + i);
                    Thread.sleep(5);//让给其他线程
                }

            } catch (InterruptedException e) {
                System.out.println("中断了线程："+threadName);
                e.printStackTrace();
            }
            System.out.println(threadName+" exiting");

    }

    public  void start(){
        System.out.println("starting "+threadName);
        if(thread==null){
            thread = new Thread(this, threadName);
            thread.start();
        }
    }
}
