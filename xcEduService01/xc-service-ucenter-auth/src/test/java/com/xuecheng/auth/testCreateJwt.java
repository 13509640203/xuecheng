package com.xuecheng.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest()
@RunWith(SpringRunner.class)

public class testCreateJwt {

    @Test
    public void createJwt(){
       //证书文件
        String key_location="xc.keystore";
        //秘钥库密码
        String keyStore_password="xuechengkeystore";
        //访问证书路径
        ClassPathResource classPathResource = new ClassPathResource(key_location);
        //秘钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource, keyStore_password.toCharArray());
        //秘钥的密码
        String keypassword = "xuecheng";
        //密钥别名
        String alias = "xckey";
        //秘钥对  秘钥和公钥
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keypassword.toCharArray());
         //拿到私钥
        RSAPrivateKey aPrivate = (RSAPrivateKey)keyPair.getPrivate();

        //定义payload信息
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("id", "1");
        tokenMap.put("name", "itcast");
        tokenMap.put("roles", "r01,r02");
        tokenMap.put("ext", "1");
        //生成jwt令牌
        Jwt encode = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(aPrivate));
        //取出令牌
        String token = encode.getEncoded();
        System.out.println("token= "+token);
    }
}
