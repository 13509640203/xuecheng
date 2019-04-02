package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.CustomException;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//捕获类异常
@ControllerAdvice
public class ExceptionCatch {

    private final  static Logger LOGGER = LoggerFactory.getLogger(ExceptionCast.class);
      //使用EXCEPTIONS存放异常类型和错误代码的映射，
      // ImmutableMap的特点的一旦创建不可改变，并且线程安全
    private static ImmutableMap<Class<? extends Throwable>,ResultCode> EXCEPTIONS;
    //使用builder来构建一个异常类型和错误代码的异常
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder =
            ImmutableMap.builder();

    //捕获CustomExcetion
   @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customExcetion(CustomException e){//自定义异常
        LOGGER.error("catch exception : {}\\r\\nexception:",e.getMessage(),e);
        ResultCode resultCode = e.getResultCode();
        ResponseResult responseResult = new ResponseResult(resultCode);
        return responseResult;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e){//系统异常
        LOGGER.error("catch exception : {}\r\nexception: ",e.getMessage(), e);
        if(EXCEPTIONS == null)//EXCEPTIONS构建成功，且不可更改
            EXCEPTIONS = builder.build();
        //从EXCEPTIONS异常类型中找到对应的错误代码，
        //找到了响应给用户，找不到返回9999
        final ResultCode resultCode = EXCEPTIONS.get(e.getClass());
        final ResponseResult responseResult;
        if (resultCode != null) {
            responseResult = new ResponseResult(resultCode);
        } else {
            responseResult = new ResponseResult(CommonCode.SERVER_ERROR);
        }
        return responseResult;
    }
    static {//在这里加入一些基础的异常类型判断
        builder.put(HttpMessageNotReadableException.class,CommonCode.INVALID_PARAM);
    }
}
