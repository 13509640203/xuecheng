package com.xuecheng.manage_cms_client.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms_client.service.PageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

@Controller
public class CmsPagePreviewController extends BaseController {
    @Autowired
   private PageService pageService;
    @GetMapping("/cms/preview/{pageId}")
    public void preview(@PathVariable("pageId") String pageId){
        String pageHtml = pageService.getPageHtml(pageId);
        if(StringUtils.isNotEmpty(pageHtml)){
            try {
                ServletOutputStream outputStream = response.getOutputStream();
                response.setHeader("Content-type","text/html;charset=utf-8");
                outputStream.write(pageHtml.getBytes("utf-8"));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
