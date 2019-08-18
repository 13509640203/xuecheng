package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EsCourseService {
    
    @Autowired
    RestHighLevelClient restHighLevelClient;
     //索引
    @Value("${xuecheng.elasticsearch.course.index}")
    private String es_index;
    @Value("${xuecheng.elasticsearch.course.type}")
    private String es_type;
    @Value("${xuecheng.elasticsearch.course.source_field}")
    private String source_field;
    

    public QueryResponseResult queryList(int page, int size, CourseSearchParam courseSearchParam)  {
        QueryResult<CoursePub> queryResult = new QueryResult<>();
        List<CoursePub> list = new ArrayList<>();

        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest(es_index);
        //指定类型
        searchRequest.types(es_type);
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置原字段过滤
        String[] split = source_field.split(",");
        searchSourceBuilder.fetchSource(split,new String[]{});
        //搜索方式
        //不尔查询作为组装而已
        BoolQueryBuilder boolQueryBuilder =  QueryBuilders.boolQuery();
      
        if(courseSearchParam.getKeyword()!=null) {
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(courseSearchParam.getKeyword(),
                    "name", "description", "teachplan")
                    .minimumShouldMatch("70%")
                    .field("name", 10);
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }
            //过滤
            //一级分类
            if(StringUtils.isNotEmpty(courseSearchParam.getMt())){
                boolQueryBuilder.filter(QueryBuilders.termQuery("mt",courseSearchParam.getMt()));
            }
            //二级分类
            if(StringUtils.isNotEmpty(courseSearchParam.getSt())){
                boolQueryBuilder.filter(QueryBuilders.termQuery("st",courseSearchParam.getSt()));
            }
            //难度等级
            if(StringUtils.isNotEmpty(courseSearchParam.getGrade())){
                boolQueryBuilder.filter(QueryBuilders.termQuery("grade",courseSearchParam.getGrade()));
            }

            searchSourceBuilder.query(boolQueryBuilder);
            //分页

             if(page<=0){
                 page=1;
             }
            if(size<=0){
                size=10;
              }
              int from=(page-1)*size;

            searchSourceBuilder.from(from);
            searchSourceBuilder.size(size);
            //升序 默认
          if(StringUtils.isNotEmpty(courseSearchParam.getSort())) {
              searchSourceBuilder.sort(courseSearchParam.getSort(), SortOrder.ASC);
         }
           //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        if(StringUtils.isNotEmpty(courseSearchParam.getGrade())){
            highlightBuilder.fields().add(new HighlightBuilder.Field("grade"));
        }
        searchSourceBuilder.highlighter(highlightBuilder);
        //向搜索请求对象中 设置搜索源
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = null;
            try {
                searchResponse = restHighLevelClient.search(searchRequest);
            } catch (IOException e) {
                e.printStackTrace();
                return  new QueryResponseResult(CommonCode.FAIL,null);
            }
            SearchHits hits = searchResponse.getHits();
            queryResult.setTotal(hits.totalHits);
            //获取结果
            SearchHit[] hitsHits = hits.getHits();

              for(SearchHit hit:hitsHits){
                  CoursePub coursePub = new CoursePub();
                  Map<String, Object> map = hit.getSourceAsMap();
                  String name = (String) map.get("name");
                  //取出高亮
                  Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                  if(highlightFields!=null){
                      HighlightField nameHighlightField = highlightFields.get("name");
                      StringBuffer buffer = new StringBuffer();
                      if(nameHighlightField!=null){
                          Text[] fragments = nameHighlightField.getFragments();
                          for(Text text:fragments){
                              buffer.append(text.string());
                          }
                          name=buffer.toString();
                      }
                  }
                  coursePub.setName(name);
                  //图片
                  String pic = (String) map.get("pic");
                  coursePub.setPic(pic);
                  //价格
                  Float price =null;
                  if(map.get("price")!=null){
                      price = Float.parseFloat(""+ map.get("price"));
                      coursePub.setPrice(price);
                  }

                  //原来的价格
                  Float price_old =null;
                   if(map.get("price_old")!=null){
                       price_old = Float.parseFloat(""+map.get("price_old"));
                       coursePub.setPrice_old(price_old);
                   }
                  String grade = (String) map.get("grade");
                   //如果用户搜索了高亮则显示高亮
                  if(StringUtils.isNotEmpty(courseSearchParam.getGrade())){
                      HighlightField nameHighlightField = highlightFields.get("grade");
                      StringBuffer buffer = new StringBuffer();
                      if(nameHighlightField!=null){
                          Text[] fragments = nameHighlightField.getFragments();
                          for(Text text:fragments){
                              buffer.append(text.string());
                          }
                          grade=buffer.toString();
                      }
                  }
                      coursePub.setGrade(grade);

                  //添加到list
                  list.add(coursePub);
              }
            queryResult.setList(list);
            return  new QueryResponseResult(CommonCode.SUCCESS,queryResult);




    }
}
