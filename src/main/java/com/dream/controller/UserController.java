package com.dream.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dream.bean.Loginlog;
import com.dream.bean.Permission;
import com.dream.bean.User;
import com.dream.service.IpService;
import com.dream.service.LoginlogService;
import com.dream.service.PermissionService;
import com.dream.service.UserService;
import com.dream.utils.CreateFileUtils;
import com.dream.utils.TimeUtils;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.TaskQuery;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Scope("prototype")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private LoginlogService ls;
    @Autowired
    IpService ipService;
    @Autowired
    PermissionService ps;
    @Autowired
    ProcessEngine pe;
    @RequestMapping("/login")
    public void login(String loginAddress,User user, HttpServletRequest request, HttpServletResponse response) throws Exception{
        User u = userService.selectOne(new EntityWrapper<User>(user).eq("del",0));
        if(u!=null) {
            //获取当前认证的用户
            Subject subject =SecurityUtils.getSubject();
            //将用户名和密码封装为UsernamePasswordToken对象
            UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
            //设置shiro登录认证
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
            Loginlog log=new Loginlog(localAddr,user.getUsername(),loginTime,loginAddress);
            //存入用户登录信息
            ls.insert(log);
            //读取最近四条用户登录信息
            Page<Loginlog> page=new Page<>(1,4,"createtime",false);
            Page<Loginlog> pages = ls.selectPage(page, new EntityWrapper<Loginlog>().eq("no", u.getUsername()));
            List<Loginlog> loginLog = pages.getRecords();
            session.setAttribute("loginLog",loginLog);
            session.setAttribute("menus",menus);
            response.getWriter().write("<script>location.href='"+request.getContextPath()+"/index.jsp'</script>");

        }else {
            response.getWriter().write("<script>alert('用户名或者密码错误');location.href='"+request.getContextPath()+"/login.jsp'</script>");
        }
    }
    @RequestMapping("/shirologin")
    public String login1(String loginAddress,User user, HttpServletRequest request, HttpServletResponse response){
        //获取当前认证的用户
        Subject subject =SecurityUtils.getSubject();
        //判断当前用户是否认证
        if(!subject.isAuthenticated()) {
            //将用户名和密码封装为UsernamePasswordToken对象
            UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
            //设置记住我
            token.setRememberMe(true);
            //登录
            try {
                //开始登录
                subject.login(token);
                User login = (User)subject.getPrincipal();
                Session session = subject.getSession();
                session.setAttribute("user",login);
                //获取待办事项
                TaskQuery taskQuery = pe.getTaskService().createTaskQuery();//创建任务查询
                taskQuery.taskAssignee(login.getUname());//设置查询的名字
                Integer size=taskQuery.list().size();//
                session.setAttribute("myTasks",size);
                String loginTime= TimeUtils.getTime();
                //更新上次登录时间
                userService.updateForSet("last_login_date='"+loginTime+"'",new EntityWrapper<User>().eq("id",login.getId()));
                //获取用户登录信息
                String localAddr = request.getRemoteAddr();
                //获取菜单
                List<Permission> menus = ps.getMenus(login.getId());
                Loginlog log=new Loginlog(localAddr,user.getUsername(),loginTime,loginAddress);
                //存入用户登录信息
                ls.insert(log);
                //读取最近四条用户登录信息
                Page<Loginlog> page=new Page<>(1,4,"createtime",false);
                Page<Loginlog> pages = ls.selectPage(page, new EntityWrapper<Loginlog>().eq("no", login.getUsername()));
                List<Loginlog> loginLog = pages.getRecords();
                session.setAttribute("loginLog",loginLog);
                session.setAttribute("menus",menus);
            } catch (AuthenticationException e) {
                //输出错误信息
                System.out.println(e);
            }

        }
        return "redirect:/index.jsp";
    }
    @RequestMapping("/loginOut")
    public String loginOut() throws Exception{
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/login.jsp";
    }
    @RequestMapping("/upload")
    public void uploadphoto(HttpSession session, @RequestParam("photo") MultipartFile file,HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user =(User) session.getAttribute("user");
        // 文件保存到数据库中路径
        String photo = "";
        String username = user.getUsername();
          //File head = FileUtils.createDirByUsername(username, "head");
        //String name = FileUtils.createName(file.getOriginalFilename());
         if (!file.isEmpty()) {
             File head = CreateFileUtils.createDirModel(request.getServletContext(), "photo");
            // 获取文件名
            String filename = file.getOriginalFilename();
            // 生成随机名
            String createName = CreateFileUtils.createName(filename);
            File f = new File(head, createName);
            // 生成保存路径
            photo = "media/photo/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "/" + createName;
            // 上传
            file.transferTo(f);}
//        System.out.println("原始名称:"+name);
//        System.out.println("存储路径:"+head.getAbsolutePath());
          //File file1=new File(head,name);
//        System.out.println("上传文件的真实路径"+file1.getAbsolutePath());
//        System.out.println("url:"+FileUtils.getUrl(file1.getAbsolutePath()));
        //String url = FileUtils.getUrl(file1.getAbsolutePath());
       // file.transferTo(file1);
        user.setPhoto(photo);
        boolean b = userService.updateById(user);
        session.setAttribute("user",user);
        if(b){
            response.getWriter().write("<script>alert('修改头像成功');location.href='"+request.getContextPath()+"/userInfo.jsp'</script>");
        }else{
            response.getWriter().write("<script>alert('修改头像失败');location.href='"+request.getContextPath()+"/photo.jsp'</script>");
        }
    }
    @RequestMapping("/validatePass/{password}")
    @ResponseBody
    public Map<String,Object> validatePass(@PathVariable("password") String password, HttpSession session){
        User user =(User) session.getAttribute("user");
        int i = userService.selectCount(new EntityWrapper<User>().eq("password", password).eq("id", user.getId()));
        Map<String,Object> map=new HashMap<>();
        if(i>0){
            map.put("status","true");
            return map ;
        }else{
            map.put("status","false");
            return map;
        }
    }
    @RequestMapping("/updatePassword")
    public void updatePassword(@RequestParam("newpassword") String password, HttpSession session,HttpServletRequest request, HttpServletResponse response) throws  Exception{
        User user =(User) session.getAttribute("user");
        boolean i=userService.updateForSet("password="+password,new EntityWrapper<User>().eq("id",user.getId()));
        if(i){
            response.getWriter().write("<script>alert('修改密码成功');window.parent.location.href='"+request.getContextPath()+"/user/loginOut'</script>");
        }else{
            response.getWriter().write("<script>alert('修改密码失败');location.href='"+request.getContextPath()+"/password.jsp'</script>");
        }
    }
}
