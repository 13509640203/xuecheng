package com.xuecheng.manage_course.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.event.TreeSelectionEvent;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

@SpringBootTest
@RunWith(SpringRunner.class)
public class HashMapTest {
    @Test
    public void HashMapTest1() throws InterruptedException {
         Map<String, Integer> map = new HashMap<>();
      //  Map<String, Integer> map = new ConcurrentHashMap<>();
        //线程T-1
        new Thread(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getName());

            for (int i = 1; i < 500; i++) {
                map.put("a" + i, i);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "T-1").start();
        //线程T-2
        new Thread(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getName());

            for (int i = 500; i <= 1000; i++) {
                map.put("a" + i, i);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "T-2").start();

        //线程T-3
        new Thread(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getName());

            for (int i = 500; i <= 1000; i++) {
                if (map.containsKey("a120")) {
                    map.put("a120", 888);
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "T-3").start();
        Thread.sleep(1000);
        System.out.println("a120= "+map.get("a120"));
        System.out.println("a130= "+map.get("a130"));
        System.out.println("大小：" + map.size());
    }

    @Test
    public void TreeMapTest1()  {
      Map<Integer, Integer> map = new TreeMap<>();
      map.put(3,8);
      map.put(2,7);
      map.put(1,9);
      map.put(4,6);
     System.out.println("map= "+map);
    }

    @Test
    public void TreeMapTest2()  {
        Map<Integer, String> map = new TreeMap<>();
        map.put(3,"8");
        map.put(2,"7");
        map.put(1,"9");
        map.put(4,"6");
        System.out.println("map= "+map);
    }

    @Test
    public void TreeMapTest3()  {
        Map<String, Integer> map = new TreeMap<>();
        map.put("g",4);
        map.put("b",1);
        map.put("f",2);
        map.put("k",3);

        System.out.println("map= "+map);
    }

    @Test
    public void TreeMapTest4()  {
        Map<String, String> map = new TreeMap<>();
        map.put("g","c");
        map.put("b","a");
        map.put("f","d");
        map.put("k","b");
        map.put("k","9999");
        System.out.println("map= "+map);
    }

    @Test
    public void TreeMapTest5() throws InterruptedException {
        //Map<String, Integer> map = new TreeMap<>();
       Map<String, Integer> map = new ConcurrentSkipListMap<>();
        //线程T-1
        new Thread(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getName());

            for (int i = 1; i <500; i++) {
                map.put("a" + i, i);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "T-1").start();

        //线程T-2
        new Thread(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getName());

            for (int i = 500; i <=1000; i++) {
                map.put("a" + i, i);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "T-2").start();
        //线程T-3
        new Thread(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getName());

            for (int i = 500; i <=1000; i++) {
               if(map.containsKey("a100")){
                   map.replace("a100",888);
               }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "T-3").start();
       Thread.sleep(1000);
        System.out.println("a100= "+map.get("a100"));
        System.out.println("大小：" + map.size());
    }

    @Test
    public void LinkHashMapTest1()  {
      Map<String, Object> map = new LinkedHashMap<>();
      map.put("name","洛少");
      map.put("age",18);
      map.put("adress","斗破大陆");
      map.put(null,null);
      map.put(null,1);
      map.put("test",null);
      System.out.println("map= "+map);
    }

    @Test
    public void LinkHashMapTest2() throws InterruptedException {
        //Map<String, Object> map = new LinkedHashMap<>();
        Map<String, Object> map = Collections.synchronizedMap(new LinkedHashMap<>());

        //线程T-1
        new Thread(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getName());

            for (int i = 1; i < 500; i++) {
                map.put("a" + i, i);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "T-1").start();
        //线程T-2
        new Thread(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getName());

            for (int i = 500; i <= 1000; i++) {
                map.put("a" + i, i);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "T-2").start();

        //线程T-3
        new Thread(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getName());

            for (int i = 500; i <= 1000; i++) {
                if (map.containsKey("a120")) {
                    map.put("a120", 888);
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "T-3").start();
        Thread.sleep(1000);
        System.out.println("a120= "+map.get("a120"));
        System.out.println("大小：" + map.size());
    }
    @Test
    public void HashSetTest1() {
        HashSet<String> set = new HashSet<>();
        set.add("照子龙");
        set.add("官域");
        set.add("草草");
        set.add("流被");
        set.add("流被2");
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()){
            String next = iterator.next();
            if(next=="流被"){
                iterator.remove();
            }
            System.out.println("结果："+next);
        }
        System.out.println("大小："+set.size());
    }
    @Test
    public void HashSetTest2() throws InterruptedException {
       HashSet<String> set = new HashSet<>();
        //Set<String> set  = Collections.synchronizedSet(new HashSet<>());

        //线程T-1
        new Thread(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getName());

            for (int i = 1; i <=500; i++) {
               set.add("a"+i);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "T-1").start();
        //线程T-2
        new Thread(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getName());

            for (int i = 495; i <= 1000; i++) {
                set.add("a"+i);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "T-2").start();


        Thread.sleep(1000);

        System.out.println("大小：" + set.size());
    }

    @Test
    public void LinkSetTest1() throws InterruptedException {
        //LinkedHashSet<String> set = new LinkedHashSet<>();
        Set<String> set  = Collections.synchronizedSet(new LinkedHashSet<>());

        //线程T-1
        new Thread(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getName());

            for (int i = 1; i <=500; i++) {
                set.add("a"+i);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "T-1").start();
        //线程T-2
        new Thread(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getName());

            for (int i = 495; i <= 1000; i++) {
                set.add("a"+i);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "T-2").start();

        Thread.sleep(1000);

        System.out.println("大小：" + set.size());
    }
    @Test
    public void LinkedHashSet() {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        set.add("照子龙");
        set.add("官域");
        set.add("草草");
        set.add("流被");
        set.add("流被2");
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()){
            String next = iterator.next();
            if(next=="流被"){
                iterator.remove();
            }
            System.out.println("结果："+next);
        }
        System.out.println("大小："+set.size());
    }

    @Test
    public void TreeSet() {
        TreeSet<String> set = new TreeSet<>();
        set.add("照子龙");
        set.add("官域");
        set.add("草草");
        set.add("流被");
        set.add("流被2");
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()){
            String next = iterator.next();
            if(next=="流被"){
                iterator.remove();
            }
            System.out.println("结果："+next);
        }
        System.out.println("大小："+set.size());
    }

    @Test
    public void TreeSet2() {
        TreeSet<String> set = new TreeSet<>();
        set.add("G");
        set.add("B");
        set.add("C");
        set.add("A");
        set.add("D");
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()){
            String next = iterator.next();

            System.out.println("结果："+next);
        }
        System.out.println("大小："+set.size());
    }

    @Test
    public void TreeSet1() throws InterruptedException {
       // TreeSet<String> set = new TreeSet<>();
       // ConcurrentSkipListSet<String> set = new ConcurrentSkipListSet<>();
        Set<String> set = Collections.synchronizedSet(new TreeSet<>());
        //线程T-1
        new Thread(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getName());

            for (int i = 1; i < 500; i++) {
                set.add("a"+i);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "T-1").start();
        //线程T-2
        new Thread(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getName());

            for (int i = 498; i <= 1000; i++) {
                set.add("a"+i);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "T-2").start();

        //线程T-3
       /* new Thread(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getName());

            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()){
                String next = iterator.next();
                if("a5".equals(next)){
                    iterator.remove();
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "T-3").start();*/
        Thread.sleep(1000);

        System.out.println("大小：" + set.size());
    }

    @Test
    public void StringBuffer(){
        StringBuilder builder = new StringBuilder();
        builder.append(5);
        builder.append(3);
        builder.append(1);
        builder.append(7);
        System.out.println("builder= "+builder);
    }

    @Test
    public void StringBuffer1(){
        StringBuffer buffer = new StringBuffer("lanpo");
        buffer.append("zui");
        buffer.append("ai");
        buffer.append("chi");
        buffer.append("rou");
       // StringBuffer reverse = buffer.reverse(); sd34344
       // StringBuffer delete = buffer.delete(5, 7);
       // buffer.insert(3,8);//buffer=lanpozuiaichirou
        StringBuffer bu = buffer.replace(5, 8, "bu");
        System.out.println("bu= "+bu);
    }
}
