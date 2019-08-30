package com.xuecheng.auth;


import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.client.XcServiceList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestClient {
    @Autowired
    RestTemplate restTemplate;
   @Autowired
    LoadBalancerClient loadBalancerClient;
    @Test
    public  void testClient(){
        //http://localhost:40400/auth/oauth/token
     //采用客户端父子啊均衡，从注册中心获取认证服务的ip和端口
        ServiceInstance serviceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
       // http://localhost:40400/
        URI uri = serviceInstance.getUri();//这url就是申请令牌的url
        String authUrl=uri+"/auth/oauth/token";
        //URI url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType
        // url就是 申请令牌的url /oauth/token
       //method http的方法类型
       //requestEntity请求内容
      //responseType，将响应的结果生成的类型
        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        //header
        String httpBais = getHttpBais("XcWebApp", "XcWebApp");
        header.add("Authorization",httpBais);
        //body
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username","itcast");
        body.add("password","1236");
        HttpEntity<MultiValueMap<String, String>> multiValueMapHttpEntity = new HttpEntity<>(body, header);
        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, multiValueMapHttpEntity, Map.class);
        //指定 restTemplate当遇到400或401响应时候也不要抛出异常，也要正常返回值
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode()!=400&&response.getRawStatusCode()!=401) {
                    super.handleError(response);
                }
            }
        });
        Map bodyMap = exchange.getBody();
        System.out.println("bodyMap= "+bodyMap);
    }

    private String getHttpBais(String clientId,String clientSecret){//客户端id 和密码
     //将客户端id和客户端密码按照“客户端ID:客户端密码”的格式拼接，并用base64编
     String string =clientId+":"+clientSecret;
        byte[] encode = Base64Utils.encode(string.getBytes());
        return  "Basic "+new String(encode);
    }

    @Test
    public  void testPasswrodEncoder(){
       String password="111111";
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        for(int i=0;i<10;i++){
            String encode = bCryptPasswordEncoder.encode(password);
            System.out.println("第"+i+"encode= "+encode);
            //效验
            boolean matches = bCryptPasswordEncoder.matches(password, encode);
            System.out.println("第"+i+"matches= "+matches);
        }
    }
}
