package com.xuecheng.search;


import javafx.scene.input.DataFormat;
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
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSearch {

    @Autowired
    RestHighLevelClient restHighLevelClient;
    @Autowired
    RestClient restClient;

    @Test
    public void findSearchAll() throws IOException, ParseException {
       //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //指定类型
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //搜索方式 全部
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //设置原字段过滤 第一个参数 结果集包含哪些字段 第二个 结果集不包含哪些字段
        searchSourceBuilder.fetchSource(new String[] {"name","studymodel","price","timestamp"},new String[] {});
        //向搜索请求对象中 设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索 向es发起http请求
        SearchResponse search = restHighLevelClient.search(searchRequest);
        //搜索结果
        SearchHits hits = search.getHits();
       long hitTotal = hits.getTotalHits();
       //获取得到比较高的匹配数据
        SearchHit[] hits1 = hits.getHits();
        System.out.println("hitTotal ="+hitTotal);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        for (SearchHit hit :hits1){
          String id = hit.getId();
          float score = hit.getScore();
          System.out.println("id= "+id+" score= "+score);
            Map<String, Object> map = hit.getSourceAsMap();
          String name = (String) map.get("name");
          String studymodel = (String) map.get("studymodel");
          Double price = (Double) map.get("price");
          //时间戳
            Date timestamp = simpleDateFormat.parse((String) map.get("timestamp"));
            String pic = (String) map.get("pic");
            System.out.println("id= "+id+" name ="+name);
            System.out.println("id= "+id+" studymodel ="+studymodel);
            System.out.println("id= "+id+" price ="+price);
            System.out.println("id= "+id+" timestamp ="+timestamp);
            //因为上面 设置原字段过滤没有设置商业结果为空
            System.out.println("id= "+id+" pic ="+pic);
      };

    }
     //分页
    @Test
    public void getDocFenYe() throws IOException, ParseException {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //指定类型
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //搜索方式 全部
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //设置原字段过滤 第一个参数 结果集包含哪些字段 第二个 结果集不包含哪些字段
        searchSourceBuilder.fetchSource(new String[] {"name","studymodel","price","timestamp"},new String[] {});
        int page =1;
        int size=2;
        int from =(page-1)*size;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        //向搜索请求对象中 设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索 向es发起http请求
        SearchResponse search = restHighLevelClient.search(searchRequest);
        //搜索结果
        SearchHits hits = search.getHits();
        long hitTotal = hits.getTotalHits();
        //获取得到比较高的匹配数据
        SearchHit[] hits1 = hits.getHits();
        System.out.println("hitTotal ="+hitTotal);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        for (SearchHit hit :hits1){
            String id = hit.getId();
            float score = hit.getScore();
            System.out.println("id= "+id+" score= "+score);
            Map<String, Object> map = hit.getSourceAsMap();
            String name = (String) map.get("name");
            String studymodel = (String) map.get("studymodel");
            Double price = (Double) map.get("price");
            //时间戳
            Date timestamp = simpleDateFormat.parse((String) map.get("timestamp"));
            String pic = (String) map.get("pic");
            System.out.println("id= "+id+" name ="+name);
            System.out.println("id= "+id+" studymodel ="+studymodel);
            System.out.println("id= "+id+" price ="+price);
            System.out.println("id= "+id+" timestamp ="+timestamp);
            //因为上面 设置原字段过滤没有设置商业结果为空
            System.out.println("id= "+id+" pic ="+pic);
        };

    }
    //精确匹配（模糊查询）
    @Test
    public void findTermQuery() throws IOException, ParseException {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //指定类型
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //搜索方式 全部
        //searchSourceBuilder.query(QueryBuilders.matchQuery("name","spring开发"));
        searchSourceBuilder.query(QueryBuilders.matchPhraseQuery("name","spring开发"));
        //searchSourceBuilder.query(QueryBuilders.termQuery("name","spring开发"));
        //设置原字段过滤 第一个参数 结果集包含哪些字段 第二个 结果集不包含哪些字段
        searchSourceBuilder.fetchSource(new String[] {"name","studymodel"},new String[] {});
        //向搜索请求对象中 设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索 向es发起http请求
        SearchResponse search = restHighLevelClient.search(searchRequest);
        //搜索结果
        SearchHits hits = search.getHits();
        long hitTotal = hits.getTotalHits();
        //获取得到比较高的匹配数据
        SearchHit[] hits1 = hits.getHits();
        System.out.println("hitTotal ="+hitTotal);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        for (SearchHit hit :hits1){
            String id = hit.getId();
            float score = hit.getScore();
            System.out.println("id= "+id+" score= "+score);
            Map<String, Object> map = hit.getSourceAsMap();
            String name = (String) map.get("name");
            String studymodel = (String) map.get("studymodel");
            Double price = (Double) map.get("price");
            //时间戳

            String pic = (String) map.get("pic");
            System.out.println("id= "+id+" name ="+name);
            System.out.println("id= "+id+" studymodel ="+studymodel);
            System.out.println("id= "+id+" price ="+price);

            //因为上面 设置原字段过滤没有设置商业结果为空
            System.out.println("id= "+id+" pic ="+pic);
        };
    }

    @Test
    public void findById() throws IOException {//根据id精确匹配
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //指定类型
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //搜索方式 全部
        //searchSourceBuilder.query(QueryBuilders.matchQuery("name","spring开发"));
        String[] idList ={"1","2"};
        //将String数组变成List集合
        List<String> ids = Arrays.asList(idList);
        searchSourceBuilder.query(QueryBuilders.termsQuery("_id",ids));

        //设置原字段过滤 第一个参数 结果集包含哪些字段 第二个 结果集不包含哪些字段
        searchSourceBuilder.fetchSource(new String[] {"name","studymodel"},new String[] {});
        //向搜索请求对象中 设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索 向es发起http请求
        SearchResponse search = restHighLevelClient.search(searchRequest);
        //搜索结果
        SearchHits hits = search.getHits();
        long hitTotal = hits.getTotalHits();
        //获取得到比较高的匹配数据
        SearchHit[] hits1 = hits.getHits();
        System.out.println("hitTotal ="+hitTotal);

        for (SearchHit hit :hits1){
            String id = hit.getId();
            float score = hit.getScore();
            System.out.println("id= "+id+" score= "+score);
            Map<String, Object> map = hit.getSourceAsMap();
            String name = (String) map.get("name");
            String studymodel = (String) map.get("studymodel");
            Double price = (Double) map.get("price");
            //时间戳

            System.out.println("id= "+id+" name ="+name);
            System.out.println("id= "+id+" studymodel ="+studymodel);
            System.out.println("id= "+id+" price ="+price);


        };
    }
    //match Query
    @Test
    public void getMatchQuery() throws IOException {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //指定类型
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //搜索方式 全部
       // searchSourceBuilder.query(QueryBuilders.matchQuery("name","spring开发").operator(Operator.OR));
        searchSourceBuilder.query(QueryBuilders.matchQuery("description","spring开发框架").minimumShouldMatch("80%"));
        //设置原字段过滤 第一个参数 结果集包含哪些字段 第二个 结果集不包含哪些字段
        searchSourceBuilder.fetchSource(new String[] {"name","studymodel"},new String[] {});
        //向搜索请求对象中 设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索 向es发起http请求
        SearchResponse search = restHighLevelClient.search(searchRequest);
        //搜索结果
        SearchHits hits = search.getHits();
        long hitTotal = hits.getTotalHits();
        //获取得到比较高的匹配数据
        SearchHit[] hits1 = hits.getHits();
        System.out.println("hitTotal ="+hitTotal);

        for (SearchHit hit :hits1){
            String id = hit.getId();
            float score = hit.getScore();
            System.out.println("id= "+id+" score= "+score);
            Map<String, Object> map = hit.getSourceAsMap();
            String name = (String) map.get("name");
            String studymodel = (String) map.get("studymodel");
            Double price = (Double) map.get("price");
            //时间戳

            System.out.println("id= "+id+" name ="+name);
            System.out.println("id= "+id+" studymodel ="+studymodel);
            System.out.println("id= "+id+" price ="+price);


        };
    }
    //匹配多个字段查
    @Test
    public void getMultiQuery() throws IOException {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //指定类型
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //搜索方式 全部

        searchSourceBuilder.query(QueryBuilders.multiMatchQuery("spring框架","name","description")
        .minimumShouldMatch("50%")
         .field("name",10)//提升boost
        );
        //设置原字段过滤 第一个参数 结果集包含哪些字段 第二个 结果集不包含哪些字段
        searchSourceBuilder.fetchSource(new String[] {"name","description"},new String[] {});
        //向搜索请求对象中 设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索 向es发起http请求
        SearchResponse search = restHighLevelClient.search(searchRequest);
        //搜索结果
        SearchHits hits = search.getHits();
        long hitTotal = hits.getTotalHits();
        //获取得到比较高的匹配数据
        SearchHit[] hits1 = hits.getHits();
        System.out.println("hitTotal ="+hitTotal);

        for (SearchHit hit :hits1){
            String id = hit.getId();
            float score = hit.getScore();
            System.out.println("id= "+id+" score= "+score);
            Map<String, Object> map = hit.getSourceAsMap();
            String name = (String) map.get("name");
            String description = (String) map.get("description");
            Double price = (Double) map.get("price");
            //时间戳

            System.out.println("id= "+id+" name ="+name);
            System.out.println("id= "+id+" description ="+description);
            System.out.println("id= "+id+" price ="+price);


        };
    }

    //不尔查询
    @Test
    public void getBoolQuery() throws IOException {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //指定类型
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //设置原字段过滤 第一个参数 结果集包含哪些字段 第二个 结果集不包含哪些字段
        searchSourceBuilder.fetchSource(new String[] {"name","studymodel"},new String[] {});
        //搜索方式 全部
        //multiQuery
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring框架", "name", "description")
                .minimumShouldMatch("50%")
                .field("name", 10);
        //TermQuery
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("studymodel", "201001");
        //不尔查询   以上两种查询下再查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(multiMatchQueryBuilder).must(termQueryBuilder);

        searchSourceBuilder.query(boolQueryBuilder);
        //向搜索请求对象中 设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索 向es发起http请求
        SearchResponse search = restHighLevelClient.search(searchRequest);
        //搜索结果
        SearchHits hits = search.getHits();
        long hitTotal = hits.getTotalHits();
        //获取得到比较高的匹配数据
        SearchHit[] hits1 = hits.getHits();
        System.out.println("hitTotal ="+hitTotal);

        for (SearchHit hit :hits1){
            String id = hit.getId();
            float score = hit.getScore();
            System.out.println("id= "+id+" score= "+score);
            Map<String, Object> map = hit.getSourceAsMap();
            String name = (String) map.get("name");
            String studymodel = (String) map.get("studymodel");
            Double price = (Double) map.get("price");
            //时间戳

            System.out.println("id= "+id+" name ="+name);
            System.out.println("id= "+id+" studymodel ="+studymodel);
            System.out.println("id= "+id+" price ="+price);


        };
    }
     //过滤查询
    @Test
    public void getFilterQuery() throws IOException {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //指定类型
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //设置原字段过滤 第一个参数 结果集包含哪些字段 第二个 结果集不包含哪些字段
        searchSourceBuilder.fetchSource(new String[] {"name","studymodel","description","price"},new String[] {});
        //搜索方式 全部
        //multiQuery
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring框架", "name", "description")
                .minimumShouldMatch("50%")
                .field("name", 10);
        //不尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(multiMatchQueryBuilder);
        //过滤
        boolQueryBuilder.filter(QueryBuilders.termQuery("studymodel","201001"));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(60).lte(100));

        searchSourceBuilder.query(boolQueryBuilder);
        //向搜索请求对象中 设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索 向es发起http请求
        SearchResponse search = restHighLevelClient.search(searchRequest);
        //搜索结果
        SearchHits hits = search.getHits();
        long hitTotal = hits.getTotalHits();
        //获取得到比较高的匹配数据
        SearchHit[] hits1 = hits.getHits();
        System.out.println("hitTotal ="+hitTotal);

        for (SearchHit hit :hits1){
            String id = hit.getId();
            float score = hit.getScore();
            System.out.println("id= "+id+" score= "+score);
            Map<String, Object> map = hit.getSourceAsMap();
            String name = (String) map.get("name");
            String description = (String) map.get("description");
            String studymodel = (String) map.get("studymodel");
            Double price = (Double) map.get("price");
            //时间戳

            System.out.println("id= "+id+" name ="+name);
            System.out.println("id= "+id+" description ="+description);
            System.out.println("id= "+id+" studymodel ="+studymodel);
            System.out.println("id= "+id+" price ="+price);


        };
    }

    //排序
    @Test
    public void getSortQuery() throws IOException {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //指定类型
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //设置原字段过滤 第一个参数 结果集包含哪些字段 第二个 结果集不包含哪些字段
        searchSourceBuilder.fetchSource(new String[] {"name","studymodel","description","price"},new String[] {});
        //搜索方式 全部

        //不尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //过滤
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(0).lte(100));
        searchSourceBuilder.query(boolQueryBuilder);

        searchSourceBuilder.sort("studymodel", SortOrder.DESC);
        searchSourceBuilder.sort("price", SortOrder.ASC);
        //向搜索请求对象中 设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索 向es发起http请求
        SearchResponse search = restHighLevelClient.search(searchRequest);
        //搜索结果
        SearchHits hits = search.getHits();
        long hitTotal = hits.getTotalHits();
        //获取得到比较高的匹配数据
        SearchHit[] hits1 = hits.getHits();
        System.out.println("hitTotal ="+hitTotal);

        for (SearchHit hit :hits1){
            String id = hit.getId();
            float score = hit.getScore();
            System.out.println("id= "+id+" score= "+score);
            Map<String, Object> map = hit.getSourceAsMap();
            String name = (String) map.get("name");
            String description = (String) map.get("description");
            String studymodel = (String) map.get("studymodel");
            Double price = (Double) map.get("price");
            //时间戳

            System.out.println("id= "+id+" name ="+name);
            System.out.println("id= "+id+" description ="+description);
            System.out.println("id= "+id+" studymodel ="+studymodel);
            System.out.println("id= "+id+" price ="+price);


        };
    }

    //高亮设置
    @Test
    public void getHightLigthQuery() throws IOException {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //指定类型
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //设置原字段过滤 第一个参数 结果集包含哪些字段 第二个 结果集不包含哪些字段
        searchSourceBuilder.fetchSource(new String[] {"name","studymodel","description","price"},new String[] {});
        //搜索方式 全部
        //multiQuery
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("开发框架", "name", "description")
                .minimumShouldMatch("50%")
                .field("name", 10);
        //不尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(multiMatchQueryBuilder);
        //过滤
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(0).lte(100));
        searchSourceBuilder.query(boolQueryBuilder);
        //价格排序
        searchSourceBuilder.sort("price", SortOrder.ASC);
        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<tag>");
        highlightBuilder.postTags("</tag>");
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        highlightBuilder.fields().add(new HighlightBuilder.Field("description"));
        searchSourceBuilder.highlighter(highlightBuilder);


        //向搜索请求对象中 设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索 向es发起http请求
        SearchResponse search = restHighLevelClient.search(searchRequest);
        //搜索结果
        SearchHits hits = search.getHits();
        long hitTotal = hits.getTotalHits();
        //获取得到比较高的匹配数据
        SearchHit[] hits1 = hits.getHits();
        System.out.println("hitTotal ="+hitTotal);

        for (SearchHit hit :hits1){
            String id = hit.getId();
            float score = hit.getScore();
            System.out.println("id= "+id+" score= "+score);
            Map<String, Object> map = hit.getSourceAsMap();
            String name = (String) map.get("name");
            //取出高亮
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
              if(highlightFields!=null){
                  HighlightField nameHighlightField = highlightFields.get("name");
                  StringBuffer stringBuffer = new StringBuffer();
                  if(nameHighlightField!=null){
                      Text[] fragments = nameHighlightField.getFragments();
                      for(Text text:fragments){
                          stringBuffer.append(text);
                      }
                      name=stringBuffer.toString();
                  }
              }

            String description = (String) map.get("description");
            if(highlightFields!=null){
                HighlightField descriptionHighlightField = highlightFields.get("description");

                if(descriptionHighlightField!=null){
                    StringBuffer stringBuffer = new StringBuffer();
                    Text[] fragments = descriptionHighlightField.getFragments();
                    for(Text text:fragments){
                        stringBuffer.append(text);
                    }
                    description=stringBuffer.toString();
                }
            }
            String studymodel = (String) map.get("studymodel");
            Double price = (Double) map.get("price");
            //时间戳

            System.out.println("id= "+id+" name ="+name);
            System.out.println("id= "+id+" description ="+description);
            System.out.println("id= "+id+" studymodel ="+studymodel);
            System.out.println("id= "+id+" price ="+price);


        };
    }

}
