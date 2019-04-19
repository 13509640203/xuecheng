package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class ConsumerPostPage {
/*
ConsumerPostPage作为
发布页面的消费客户端，监听
页面发布队列的消息，收到消息后从mongodb下载文件，保存在本地。
 */
private  static  final Logger LOGGER = LoggerFactory.getLogger(ConsumerPostPage.class);
 @Autowired
 private PageService pageService;
 @Autowired
 private CmsPageRepository cmsPageRepository;

@RabbitListener(queues = {"${xuecheng.mq.queue}"})
public void postPage(String msg){
    Map map = JSON.parseObject(msg, Map.class);
    LOGGER.info("receive cms post page:{}",msg.toString());
    String  pageId = (String) map.get("pageId");
    Optional<CmsPage> byId = cmsPageRepository.findById(pageId);
   if(byId==null){
       ExceptionCast.cast(CmsCode.CMS_PAGE_ISNULL);
   }
   // CmsPage cmsPage = byId.get();
   pageService.savePageToServicePath(pageId);

}
}
