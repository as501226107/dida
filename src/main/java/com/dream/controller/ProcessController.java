package com.dream.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.dream.bean.*;
import com.dream.service.*;
import com.dream.utils.CreateFileUtils;
import com.dream.utils.FileUtils;
import com.dream.utils.TimeUtils;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricTaskInstance;
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
    @Autowired
    ProcessService ps;
    @Autowired
    ApproveService approveService;
    /*
     * 11-查询png流 显示图
     */
    @RequestMapping("/viewImage")
    public void viewImage(String deploymentId, String imageName, HttpServletResponse response) throws Exception {
        InputStream in = pe.getRepositoryService().getResourceAsStream(deploymentId, imageName);

        ServletOutputStream out = response.getOutputStream();
        response.setContentType("image/png");
        byte buffer[] = new byte[1024];
        int len = 0;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        out.close();
        in.close();
    }
    /*
     * 10-查看流程记录图片图示
     */
    @RequestMapping("/showPng")
    public String showPng(Integer applyId, Model model) {

        // applyId  申请对象 Aplly的ID

        // 根据流程变量查询当前任务
        TaskQuery query = pe.getTaskService().createTaskQuery();
        // 根据设置的流程变量进行过滤
        query.processVariableValueEquals("applyId", applyId); //根据流程变量中applyId 查询一个任务

        Task task = query.singleResult();

        // 根据任务查询流程定义对象
        String processDefinitionId = task.getProcessDefinitionId();
        ProcessDefinitionQuery query1 = pe.getRepositoryService().createProcessDefinitionQuery();
        query1.processDefinitionId(processDefinitionId);

        ProcessDefinition pd = query1.singleResult();

        model.addAttribute("deploymentId", pd.getDeploymentId()); //获取部署流程ID
        model.addAttribute("imageName", pd.getDiagramResourceName());  //获取部署图片资源名字

        // 根据任务查询坐标
        Map<String, Object> map = ps.findCoordingByTask(task);

        model.addAttribute("acs", map);

        return "/flow_image.jsp";
    }



    //查看流程流转记录
    @RequestMapping("/flowRecord/{applyId}")
    public String flowRecord(@PathVariable("applyId") Integer id,Model model){
        List<Approve> approves = approveService.selectList(new EntityWrapper<Approve>().eq("apply_id", id));
        for (Approve approve : approves) {
            Integer userId = approve.getUserId();
            User user = us.selectById(userId);
            approve.setUser(user);
        }
        model.addAttribute("approves",approves);
        return "/flow_myApplyReordList.jsp";
    }


    //审批流程
    @RequestMapping("/approve")
    public void approve(HttpSession session,Integer applyId,String taskId,String comment,Boolean approval,HttpServletResponse response, HttpServletRequest request) throws  Exception{
        User user=(User)session.getAttribute("user");
        //1.接受用户发送的参数
        Approve approve=new Approve();
        approve.setId(applyId);
        approve.setUserId(user.getId());//处理人的id
        approve.setApproveDate(TimeUtils.getTime());
        approve.setComment(comment);
        approve.setApproveValue(approval);
        int aprrove = ps.aprrove(approve, taskId);
        //更新用户当前的任务数
        TaskQuery taskQuery = pe.getTaskService().createTaskQuery();//创建任务查询
        taskQuery.taskAssignee(user.getUname());//设置查询的名字
        Integer size=taskQuery.list().size();//
        session.setAttribute("myTasks",size);
        if(aprrove==1){
            response.getWriter().write("<script>alert('审批成功！！');location.href='"+request.getContextPath()+"/process/myTasks'</script>");
        }else{
            response.getWriter().write("<script>alert('审批失败！！');location.href='"+request.getContextPath()+"/process/myTasks'</script>");

        }
    }


    //审批任务，查看详细信息
    @RequestMapping("/pageToApplyReply")
    public String pageToApplyReply(String taskId,String apply_id,HttpSession session, HttpServletResponse response, HttpServletRequest request,Model model){
        User user=(User)session.getAttribute("user");
        Apply apply = as.selectById(apply_id);
        model.addAttribute("apply",apply);
        model.addAttribute("taskId",taskId);
        return "/flow_applyReply.jsp";
    }


    //查看任务申请信息
    @RequestMapping("/pageToApplyInfo/{id}")
    public String pageToApplyInfo(@PathVariable("id") String apply_id,HttpSession session, HttpServletResponse response, HttpServletRequest request,Model model){
        Apply apply = as.selectById(apply_id);
        model.addAttribute("apply",apply);
        return "/flow_applyInfo.jsp";
    }

    //我的申请
    @RequestMapping("/myApplies")
    public String myApplies(HttpSession session, String type, HttpServletResponse response, HttpServletRequest request,Model model){
        User user=(User)session.getAttribute("user");
        List<Apply> apply_id = as.selectList(new EntityWrapper<Apply>().eq("apply_id", user.getId()));
        model.addAttribute("myApplies",apply_id);
        return "/flow_myApplyList.jsp";
    }

    //查看我的任务列表
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
        apply.setStatus(Apply.APPLY_STATUS_ING);
        apply.setApplydate(TimeUtils.getTime());
        apply.setApplyId(user.getId());
        //开始申请
        int status = ps.apply(apply, user);
        if(status==1){
            response.getWriter()
                    .write("<script>alert('申请提交成功，审核中....');location.href='"
                            + request.getContextPath() + "/process/myApplies;'</script>");
        }else {
            response.getWriter()
                    .write("<script>alert('提交失败，请练习管理员.');location.href='"
                            + request.getContextPath() + "/process/myApplies;'</script>");
        }

    }

}
