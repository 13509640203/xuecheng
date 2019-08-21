package com.xuecheng.auth.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.exception.ExceptionCast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    LoadBalancerClient loadBalancerClient;
    @Value("${auth.tokenValiditySeconds}")
    long tokenValiditySeconds;
    public AuthToken login(String username, String password, String clientId, String clientSecret) {

        //申请令牌
        AuthToken authToken = applyToken(username, password, clientId, clientSecret);
        String access_token = authToken.getAccess_token();

        //存贮令牌到redis
        boolean result = saveToken(access_token, authToken, tokenValiditySeconds);
        if(!result){
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_REDIS_ERROR);
        }
        return  authToken;
    }

    //保存到redis
    private boolean saveToken( String access_token,AuthToken authToken,long time ){
        String key ="user_token:"+access_token;
        String jsonString = JSON.toJSONString(authToken);
        redisTemplate.boundValueOps(key).set(jsonString,time, TimeUnit.SECONDS);
        Long expire = redisTemplate.getExpire(key);
        return  expire>0;

    }
     //认证方法
    private  AuthToken applyToken(String username, String password, String clientId, String clientSecret){
        //http://localhost:40400/auth/oauth/token
        //采用客户端父子啊均衡，从注册中心获取认证服务的ip和端口
        ServiceInstance serviceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);

        if(serviceInstance==null){
            LOGGER.error("choose an auth instance fail");
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_AUTHSERVER_NOTFOUND);
        }
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
        String httpBais = getHttpBais(clientId, clientSecret);
        header.add("Authorization",httpBais);
        //body
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username",username);
        body.add("password",password);
        HttpEntity<MultiValueMap<String, String>> multiValueMapHttpEntity = new HttpEntity<>(body, header);
        Map bodyMap =null;
        try {
            ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, multiValueMapHttpEntity, Map.class);
             bodyMap = exchange.getBody();
        }catch (RestClientException e ){
            e.printStackTrace();
            LOGGER.error("request oauth_token_password error: {}",e.getMessage());
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
        }


        //指定 restTemplate当遇到400或401响应时候也不要抛出异常，也要正常返回值
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode()!=400&&response.getRawStatusCode()!=401) {
                    super.handleError(response);
                }
            }
        });

        if(bodyMap==null||bodyMap.get("access_token")==null||bodyMap.get("refresh_token")==null||
        bodyMap.get("jti")==null){
         return  null;
        }

        AuthToken authToken = new AuthToken();
        authToken.setAccess_token((String) bodyMap.get("jti"));
        authToken.setRefresh_token((String) bodyMap.get("refresh_token"));
        authToken.setJwt_token((String) bodyMap.get("access_token"));
        return  authToken;
    }

    private String getHttpBais(String clientId,String clientSecret){//客户端id 和密码
        //将客户端id和客户端密码按照“客户端ID:客户端密码”的格式拼接，并用base64编
        String string =clientId+":"+clientSecret;
        byte[] encode = Base64Utils.encode(string.getBytes());
        return  "Basic "+new String(encode);
    }
}
