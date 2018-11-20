package com.dream.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dream.bean.*;
import com.dream.service.PermissionService;
import com.dream.service.RolePermissionService;
import com.dream.service.RoleService;
import com.dream.utils.PageHelper;
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

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dragon
 * @since 2018-11-11
 */
@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    RoleService rs;
    @Autowired
    PermissionService ps;
    @Autowired
    RolePermissionService rps;
    @RequestMapping("/list/{pageIndex}")
    public String list(@PathVariable("pageIndex") Integer pageIndex, @RequestParam(defaultValue = "5") Integer pageSize, Model model){
        Page<Role> page = new Page<>(pageIndex, pageSize);
        Page<Role> pages = rs.selectPage(page, new EntityWrapper<Role>().eq("available","1"));
        //查询出的数据
        List<Role> records = pages.getRecords();
        int totalCount=((Long)pages.getTotal()).intValue();
        //封装数据
        boolean hasNext = pages.hasNext();
        boolean hasPre = pages.hasPrevious();
        PageHelper<Role> pageBean = new PageHelper<>(pageIndex, pageSize, totalCount, records, null);
        model.addAttribute("pageBean",pageBean);
        model.addAttribute("hasPre",hasPre);
        model.addAttribute("hasNext",hasNext);
        return "/rolelist.jsp";
    }
    @ResponseBody
    @RequestMapping("/pagaToAdd")
    public Map<String,Object> pagaToAdd(){
        //1.获得菜单列表
        List<Permission> menus = ps.selectList(new EntityWrapper<Permission>().
                eq("del", 0)
                .eq("type","menu")
        );
        //2.获得权限列表
        List<Permission> permissions = ps.selectList(new EntityWrapper<Permission>().
                eq("del", 0)
                .eq("type","permission")
        );
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("menus",menus);
        maps.put("permissions",permissions);
        return maps;
    }
    @ResponseBody
    @RequestMapping("/add")
    public Boolean add(Role role,Integer[] rids){
        return rs.addRole(role, rids);
    }
    @ResponseBody
    @RequestMapping("/pagaToUpdate/{id}")
    public Map<String,Object> pagaToUpdate(@PathVariable("id") Integer id){
        //1.获得菜单列表
        List<Permission> menus = ps.selectList(new EntityWrapper<Permission>().
                eq("del", 0)
                .eq("type","menu")
        );
        //2.获得权限列表
        List<Permission> permissions = ps.selectList(new EntityWrapper<Permission>().
                eq("del", 0)
                .eq("type","permission")
        );
        //3.获得中间表
        List<RolePermission> sys_role_id = rps.selectList(new EntityWrapper<RolePermission>().eq("sys_role_id", id));

        //设置应该选中得权限
        for (Permission permission : permissions) {
            for (RolePermission rolePermission : sys_role_id) {
                Integer sysPermissionId = rolePermission.getSysPermissionId();
                if(permission.getId()==sysPermissionId){
                    permission.setFlag(true);
                }
            }
        }
        //设置应该选中得菜单
        for (Permission menu : menus) {
            for (RolePermission rolePermission : sys_role_id) {
                Integer sysPermissionId = rolePermission.getSysPermissionId();
                if(menu.getId()==sysPermissionId){
                    menu.setFlag(true);
                }
            }
        }
        //4.获取role
        Role role=rs.selectById(id);
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("menus",menus);
        maps.put("permissions",permissions);
        maps.put("role",role);
        return maps;
    }
    @ResponseBody
    @RequestMapping("/update")
    public Boolean update(Role role,Integer[] rids){
       return rs.update(role,rids);
    }

    @ResponseBody
    @RequestMapping("/delete/{id}")
    public Boolean delete(@PathVariable("id") Integer id){
        //
        return rs.updateForSet("available=0",new EntityWrapper<Role>().eq("id",id));
    }
}

