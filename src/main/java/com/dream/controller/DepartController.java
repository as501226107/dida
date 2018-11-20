package com.dream.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dream.bean.Depart;
import com.dream.bean.Emp;
import com.dream.service.DepartService;
import com.dream.service.EmpService;
import com.dream.utils.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.rmi.server.RemoteServer;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dragon
 * @since 2018-11-09
 */
@Controller
@RequestMapping("/depart")
public class DepartController {
    @Autowired
    DepartService ds;
    @Autowired
    EmpService es;
    @RequestMapping("/list/{pageIndex}")
    public String lists(@PathVariable("pageIndex") Integer pageIndex, @RequestParam(defaultValue = "5") Integer pageSize, Model model){
        Page<Depart> page = new Page<>(pageIndex, pageSize);
        Page<Depart> pages = ds.selectPage(page, new EntityWrapper<Depart>().eq("del","0"));
        //查询出的数据
        List<Depart> records = pages.getRecords();
        int totalCount=((Long)pages.getTotal()).intValue();

        boolean hasNext = pages.hasNext();
        boolean hasPre = pages.hasPrevious();
        PageHelper<Depart> pageBean = new PageHelper<>(pageIndex, pageSize, totalCount, records, null);
        model.addAttribute("pageBean",pageBean);
        model.addAttribute("hasPre",hasPre);
        model.addAttribute("hasNext",hasNext);
        return "/departlist.jsp";
    }
    @RequestMapping("/pageToUpage/{id}")
    public String pageToUpdate(@PathVariable("id") Integer id, Model model){
        Depart grade = ds.selectById(id);
        model.addAttribute("depart",grade);
        return "/departupdate.jsp";
    }
    @RequestMapping("/update")
    public void update(Depart depart, HttpServletResponse response, HttpServletRequest request) throws Exception{
        boolean b = ds.updateById(depart);
        if(b){
            response.getWriter().write("<script>alert('更新成功！！');location.href='"+request.getContextPath()+"/depart/list/1'</script>");
        }else{
            response.getWriter().write("<script>alert('更新失败！！');location.href='"+request.getContextPath()+"/depart/list/1'</script>");
        }
    }
    @RequestMapping("/delete/{id}")
    public void delete(@PathVariable("id") Integer id, HttpServletResponse response, HttpServletRequest request) throws Exception{
        Depart depart=new Depart();
        depart.setId(id);
        depart.setDel(1);
        int count = es.selectCount(new EntityWrapper<Emp>().eq("did", depart.getId()).eq("del",0));
        if(count==0){
            boolean b = ds.updateById(depart);
            if(b){
                response.getWriter().write("<script>alert('删除成功！！');location.href='"+request.getContextPath()+"/depart/list/1'</script>");
            }else{
                response.getWriter().write("<script>alert('删除失败！！');location.href='"+request.getContextPath()+"/depart/list/1'</script>");
            }
        }else{
            response.getWriter().write("<script>alert('当前部门仍有雇员，删除失败！！');location.href='"+request.getContextPath()+"/depart/list/1'</script>");
        }


    }
    @RequestMapping("/goAdd")
    public String add()
    {
        return "/departadd.jsp";
    }
    @RequestMapping("/add")
    public void add(Depart grade, HttpServletResponse response, HttpServletRequest request, MultipartFile file) throws Exception{
        grade.setDel(0);
        //处理图片











        boolean b = ds.insert(grade);
        if(b){
            response.getWriter().write("<script>alert('添加成功！！');location.href='"+request.getContextPath()+"/depart/list/1'</script>");
        }else{
            response.getWriter().write("<script>alert('添加失败！！');location.href='"+request.getContextPath()+"/depart/list/1'</script>");
        }
    }
    @RequestMapping("/lists")
    @ResponseBody
    public List<Depart> lists(Depart grade, HttpServletResponse response, HttpServletRequest request) throws Exception{
        List<Depart> lists = ds.selectList(new EntityWrapper<Depart>().eq("del", 0));
        return lists;
    }
}

