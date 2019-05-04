package com.xuecheng.api.filesystem;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;
@Api(value = "文件管理系统",description = "图片文件上传",tags = {"图片文件上传"})
public interface FileSystemControllerApi {

    /**
     * 上传文件
     * multipartFile 文件
     * filetag 文件标签
     * businesskey 业务key
     * metedata 元信息,json格式
     *
     */
    @ApiOperation("文件上传")
    public UploadFileResult upload(MultipartFile multipartFile,
                                   String filetag,String businesskey,String metedata);



}
