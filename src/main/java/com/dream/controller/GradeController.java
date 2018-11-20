package com.dream.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dream.bean.Course;
import com.dream.bean.Grade;
import com.dream.service.CourseService;
import com.dream.service.GradeService;
import com.dream.utils.PageHelper;
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
@RequestMapping("/grade")
public class GradeController {
    @Autowired
    CourseService cs;
    @Autowired
    GradeService gs;
    @RequestMapping("/lists")
    @ResponseBody
    public List<Grade> list(){
        List<Grade> grades = gs.selectList(null);
        return grades;
    }
    @RequestMapping("/list/{pageIndex}")
    public String list(@PathVariable("pageIndex") Integer pageIndex, @RequestParam(defaultValue = "5") Integer pageSize, Model model){
        Page<Grade> page = new Page<>(pageIndex, pageSize);
        Page<Grade> pages = gs.selectPage(page, new EntityWrapper<Grade>().eq("del","0"));
        //查询出的数据
        List<Grade> records = pages.getRecords();
        int totalCount=((Long)pages.getTotal()).intValue();
        //封装数据
        for (Grade g:records
        ) {
            Integer cid = g.getCid();
            Course course = cs.selectById(cid);
            g.setCourse(course);
            //查询总人数

        }
        boolean hasNext = pages.hasNext();
        boolean hasPre = pages.hasPrevious();
        PageHelper<Grade> pageBean = new PageHelper<>(pageIndex, pageSize, totalCount, records, null);
        model.addAttribute("pageBean",pageBean);
        model.addAttribute("hasPre",hasPre);
        model.addAttribute("hasNext",hasNext);
        return "/gradelist.jsp";
    }
    @RequestMapping("/pageToUpage/{id}")
    public String pageToUpdate(@PathVariable("id") Integer id, Model model){
        Grade grade = gs.selectById(id);
        model.addAttribute("grade",grade);
        return "/gradeupdate.jsp";
    }
    @RequestMapping("/update")
    public void update(Grade grade, HttpServletResponse response, HttpServletRequest request) throws Exception{
        System.out.println(grade);
        boolean b = gs.updateById(grade);
        if(b){
            response.getWriter().write("<script>alert('更新成功！！');location.href='"+request.getContextPath()+"/grade/list/1'</script>");
        }else{
            response.getWriter().write("<script>alert('更新失败！！');location.href='"+request.getContextPath()+"/grade/list/1'</script>");
        }
    }
    @RequestMapping("/delete/{id}")
    public void delete(@PathVariable("id") Integer id, HttpServletResponse response, HttpServletRequest request) throws Exception{
        System.out.println("delete..............");
        Grade grade = new Grade();
        grade.setId(id);
        grade.setDel(1);
        boolean b = gs.updateById(grade);
        if(b){
            response.getWriter().write("<script>alert('删除成功！！');location.href='"+request.getContextPath()+"/grade/list/1'</script>");
        }else{
            response.getWriter().write("<script>alert('删除失败！！');location.href='"+request.getContextPath()+"/grade/list/1'</script>");
        }
    }
    @RequestMapping("/goAdd")
    public String add()
    {
        return "/gradeadd.jsp";
    }
    @RequestMapping("/add")
    public void add(Grade grade,HttpServletResponse response, HttpServletRequest request) throws Exception{
        grade.setDel(0);
        boolean b = gs.insert(grade);
        if(b){
            response.getWriter().write("<script>alert('添加成功！！');location.href='"+request.getContextPath()+"/grade/list/1'</script>");
        }else{
            response.getWriter().write("<script>alert('添加失败！！');location.href='"+request.getContextPath()+"/grade/list/1'</script>");
        }
    }
}

