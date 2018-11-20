package com.dream.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dream.bean.Depart;
import com.dream.bean.Emp;
import com.dream.service.DepartService;
import com.dream.service.EmpService;
import com.dream.utils.FileUtils;
import com.dream.utils.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.security.provider.MD5;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dragon
 * @since 2018-11-09
 */
@Controller
@RequestMapping("/emp")
public class EmpController {
    @Autowired
    EmpService es;
    @Autowired
    DepartService ds;
    @RequestMapping("/list/{pageIndex}")
    public String lists(@PathVariable("pageIndex") Integer pageIndex, @RequestParam(defaultValue = "5") Integer pageSize, Model model){
        Page<Emp> page = new Page<>(pageIndex, pageSize);
        Page<Emp> pages = es.selectPage(page, new EntityWrapper<Emp>().eq("del","0"));
        //查询出的数据
        List<Emp> records = pages.getRecords();
        //初始化数据
        for (Emp emp:records) {
            Integer did = emp.getDid();
            Depart depart = ds.selectById(did);
            emp.setDepart(depart);
        }
        int totalCount=((Long)pages.getTotal()).intValue();
        boolean hasNext = pages.hasNext();
        boolean hasPre = pages.hasPrevious();
        PageHelper<Emp> pageBean = new PageHelper<>(pageIndex, pageSize, totalCount, records, null);
        model.addAttribute("pageBean",pageBean);
        model.addAttribute("hasPre",hasPre);
        model.addAttribute("hasNext",hasNext);
        return "/emplist.jsp";
    }
    @RequestMapping("/pageToUpage/{id}")
    public String pageToUpdate(@PathVariable("id") Integer id, Model model){
        Emp grade = es.selectById(id);
        model.addAttribute("emp",grade);
        return "/empupdate.jsp";
    }
    @RequestMapping("/update")
    public void update(Emp emp, HttpServletResponse response, HttpServletRequest request) throws Exception{
        boolean b = es.updateById(emp);
        if(b){
            response.getWriter().write("<script>alert('更新成功！！');location.href='"+request.getContextPath()+"/emp/list/1'</script>");
        }else{
            response.getWriter().write("<script>alert('更新失败！！');location.href='"+request.getContextPath()+"/emp/list/1'</script>");
        }
    }
    @RequestMapping("/delete/{id}")
    public void delete(@PathVariable("id") Integer id, HttpServletResponse response, HttpServletRequest request) throws Exception{
        Emp emp=new Emp();
        emp.setId(id);
        emp.setDel(1);
        boolean b = es.updateById(emp);
        if(b){
            response.getWriter().write("<script>alert('删除成功！！');location.href='"+request.getContextPath()+"/emp/list/1'</script>");
        }else{
            response.getWriter().write("<script>alert('删除失败！！');location.href='"+request.getContextPath()+"/emp/list/1'</script>");
        }
    }
    @RequestMapping("/goAdd")
    public String add()
    {
        return "/empadd.jsp";
    }
    @RequestMapping("/add")
    public void add(Emp emp, HttpServletResponse response, HttpServletRequest request, MultipartFile file) throws Exception{
        emp.setNo(UUID.randomUUID().toString());
        emp.setDel(0);
        boolean b = es.insert(emp);
        if(b){
            response.getWriter().write("<script>alert('添加成功！！');location.href='"+request.getContextPath()+"/emp/list/1'</script>");
        }else{
            response.getWriter().write("<script>alert('添加失败！！');location.href='"+request.getContextPath()+"/emp/list/1'</script>");
        }
    }
    @ResponseBody
    @RequestMapping("/uploade")
    public Map<String,Object> uploade(MultipartFile file) throws Exception{
        Map<String,Object> map=new HashMap<>();
       if(!file.isEmpty()){
           File dirByType = FileUtils.createDirByType("emp", "temp", "photo");
           String name = FileUtils.createName(file.getOriginalFilename());
           File f=new File(dirByType,name);
           file.transferTo(f);
           map.put("msg",FileUtils.getUrl(f.getAbsolutePath()));
           map.put("code",1000);
       }else{
           map.put("code","error");
       }
       return map;
    }
}

