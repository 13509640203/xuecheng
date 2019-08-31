package com.xuecheng.manage_course.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

@SpringBootTest
@RunWith(SpringRunner.class)
public class HashTest {
    private   int sum=1;
    @Test
    public void ArrayList() {
        List<Integer> aa = new ArrayList<>();

        new Thread(() -> {
            System.out.println("当前线程1：" + Thread.currentThread().getName());
            aa.add(5);
            aa.add(3);
            aa.add(7);
            aa.add(5);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "T-1").start();

        new Thread(() -> {
            System.out.println("当前线程2：" + Thread.currentThread().getName());

            Iterator<Integer> iterator = aa.iterator();
            for (Integer a : aa) {
                if (a == 7) {
                    aa.remove(a);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "T-2").start();
        //  System.out.println("aa= "+aa.size());


        System.out.println("intValue =" + aa.toString());


    }

    @Test
    public void HashSet() {
        Set<String> hashSet = new HashSet<>();
        hashSet.add("aa");
        hashSet.add("bb");
        hashSet.add("cc");
        hashSet.add("aa");
        Iterator<String> iterator = hashSet.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if ("bb".equals(next)) {
                hashSet.remove(next);
            }

        }
        System.out.println("hashSet= " + hashSet.toArray());
    }

    @Test
    public void ArrayListSafeTest2() throws InterruptedException {
        //List<Integer> list = Collections.synchronizedList(new LinkedList<Integer>());
        //List<Integer> list = new ArrayList<>();
       // List<Integer> list = Collections.synchronizedList(new ArrayList<Integer>());
       List<Integer> list = new CopyOnWriteArrayList<>();
        // List<Integer> list = new LinkedList<>();
        // 线程A将1-999添加到列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程A");
                for (int i=1;i<500;i++){
                    list.add(i);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
        // 线程B将1001-2000添加到列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程B");
                for (int i=500;i<=1000;i++){
                    list.add(i);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
        // 线程C修改
        new Thread(new Runnable(){
            @Override
            public void run() {
                System.out.println("线程C:修改");
                for (int i=1;i<=10;i++){
                    if(i==5){
                        list.set(5,8888);
                    }

                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } ).start();

        // 线程D删除
        new Thread(new Runnable(){
            @Override
            public void run() {
                System.out.println("线程D:删除");
                Iterator<Integer> iterator = list.iterator();
                while (iterator.hasNext()){
                    int next = iterator.next();
                    if(next==100){
                        iterator.remove();
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        } ).start();

        Thread.sleep(1000);
        // 打印所有结果
        System.out.println("大小："+list.size());
        for (int i = 0; i < list.size(); i++) {
            System.out.println("第" + (i + 1) + "个元素为：" + list.get(i));

        }
    }

    @Test
    public void ArrayListSafeTest() throws InterruptedException {
       // List<Integer> list = new ArrayList<>();
        // List<Integer> list = Collections.synchronizedList(new ArrayList<Integer>());
        //List<Integer> list = new CopyOnWriteArrayList<>();
      // List<Integer> list = new LinkedList<>();
        ConcurrentLinkedQueue<Integer> list = new ConcurrentLinkedQueue<>();
        // 线程A将1-999添加到列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程A");
                for (int i = 1; i < 1000; i++) {
                    list.offer(i);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
        // 线程B将1001-2000添加到列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程B");
                for (int i = 1000; i <= 2000; i++) {
                    list.offer(i);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
        Thread.sleep(1000);
        // 打印所有结果
        Iterator<Integer> iterator = list.iterator();
        System.out.println("大小： "+list.size());
       /* while (iterator.hasNext()){
            Integer next = iterator.next();
           // System.out.println("next= "+next);
        }*/

    }

    @Test
    public void ForArrayList1() {
        ArrayList<String> list = new ArrayList<String>(3);
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("C");
        list.add("D");
        list.add("E");

        for (int i = 0; i < list.size(); i++) {
            System.out.println(i);
            if(list.get(i).equals("C")){
                list.remove(list.get(i));
                i--;
            }
        }
        System.out.println(list.toString());
    }

    @Test
    public void ForArrayList2() {
        ArrayList<String> list = new ArrayList<String>(3);
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("C");
        list.add("D");
        list.add("E");

        for (int i = list.size()-1; i >=0; i--) {
            System.out.println(i);
            if(list.get(i).equals("C")){
                list.remove(list.get(i));
            }
        }
        System.out.println(list.toString());
    }
    @Test
    public void ForArrayList3() {
        ArrayList<String> list = new ArrayList<String>(3);
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("C");
        list.add("D");
        list.add("E");

        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()){
            String next = iterator.next();
            if("C".equals(next)){
                iterator.remove();
            }
        }
        System.out.println(list.toString());
    }

    @Test
    public void digui1() {//adadjdakd
        int sum = digui2(5);//dakdjad
        System.out.println("阶层结果： "+sum);
        int num = digui3(6);
        System.out.println("阶层结果num： "+num);
        int nodigui = Nodigui(5);
        System.out.println("阶层结果：nodigui "+nodigui);
    }

    public int digui2(int n) {
        if(n>0){
            sum=sum*n;
            n--;
            digui2(n);
        }
        return sum;
    }

    public int digui3(int n) {
        if(n>0) {
            if (n == 1) {
                return 1;
            }
            return digui3(n - 1) * n;
        }
        return -1;
    }
    public int Nodigui(int n) {
        if(n==1){
            return 1;
        }
        if(n==0){
            return 0;
        }
       int sum=1;
        for(int m=n;m>=2;m--){
            sum=sum*m;
        }
        return sum;
    }
}