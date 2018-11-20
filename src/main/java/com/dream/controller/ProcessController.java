package com.dream.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.dream.bean.Apply;
import com.dream.bean.MyTask;
import com.dream.bean.Processdefine;
import com.dream.bean.User;
import com.dream.service.ApplyService;
import com.dream.service.ProcessdefineService;
import com.dream.service.UserService;
import com.dream.utils.CreateFileUtils;
import com.dream.utils.FileUtils;
import com.dream.utils.TimeUtils;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipInputStream;

@RequestMapping("/process")
@Scope(scopeName = "prototype")
@Controller
public class ProcessController {
    @Autowired
    ProcessEngine pe;
    @Autowired
    ProcessdefineService pds;
    @Autowired
    UserService us;
    @Autowired
    ApplyService as;
    @Autowired
    ProcessdefineService pfs;
    @RequestMapping("/myApplies")
    public String myApplies(HttpSession session, String type, HttpServletResponse response, HttpServletRequest request,Model model){
        User user=(User)session.getAttribute("user");
        TaskQuery taskQuery = pe.getTaskService().createTaskQuery();//创建任务查询
        String uname = user.getUname();//获取当前用户的名字
        taskQuery.taskAssignee(uname);//设置查询的名字
        taskQuery.orderByTaskCreateTime().desc();//根据创建时间降序查询
        List<Task> list = taskQuery.list();//查询任务集合
        List<MyTask> myApplies=new ArrayList<>();
        for (Task task : list) {
            Apply apply=(Apply)pe.getRuntimeService().getVariable(task.getExecutionId(),"apply");//获取流程变量需要Task中的ExecutionId
            User user1 = us.selectById(apply.getExcuteId());
            MyTask mytask=new MyTask(task,apply,user1);
            myApplies.add(mytask);
        }
        model.addAttribute("myApplies",myApplies);
        return "/flow_myApplyList.jsp";
    }

        @RequestMapping("/myTasks")
    public String myTask(HttpSession session, HttpServletResponse response, HttpServletRequest request,Model model){
        User user=(User)session.getAttribute("user");
        TaskQuery taskQuery = pe.getTaskService().createTaskQuery();//创建任务查询
        String uname = user.getUname();//获取当前用户的名字
        taskQuery.taskAssignee(uname);//设置查询的名字
        taskQuery.orderByTaskCreateTime().desc();//根据创建时间降序查询
        List<Task> list = taskQuery.list();//查询任务集合
        List<MyTask> myTasks=new ArrayList<>();
        for (Task task : list) {
            Apply apply=(Apply)pe.getRuntimeService().getVariable(task.getExecutionId(),"apply");//获取流程变量需要Task中的ExecutionId
            User user1 = us.selectById(apply.getExcuteId());
            MyTask mytask=new MyTask(task,apply,user1);
            myTasks.add(mytask);
        }
        model.addAttribute("myTasks",myTasks);
        return "/flow_myTaskList.jsp";
    }


    @RequestMapping("/deploy")
   public void deploy(MultipartFile mFile, HttpSession session, String type, HttpServletResponse response, HttpServletRequest request) throws Exception{
        //1.获取上传后的文件
        User user=(User)session.getAttribute("user");
        String path = "";

        if(!mFile.isEmpty()){
//            File dir = FileUtils.createDirByUsername(user.getUsername(), "deploy");
//            String name = FileUtils.createName(mFile.getOriginalFilename());
//            File file=new File(dir,name);
//            mFile.transferTo(file);
//            filePath=file.getAbsolutePath();
            File head = CreateFileUtils.createDirModel(request.getServletContext(), "process");
            // 获取文件名
            String filename = mFile.getOriginalFilename();
            // 生成随机名
            String createName = CreateFileUtils.createName(filename);
            File  f = new File(head, createName);
            // 生成保存路径
            path = "media/photo/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "/" + createName;
            // 上传
            mFile.transferTo(f);
            //2.部署流程定义
            DeploymentBuilder deploymentBuilder = pe.getRepositoryService().createDeployment();
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(f));
            deploymentBuilder.addZipInputStream(zipInputStream);
            //为部署命名
            deploymentBuilder.name(type);
            Deployment deployment = deploymentBuilder.deploy();
            System.out.println("部署成功"+deployment.getId());
            //获得当前部署的流程的key，我们需要先取出部署后的流程，倒序取出第一个
            ProcessDefinitionQuery query = pe.getRepositoryService().createProcessDefinitionQuery().orderByProcessDefinitionVersion().desc();
            ProcessDefinition pd = query.list().get(0);
            String key = pd.getKey();
            //将流程定义key和type存入数据库
            Processdefine p=new Processdefine();
            p.setKey(key);
            p.setType(type);
            boolean insert = pds.insert(p);
            //成功后返回列表界面
            response.getWriter().write("<script>alert('部署成功！！');location.href='"+request.getContextPath()+"/process/list'</script>");
        }else{
            response.getWriter().write("<script>alert('部署失败！！');location.href='"+request.getContextPath()+"/process/list'</script>");
        }
        }
    @RequestMapping("/list")
    public String list(Model model){
        ProcessDefinitionQuery query = pe.getRepositoryService().createProcessDefinitionQuery();
        List<ProcessDefinition> list = query.list();
        model.addAttribute("list",list);
        return "/flow_ProcessDefinitionList.jsp";
    }
    @RequestMapping("/delete/{id}")
    public void delete(@PathVariable("id") String  pid, HttpServletResponse response, HttpServletRequest request) throws Exception{
        // processEngine.getRepositoryService().deleteDeployment(deploymentId);
        pe.getRepositoryService().deleteDeployment(pid, true); //级联删除
        //删除成功后返回列表界面
        response.getWriter().write("<script>alert('删除成功！！');location.href='"+request.getContextPath()+"/process/list'</script>");
    }
    /*
     * 03-查看流程图
     */
    @RequestMapping("/processpng/{pdid}")
    public void processpng(@PathVariable("pdid") String pdid,
                           HttpServletResponse response) throws Exception {
        System.out.println("流程定义的ID是:" + pdid);
        InputStream in = pe.getRepositoryService().getProcessDiagram(pdid);
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("image/png");
        int data = 0;
        while ((data = in.read()) != -1) {
            out.write(data);
        }
        in.close();
        out.close();
    }

    //起草申请
    @RequestMapping("/goApply")
    public String goApply(Model model,HttpSession session){
        User user=(User) session.getAttribute("user");
        System.out.println(user);
        //根据user的学号去得到他所在班上的班主任信息
        User myTeachers = us.getMyTeachers(user.getNo());
        model.addAttribute("teacher",myTeachers);
        return "/flow_apply.jsp";
    }
    //起草申请
    @RequestMapping("/apply")
    public void apply(Model model, HttpSession session, Apply apply,HttpServletResponse response,HttpServletRequest request) throws Exception{
        User user=(User) session.getAttribute("user");
        //1.设置初始化值
        apply.setExcuteId(user.getId());
        apply.setStatus(Apply.APPLY_STATUS_ING);
        apply.setApplydate(TimeUtils.getTime());
        //2.存取申请对象
        boolean insert = as.insert(apply);
        //启动任务
        //根据类型获取任务key
        Processdefine applytype = pfs.selectOne(new EntityWrapper<Processdefine>().eq("type","请假流程"));
        String pdkey = applytype.getKey();
        //获取该学生所对应班级的班主任和辅导员的id
        //3.设置环境变量
        Map<String,Object> variables=new HashMap<>();
        variables.put("apply",apply);//将该申请的对象传入到变量中
        variables.put("uname",user.getUname());//设置申请人的名称
        variables.put("banzhuren",us.getMyTeachers(user.getNo()).getUname());//设置班主任的名称
        variables.put("fudaoyuan",us.getMyDaoyuan(user.getNo()).getUname());//设置导员
        //4 获取流程实例
        ProcessInstance pi = pe.getRuntimeService().startProcessInstanceByKey(pdkey, variables);
        System.out.println("ProcessInstance 的id为："+pi.getId());
        //5 执行流程（提交任务）
        TaskQuery query = pe.getTaskService().createTaskQuery();
        query.taskAssignee(user.getUname());//根据登录人查询 他的任务
        query.processInstanceId(pi.getId());//设置实例id
        //15015
        Task task = query.singleResult();
        String taskId = task.getId(); //任务ID
        pe.getTaskService().complete(taskId); //提交任务
        System.out.println("任务申请成功......");
        response.getWriter()
                .write("<script>alert('申请提交成功，审核中....');location.href='"
                        + request.getContextPath() + "/process/list;'</script>");
    }

}
