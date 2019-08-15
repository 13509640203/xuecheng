package com.xuecheng.search;


import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.rest.RestStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestIndex {

    @Autowired
    RestHighLevelClient restHighLevelClient;
    @Autowired
    RestClient restClient;

    //创建索引
    @Test
    public  void createIndexTest() throws IOException {

        //创建索引
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("xc_course");
        //设置参数
        createIndexRequest.settings(Settings.builder().put("number_of_shards",1).put("number_of_replicas",0));
        createIndexRequest.mapping("doc","{\n" +
                " \t\"properties\": {\n" +
                " \"name\": {\n" +
                " \"type\": \"text\",\n" +
                " \"analyzer\":\"ik_max_word\",\n" +
                " \"search_analyzer\":\"ik_smart\"\n" +
                " },\n" +
                " \"description\": {\n" +
                " \"type\": \"text\",\n" +
                " \"analyzer\":\"ik_max_word\",\n" +
                " \"search_analyzer\":\"ik_smart\"\n" +
                " },\n" +
                " \"studymodel\": {\n" +
                " \"type\": \"keyword\"\n" +
                " },\n" +
                " \"price\": {\n" +
                " \"type\": \"float\"\n" +
                " }\n" +
                " }\n" +
                "}", XContentType.JSON);
        //操作索引的客户端
        IndicesClient indices = restHighLevelClient.indices();
        //执行创建操作
        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest);
        boolean acknowledged = createIndexResponse.isAcknowledged();
        System.out.println("acknowledged= "+acknowledged);

    }
    
    //添加文档
    @Test
    public  void  addContext() throws IOException {
        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("name","spring cloud实战");
        hashMap.put("description","本课程主要从四个章节进行讲解： 1.微服务架构入门 2.spring cloud\n" +
                "基础入门 3.实战Spring Boot 4.注册中心eureka。");
        hashMap.put("studymodel","201001");
        //没有这个字段就会添加
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        hashMap.put("timestamp",format.format(new Date()));
        hashMap.put("price", 5.6f);
        //索引请求对象
        IndexRequest indexRequest = new IndexRequest("xc_course","doc","efNIj2wBVXWreJp80DPV");
        indexRequest.source(hashMap);
        //
        IndexResponse index = restHighLevelClient.index(indexRequest);
        DocWriteResponse.Result result = index.getResult();
        System.out.println("result= "+result);
    }

    //查询文档
    @Test
    public void getContet() throws IOException {
        GetRequest getRequest = new GetRequest("xc_course", "doc", "efNIj2wBVXWreJp80DPV");
        GetResponse getResponse = restHighLevelClient.get(getRequest);
        DocumentField id = getResponse.getField("id");
        String index = getResponse.getIndex();
        Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
        Map<String, DocumentField> fields = getResponse.getFields();
        String sourceAsString = getResponse.getSourceAsString();
        System.out.println("id= "+id);
        System.out.println("index= "+index);
        System.out.println("sourceAsMap= "+sourceAsMap);
        System.out.println("fields= "+fields);
        System.out.println("sourceAsString= "+sourceAsString);

    }

    //更新文档
    @Test
    public void updateDoc() throws IOException {

        UpdateRequest updateRequest = new UpdateRequest("xc_course", "doc", "efNIj2wBVXWreJp80DPV");
       Map<String, Object> map = new HashMap<>();
       map.put("name","人工智能");
        updateRequest.doc(map);
        UpdateResponse update = restHighLevelClient.update(updateRequest);
        GetResult getResult = update.getGetResult();
        System.out.println("getResult= "+getResult);

    }

    //删除文档

    //根据id删除文档
    @Test
    public void testDelDoc() throws IOException {
//删除文档id
        String id = "efNIj2wBVXWreJp80DPV";
//删除索引请求对象
        DeleteRequest deleteRequest = new DeleteRequest("xc_course","doc",id);
//响应对象
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest);
//获取响应结果
        DocWriteResponse.Result result = deleteResponse.getResult();
        System.out.println(result);
    }

    //删除索引
    @Test
    public  void deleteIndexTest() throws IOException {

        //删除索引对象
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("xc_course");
        //操作索引的客户端
        IndicesClient indices = restHighLevelClient.indices();
        //执行删除操作
        DeleteIndexResponse delete = indices.delete(deleteIndexRequest);
        boolean acknowledged = delete.isAcknowledged();
        System.out.println("acknowledged= "+acknowledged);

    }
}
