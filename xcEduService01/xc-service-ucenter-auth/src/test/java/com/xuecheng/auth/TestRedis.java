package com.xuecheng.auth;


import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedis {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    public  void Rediss(){
        //定义key
        String key = "user_token:3b941d27-6228-4e79-a54a-53a6ecb9a392";
      //定义Map
        Map<String,String> mapValue = new HashMap<>();
        mapValue.put("id","101");
        mapValue.put("username","itcast");
        mapValue.put("jwt","eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6IlhjV2ViQXBwIiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOm51bGwsInV0eXBlIjpudWxsLCJpZCI6bnVsbCwiZXhwIjoxNTY2MzA5OTMyLCJqdGkiOiIzYjk0MWQyNy02MjI4LTRlNzktYTU0YS01M2E2ZWNiOWEzOTIiLCJjbGllbnRfaWQiOiJYY1dlYkFwcCJ9.apCKGUIK3nwY17qgSduslY4YomZ1ICJ74obgZ_Zh6kx0uqmNLASayfW-pv_va7PpfuUxFTWNMRThc4n9SC2XzbiXb2jro5waSpIaUOUu_obAwz9WcrYPm5PRE1Z8jaLSYjllQvQPHtuOgqericoWwjIbCzd4BnGdO_RTpwW67yG2sf8BvhXX8Ss4hljgma_ivR3WfUDEGKDvpHNhk5NJB8859mVp1Rc2BcDxp2-rUlmB4P48tEk2ZNopHp2xHAaHECLDytgTNvv5gREWyZ405NSreKJyDVW05wuvnTJGnqBRhD6mXf2nOYZzn7UJRG6jhtsFHXz6PJlFaw0x_6mAIQ");
        mapValue.put("refresh_token","eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6IlhjV2ViQXBwIiwic2NvcGUiOlsiYXBwIl0sImF0aSI6IjNiOTQxZDI3LTYyMjgtNGU3OS1hNTRhLTUzYTZlY2I5YTM5MiIsIm5hbWUiOm51bGwsInV0eXBlIjpudWxsLCJpZCI6bnVsbCwiZXhwIjoxNTY2MzA5ODU1LCJqdGkiOiIzZGExZWM1MS1jYjA0LTQ3MTMtYTIzNC0zYWZiYjBjMGZlMjQiLCJjbGllbnRfaWQiOiJYY1dlYkFwcCJ9.Uhr_3Xb-rVF3CJVnH-SfaozprtMFtyTOrEp3L_fF4_cqrQtV26ypWxwlW4nBiumzg3LEaxDJ34vSCGTQqpWTdRBd6A0IBRKZQti-lHPKuw98bLr9P2_wSm4FhHXM_yy9dy0efatv_44wCUfo-Puv4xXx9Ra8YBU8BfqVT4f19FXjHKiQl6Xpi-WN19V9bbvDxIGpynjEoi2aLQWLsyAzwAoiC0VU6YLv9CzCYSrbz85L5pWvq6pAbkCtIDBQPYtxiVRC9VtxzQnD1VtNKlEJUb-gfVLOwpbVl7IKVeqQsChsW6W9wR68iVYElbiwmYDQC7oi2TdWKTaPAW8y_JksDQ");
        String value = JSON.toJSONString(mapValue);
       //向redis中存贮字符串
      BoundValueOperations<String, String> stringStringBoundValueOperations = redisTemplate.boundValueOps(key);
       stringStringBoundValueOperations.set(value,200, TimeUnit.SECONDS);
        //读取过期时间 过期返回-2
        Long expire = redisTemplate.getExpire(key);
        System.out.println("expire= "+expire);

        String user_token = redisTemplate.opsForValue().get(key);

       System.out.println("user_token= "+user_token);

    }
}
