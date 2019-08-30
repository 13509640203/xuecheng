package com.xuecheng.manage_course.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ThreadTest {
    @Test
    public  void RunnableTest(){
        RunnableDemo runnableDemo1 = new RunnableDemo("Thread-1");
        runnableDemo1.start();
        RunnableDemo runnableDemo2 = new RunnableDemo("Thread-2");
        runnableDemo2.start();
    }

    @Test
    public  void ThreadTest(){
        ThreadDemo t = new ThreadDemo("Thread-1");
        t.start();
        ThreadDemo t2 = new ThreadDemo("Thread-2");
        t2.start();
    }

}
