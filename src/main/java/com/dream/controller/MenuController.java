package com.dream.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dream.bean.Permission;
import com.dream.service.MenuService;
import com.dream.utils.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    MenuService ms;
    @RequestMapping("/list/{pageIndex}")
    public String lists(@PathVariable("pageIndex") Integer pageIndex, @RequestParam(defaultValue = "5") Integer pageSize, Model model){
        Page<Permission> page = new Page<>(pageIndex, pageSize);
        Page<Permission> pages = ms.selectPage(page, new EntityWrapper<Permission>().eq("del","0").eq("type","menu").orderBy("parentid"));
        //查询出的数据
        List<Permission> records = pages.getRecords();
        int totalCount=((Long)pages.getTotal()).intValue();
        boolean hasNext = pages.hasNext();
        boolean hasPre = pages.hasPrevious();
        PageHelper<Permission> pageBean = new PageHelper<>(pageIndex, pageSize, totalCount, records, null);
        model.addAttribute("pageBean",pageBean);
        model.addAttribute("hasPre",hasPre);
        model.addAttribute("hasNext",hasNext);
        return "/menus.jsp";
    }

    @RequestMapping("/getParentMenus")
    @ResponseBody
    public List<Permission> getMenus(Permission permission, HttpServletResponse response, HttpServletRequest request) throws Exception{
            return ms.selectList(new EntityWrapper<Permission>().eq("del",0).eq("type","menu")
            .eq("parentid",0)
            );
    }
    @ResponseBody
    @RequestMapping("/addMenu")
    public Boolean addMenu(Permission permission){
        permission.setType("menu");
        permission.setAvailable("1");
        permission.setDel(0);
        return  ms.insert(permission);
    }

    @ResponseBody
    @RequestMapping("/getMenu/{id}")
    public Permission getMenu(@PathVariable("id")Integer id){
        return ms.selectById(id);
    }

    @ResponseBody
    @RequestMapping("/update")
    public Boolean updateAllLevel(Permission permission){
        System.out.println(permission);
        return ms.updateById(permission);
    }

    @ResponseBody
    @RequestMapping("/delete/{id}")
    public Boolean delete(@PathVariable("id") Integer id){
        return ms.updateForSet("del=1",new EntityWrapper<Permission>().eq("id",id));
    }
}
