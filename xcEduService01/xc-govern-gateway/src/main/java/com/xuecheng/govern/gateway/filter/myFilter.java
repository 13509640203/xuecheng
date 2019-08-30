package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class myFilter extends ZuulFilter {


    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    //过虑器的内容
    //测试的需求：过虑所有请求，判断头部信息是否有Authorization，如果没有则拒绝访问，否则转发到微服务。
    public Object run()  {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        HttpServletResponse response =  currentContext.getResponse();
        //取出头部信息Authorization
        String authorization = request.getHeader("Authorization");
        if(authorization==null){
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseStatusCode(200);
            ResponseResult responseResult = new ResponseResult(CommonCode.LOGIN_FIRST);
            String string = JSON.toJSONString(responseResult);
            currentContext.setResponseBody(string);
            currentContext.getResponse().setContentType("application/json;charset=UTF-8");
            return null;
        }

        return null;
    }
}
