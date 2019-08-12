package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jcajce.provider.symmetric.AES;
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
    @Autowired
    CourseMarketRepository courseMarketRepository;
    @Autowired
    CoursePicRepository coursePicRepository;
    @Autowired
    TeanchplanNodeRepository teanchplanNodeRepository;

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
    //添加课程信息
    public ResponseResult addCourseBase(CourseBase courseBase) {
        if(courseBase==null){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        CourseBase courseBase1 = new CourseBase();
        //id自增
        courseBase1.setId(null);
        courseBase1.setName(courseBase.getName());
        courseBase1.setUsers(courseBase.getUsers());
        courseBase1.setGrade(courseBase.getGrade());
        courseBase1.setStudymodel(courseBase.getStudymodel());
        courseBase1.setMt(courseBase.getMt());
        courseBase1.setSt(courseBase.getSt());
        courseBase1.setDescription(courseBase.getDescription());
        courseBaseRepository.save(courseBase1);
        return new ResponseResult(CommonCode.SUCCESS);
    }
    //课程基础信息回显
    public CourseBase findCourseBaseByCourseId(String courseId) {
        Optional<CourseBase> byId = courseBaseRepository.findById(courseId);
        if(!byId.isPresent()){
            ExceptionCast.cast(CmsCode.COURSE_BASE_ISNULL);
        }
        return  byId.get();
    }
    //更新基础信息回显
    public ResponseResult updateByCourseId(CourseBase courseBase ,String courseId){
        Optional<CourseBase> byId = courseBaseRepository.findById(courseId);
        if(!byId.isPresent()){
            ExceptionCast.cast(CmsCode.COURSE_BASE_ISNULL);
        }
        CourseBase courseBase1 = byId.get();
        courseBase1.setName(courseBase.getName());
        courseBase1.setUsers(courseBase.getUsers());
        courseBase1.setGrade(courseBase.getGrade());
        courseBase1.setStudymodel(courseBase.getStudymodel());
        courseBase1.setMt(courseBase.getMt());
        courseBase1.setSt(courseBase.getSt());
        courseBase1.setDescription(courseBase.getDescription());
        courseBaseRepository.save(courseBase1);
        return new ResponseResult(CommonCode.SUCCESS);
    }
    //通过课程id查出课程营销信息
    public CourseMarket getCourseMarketById(String courseId) {
        Optional<CourseMarket> byId = courseMarketRepository.findById(courseId);
        if(!byId.isPresent()){
            ExceptionCast.cast(CmsCode.COURSE_MARK_ISNULL);
        }
        return byId.get();
    }

    //通过课程id更新课程营销信息
    public ResponseResult updateCourseMarket(String id, CourseMarket courseMarket) {
        CourseMarket courseMarketById = this.getCourseMarketById(id);
        if(courseMarketById==null){//查出为空则添加
            courseMarketRepository.save(courseMarket);
        }
        if("203001".equals(courseMarket.getCharge())){
            //免费课程应该是元，同时将金额保存到price_old
            courseMarketById.setPrice_old(courseMarketById.getPrice());
            courseMarketById.setPrice((float) 0.00);
        }
        if("203002".equals(courseMarket.getCharge())){//收费课程
            courseMarketById.setPrice(courseMarket.getPrice());
        }
        if("204001".equals(courseMarket.getValid())){
            //课程永久有效，有效期为null,startTime,endTime全部为null
            courseMarketById.setStartTime(null);
            courseMarketById.setEndTime(null);
        }
        if("204002".equals(courseMarket.getValid())){//课程限时
            courseMarketById.setStartTime(courseMarket.getStartTime());
            courseMarketById.setEndTime(courseMarket.getEndTime());
        }
        courseMarketById.setCharge(courseMarket.getCharge());
        courseMarketById.setValid(courseMarket.getValid());
        courseMarketById.setQq(courseMarket.getQq());
        //courseMarketById.set
        courseMarketRepository.save(courseMarketById);
        return  new ResponseResult(CommonCode.SUCCESS);
    }


    public ResponseResult addCoursePic(String courseId, String pic) {
        Optional<CoursePic> byId = coursePicRepository.findById(courseId);
        CoursePic coursePic =null;
        if(byId.isPresent()){//可以查出来
            coursePic = byId.get();
        }
        if(coursePic==null){
            coursePic=new CoursePic();
        }
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
        return  new ResponseResult(CommonCode.SUCCESS);

    }

    public CoursePic findCoursePicListByCourseId(String courseId) {
        Optional<CoursePic> byId = coursePicRepository.findById(courseId);
        if(byId.isPresent()){//可以查出来
            return  byId.get();
        }
        return  null;
    }

    //删除课程图片
    @Transactional
    public ResponseResult deleteCoursePic(String courseId) {
//执行删除，返回1表示删除成功，返回0表示删除失败
        long result = coursePicRepository.deleteByCourseid(courseId);
        if(result>0){
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }
    //课程视图 有课程基本信息 课程图片 课程营销 课程计划
    public CourseView couserView(String courseId) {
        CourseView courseView = new CourseView();
        Optional<CourseBase> byId1 = courseBaseRepository.findById(courseId);
        if(byId1.isPresent()){
            courseView.setCourseBase(byId1.get());
        }
        Optional<CoursePic> byId2 = coursePicRepository.findById(courseId);
        if(byId2.isPresent()){
            courseView.setCoursePic(byId2.get());
        }
        Optional<CourseMarket> byId3 = courseMarketRepository.findById(courseId);
        if(byId3.isPresent()){
            courseView.setCourseMarket(byId3.get());
        }
        TeachplanNode courseList = teachplanMapper.findCourseList(courseId);
        courseView.setTeachplanNode(courseList);
        return  courseView;
    }
}
