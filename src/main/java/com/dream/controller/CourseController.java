package com.dream.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dream.bean.Course;
import com.dream.bean.Grade;
import com.dream.service.CourseService;
import com.dream.service.GradeService;
import com.dream.utils.PageHelper;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dragon
 * @since 2018-11-07
 */
@Controller
@RequestMapping("/course")
public class CourseController {
    @Autowired
    CourseService cs;
    @Autowired
    GradeService gs;
    @RequestMapping("/list")
    @ResponseBody
    public List<Course> list(){
        List<Course> courses = cs.selectList(null);
        return courses;
    }
    @RequestMapping("/list/{pageIndex}")
    public String lists(@PathVariable("pageIndex") Integer pageIndex, @RequestParam(defaultValue = "5") Integer pageSize, Model model){
        Page<Course> page = new Page<>(pageIndex, pageSize);
        Page<Course> pages = cs.selectPage(page, new EntityWrapper<Course>().eq("del","0"));
        //查询出的数据
        List<Course> records = pages.getRecords();
        int totalCount=((Long)pages.getTotal()).intValue();

        boolean hasNext = pages.hasNext();
        boolean hasPre = pages.hasPrevious();
        PageHelper<Course> pageBean = new PageHelper<>(pageIndex, pageSize, totalCount, records, null);
        model.addAttribute("pageBean",pageBean);
        model.addAttribute("hasPre",hasPre);
        model.addAttribute("hasNext",hasNext);
        return "/courselist.jsp";
    }
    @RequestMapping("/pageToUpdate/{id}")
    public String pageToUpdate(@PathVariable("id") Integer id,Model model){
        Course course = cs.selectById(id);
        model.addAttribute("course",course);
        return "/courseupdate.jsp";
    }
    @RequestMapping("/update")
    public void update(Course course, HttpServletResponse response, HttpServletRequest request) throws Exception{
        boolean b = cs.updateById(course);
        if(b){
            response.getWriter().write("<script>alert('更新成功！！');location.href='"+request.getContextPath()+"/course/list/1'</script>");
        }else{
            response.getWriter().write("<script>alert('更新失败！！');location.href='"+request.getContextPath()+"/course/list/1'</script>");
        }
    }
    @RequestMapping("/delete/{id}")
    public void delete(@PathVariable("id") Integer id, HttpServletResponse response, HttpServletRequest request) throws Exception{
        System.out.println("delete..............");
        //判断当前课程是否存在班级
        int count = gs.selectCount(new EntityWrapper<Grade>().eq("cid", id));
        if(count==0){
              /*  Course course = new Course();
                course.setId(id);
                course.setDel(1);*/
            // boolean b = cs.updateById(course);
            boolean b = cs.updateForSet("del=" + 1, new EntityWrapper<Course>().eq("id", id));
            if(b){
                response.getWriter().write("<script>alert('删除成功！！');location.href='"+request.getContextPath()+"/course/list/1'</script>");
            }else{
                response.getWriter().write("<script>alert('删除失败！！');location.href='"+request.getContextPath()+"/course/list/1'</script>");
            }
        }else{
            response.getWriter().write("<script>alert('当前课程已被使用，删除失败！！！');location.href='"+request.getContextPath()+"/course/list/1'</script>");
        }
    }
    @RequestMapping("/goAdd")
    public String add()
    {
        return "/courseadd.jsp";
    }
    @RequestMapping("/add")
    public void add(Course course, HttpServletResponse response, HttpServletRequest request) throws Exception{
        course.setDel(0);
        boolean b = cs.insert(course);
        if(b){
            response.getWriter().write("<script>alert('添加成功！！');location.href='"+request.getContextPath()+"/course/list/1'</script>");
        }else{
            response.getWriter().write("<script>alert('添加失败！！');location.href='"+request.getContextPath()+"/course/list/1'</script>");
        }
    }
}

