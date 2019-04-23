package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.CourseMapper;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import com.xuecheng.manage_course.dao.TeanchplanRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
   @Autowired
   TeachplanMapper teachplanMapper;
   @Autowired
    CourseMapper courseMapper;
   @Autowired
    TeanchplanRepository teanchplanRepository;
    @Autowired
    CourseBaseRepository courseBaseRepository;
  //查询课程计划
  public TeachplanNode findTeachplanList(String courseId){
      TeachplanNode courseList = teachplanMapper.findCourseList(courseId);
      return  courseList;
  }
  //添加课程计划
    @Transactional //CRUD都需要添加事务
    public ResponseResult addCourseTeachplan(Teachplan teachplan) {
        if(teachplan==null|| StringUtils.isEmpty(teachplan.getCourseid())||StringUtils.isEmpty(teachplan.getPname())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);//非法参数
        }
        String courdeId = teachplan.getCourseid();
        String parentid = teachplan.getParentid();
        if(StringUtils.isEmpty(parentid)||parentid.length()<=0){
            //父id为空,没有填父上级节点
            parentid = this.getTeachplanParentid(courdeId);
        }
        //父节点查询出页面在某一个课程下添加课程计划时改课程的id
        Optional<Teachplan> byId = teanchplanRepository.findById(parentid);
        if(!byId.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);//非法参数
        }
        Teachplan teachplan2 = byId.get();
        String parentidGrade=teachplan2.getGrade();
        //准备添加课程计划
        Teachplan teachplan1 = new Teachplan();
        teachplan1.setCourseid(courdeId);
        teachplan1.setParentid(parentid);//父节点
        teachplan1.setPname(teachplan.getPname());
        teachplan1.setDescription(teachplan.getDescription());
        teachplan1.setOrderby(teachplan.getOrderby());//排序章节顺序有关
        teachplan1.setPtype(teachplan.getPtype());//视频还是文档
        teachplan1.setTimelength(teachplan.getTimelength());//学习时长（分钟）
        teachplan1.setStatus("0");
        if(parentidGrade.equals("1")){
            teachplan1.setGrade("2");
        }else {
            teachplan1.setGrade("3");
        }
        teanchplanRepository.save(teachplan1);
        return new ResponseResult(CommonCode.SUCCESS);
    }
    //获取课程根节点，如果没有则添加(只要courseId是当前课程的就有)
    private String getTeachplanParentid(String courseId){//父id为空,没有填父上级节点,所以Parentid=0
        List<Teachplan> byCourseidAndParentid = teanchplanRepository.findByCourseidAndParentid(courseId, "0");
        Optional<CourseBase> byId = courseBaseRepository.findById(courseId);
        if(!byId.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);//非法参数
        }
        CourseBase courseBase = byId.get();

        //需要添加
        if(byCourseidAndParentid==null||byCourseidAndParentid.size()<=0){
            Teachplan teachplan = new Teachplan();
            teachplan.setCourseid(courseId);
            teachplan.setGrade("1");//一级
            teachplan.setParentid("0");
            teachplan.setStatus("0");//未发布
            teachplan.setPname(courseBase.getName());
            teanchplanRepository.save(teachplan);
            return teachplan.getId();//以id作为新添加的课程计划父id
        }
        //不为空
        return  byCourseidAndParentid.get(0).getId();
    }

    public QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest) {
        //courseListRequest 是为以后扩展做条件查询准备的
        //第一个参数是第一页。第二参数是一页显示的数量
        if(page<=0){
            page=1;
        }
        if(size<=0){
            size=10;
        }
        PageHelper.startPage(page,size);
        Page<CourseInfo> courseList = courseMapper.findCourseList();
        List<CourseInfo> result = courseList.getResult();
        //总数
        long total = courseList.getTotal();
        //准备返回把数据放进去
        QueryResult<CourseInfo> objectQueryResult = new QueryResult<>();
        objectQueryResult.setList(result);
        objectQueryResult.setTotal(total);

        return new QueryResponseResult(CommonCode.SUCCESS,objectQueryResult);
    }

}
