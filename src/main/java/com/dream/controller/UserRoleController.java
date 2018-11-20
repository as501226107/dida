package com.dream.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dream.bean.*;
import com.dream.service.RoleService;
import com.dream.service.UserRoleService;
import com.dream.service.UserService;
import com.dream.utils.PageHelper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dragon
 * @since 2018-11-15
 */
@Controller
@RequestMapping("/userRole")
public class UserRoleController {
    @Autowired
    UserService us;
    @Autowired
    UserRoleService urs;
    @Autowired
    RoleService rs;
    @RequestMapping("/list/{pageIndex}")
    public String lists(@PathVariable("pageIndex") Integer pageIndex, @RequestParam(defaultValue = "5") Integer pageSize, Model model, User user){
        Page<User> page = new Page<>(pageIndex, pageSize);
        //判断user中是否存在locked

        Wrapper<User> wrapper= new EntityWrapper<User>().eq("del", "0").like("uname",user.getUsername());
        //用户选中了禁用或者启用执行一下流程,默认查询禁用和启用的
        if(user!=null&&user.getLocked()!=null&&!"-1".equals(user.getLocked())){
            System.out.println("运行这里.........");
            wrapper.eq("locked",user.getLocked());
        }
        Page<User> pages = us.selectPage(page,wrapper);
        //查询出的数据
        List<User> records = pages.getRecords();
        //加载数据
        int totalCount=((Long)pages.getTotal()).intValue();
        //为用户添加角色集合
        for (User s:records) {
            User user1 = us.selectUserWithRole(s);
            if(user1!=null){
                List<Role> roles = user1.getRoles();
                System.out.println(roles);
                s.setRoles(roles);
            }
        }
        boolean hasNext = pages.hasNext();
        boolean hasPre = pages.hasPrevious();
        PageHelper<User> pageBean = new PageHelper<>(pageIndex, pageSize, totalCount, records, user);
        model.addAttribute("pageBean",pageBean);
        model.addAttribute("hasPre",hasPre);
        model.addAttribute("hasNext",hasNext);
        return "/userlist.jsp";
    }
    @ResponseBody
    @RequestMapping("/pageToUpdate/{id}")
    public Map<String,Object> pageToUpdate(@PathVariable("id") Integer id){
        Map<String,Object> map=new HashMap<String,Object>();
        User user = us.selectById(id);
        Integer id1 = user.getId();
        //查询可用的角色
        List<Role> roles = rs.selectList(new EntityWrapper<Role>().eq("available", "1"));
        //判断该用户是否存在角色
        int count = urs.selectCount(new EntityWrapper<UserRole>().eq("sys_user_id", id1));
        //如果用户有角色 为其打上标志
        if(count>0){
            List<UserRole> sys_user_id = urs.selectList(new EntityWrapper<UserRole>().eq("sys_user_id", id1));
            for (Role role : roles) {
                for (UserRole userRole : sys_user_id) {
                    if(role.getId()==userRole.getSysRoleId()){
                        role.setFlag(true);
                    }
                }
            }
        }
        //查询角色
        map.put("user",user);
        map.put("roles",roles);
        return map;
    }
    @RequestMapping("/update")
    @ResponseBody
    public Boolean update(User user,Integer[] rids){
        return urs.updateUserRole(user,rids);
    }
    @RequestMapping("/changeStatus")
    @ResponseBody
    public Boolean changeStatus(String locked,Integer id){
      return  us.updateForSet("locked="+locked,new EntityWrapper<User>().eq("id",id));
    }

    @ResponseBody
    @RequestMapping("/delete/{id}")
    public Boolean delete(@PathVariable("id") Integer id){
        return  us.updateForSet("del=1",new EntityWrapper<User>().eq("id",id));
    }

}

