package com.dream.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dream.bean.Loginlog;
import com.dream.bean.Permission;
import com.dream.bean.User;
import com.dream.bean.UuidUser;
import com.dream.service.LoginlogService;
import com.dream.service.PermissionService;
import com.dream.service.UserService;
import com.dream.utils.TimeUtils;
import com.dream.utils.ZXingCode;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.TaskQuery;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//二维码生成控制器
@Controller
@RequestMapping("/xcode")
public class XcodeController {
    @Autowired
    UserService userService;
    @Autowired
    ProcessEngine pe;
    @Autowired
    PermissionService ps;
    @Autowired
    LoginlogService ls;
    @RequestMapping("/getCode")
    public void getCode(HttpServletResponse response, HttpServletRequest request) throws Exception{
        response.setContentType("image/jpeg");
        String randomUUID = UUID.randomUUID().toString().replace("-","");
        File logoFile = new File("D://QrCode/ico.png");
        String url = "http://192.168.0.125"+request.getContextPath()+"/Login.html?uuid="+randomUUID;
        System.out.println("url:"+url);
        String note = "扫码登录";
        BufferedImage drawLogoQRCode = ZXingCode.drawLogoQRCode(logoFile, url, note);
        ServletOutputStream os = response.getOutputStream();
        ImageIO.write(drawLogoQRCode, "png", os);
        request.getSession().setAttribute("uuid",randomUUID);
    }
    @RequestMapping("/phoneLogin")
    @ResponseBody
    public Boolean phoneLogin(HttpServletRequest request,HttpServletResponse response,User user) throws Exception{
        String uuid = request.getParameter("uuid");
        String uname = request.getParameter("uname");
        String upwd = request.getParameter("upwd");
        System.out.println("uuid:"+uuid);
        System.out.println("uname:"+uname);
        System.out.println("upwd"+upwd);
        //TODO 验证登录
        boolean bool = false;
        User u = userService.selectOne(new EntityWrapper<User>(user).eq("del",0));
        if(u!=null){
            bool=true;
            //将登陆信息存入map
            UuidUser.getUUMaper().put(uuid,u);
          return bool;
        }
        else{
            return bool;
        }

    }
    @RequestMapping("/longConnection")
    @ResponseBody
    public String longConnection(HttpServletRequest request,String loginAddress){
        /*
         * 1、首先获取到页面发送的uuid，
         * 2、设置定时器，持续与页面进行交流，判断是否已经扫描了，如果已经扫描了，该二维码失效（二维码失效）
         * 3、用户扫描后会进入登录界面，登录成功后，将uuid和user的登录信息封装到map集合中
         * 4、定时器此时获取到该uuid对应的user则进行自动登录
         * */
        /*if(i>100){
            i=0;
            response.getWriter().print("timeout");
        }*/
        String uuid=(String)request.getSession().getAttribute("uuid");
        Map<String, User> uuMaper = UuidUser.getUUMaper();
        User u = uuMaper.get(uuid);
        if(u!=null){
            //进行权限认证
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(u.getUsername(), u.getPassword());
            subject.login(token);
            Session session = subject.getSession();
            session.setAttribute("user",u);
            //获取待办事项
            TaskQuery taskQuery = pe.getTaskService().createTaskQuery();//创建任务查询
            taskQuery.taskAssignee(u.getUname());//设置查询的名字
            Integer size=taskQuery.list().size();//
            session.setAttribute("myTasks",size);
            String loginTime= TimeUtils.getTime();
            //更新上次登录时间
            userService.updateForSet("last_login_date='"+loginTime+"'",new EntityWrapper<User>().eq("id",u.getId()));
            //获取用户登录信息
            String localAddr = request.getRemoteAddr();
            //获取菜单
            List<Permission> menus = ps.getMenus(u.getId());
            Loginlog log=new Loginlog(localAddr,u.getUsername(),loginTime,loginAddress);
            //存入用户登录信息
            ls.insert(log);
            //读取最近四条用户登录信息
            Page<Loginlog> page=new Page<>(1,4,"createtime",false);
            Page<Loginlog> pages = ls.selectPage(page, new EntityWrapper<Loginlog>().eq("no", u.getUsername()));
            List<Loginlog> loginLog = pages.getRecords();
            session.setAttribute("loginLog",loginLog);
            session.setAttribute("menus",menus);
            return "success";
        }else{
            return "wait";
        }
    }
}
