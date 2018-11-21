package com.dream.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.dream.bean.Apply;
import com.dream.bean.Approve;
import com.dream.bean.Processdefine;
import com.dream.bean.User;
import com.dream.service.*;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class ProcessServiceImpl implements ProcessService {
    @Autowired
    ProcessEngine pe;
    @Autowired
    ApplyService as;
    @Autowired
    ProcessdefineService pfs;
    @Autowired
    UserService us;
    @Autowired
    ApproveService approveService;
    @Override
    public int apply(Apply apply, User user) {
        try {
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
            variables.put("banzhuren",us.selectById(apply.getExcuteId()).getUname());//设置班主任的名称

            //导员名称
            String fudaoyuan = us.getMyDaoyuan(user.getNo()).getUname();
            System.out.println("导员的名称:"+fudaoyuan);
            variables.put("fudaoyuan",fudaoyuan);//设置导员

            //存入申请id方便之后获取流程图的位置标记
            variables.put("applyId", apply.getId());

            //4 获取流程实例
            ProcessInstance pi = pe.getRuntimeService().startProcessInstanceByKey(pdkey, variables);
            System.out.println("ProcessInstance 的id为："+pi.getId());

            //保存任务的流程实例id
            String id = pi.getId();
            apply.setProcessInstanceId(id);
            boolean b = as.updateById(apply);

            //5 执行流程（提交任务）
            TaskQuery query = pe.getTaskService().createTaskQuery();
            query.taskAssignee(user.getUname());//根据登录人查询 他的任务
            query.processInstanceId(pi.getId());//设置实例id
            Task task = query.singleResult();
            String taskId = task.getId(); //任务ID
            pe.getTaskService().complete(taskId); //提交任务
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int aprrove(Approve approve, String taskId) {
        try {

            //1.将审批对象存入数据库
            boolean insert = approveService.insert(approve);
            //2.开始提交任务
            //得到apply表的id
            Apply apply = as.selectById(approve.getApplyId());
            //0.根据当前任务id查到任务
            Task task = pe.getTaskService().createTaskQuery().taskId(taskId).singleResult();
            // 0.获得当前流程实例id
            String processInstanceId = task.getProcessInstanceId();
            TaskService taskService = pe.getTaskService();
            //3.办理当前的任务
            pe.getTaskService().complete(taskId); // 办理当前任务
            //4.根据流程实例ID查询出流程实例对象
            ProcessInstance processInstance = pe.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            //5.判断审批是否通过
            if(approve.getApproveValue()==true){
                //判断当前流程对象是否为null,null表示当前流程已经结束，没有下一个,设置审批流程对象的状态为通过
                if(processInstance==null){
                    apply.setStatus(Apply.APPLY_STATUS_SUCCESS);//设置通过审批
                }
            }else{
                apply.setStatus(Apply.APPLY_STATUS_REFUSE);//表示当前申请审批未通过
                if(processInstance!=null){//如果当前任务不是最后一个流程
                    pe.getRuntimeService().deleteProcessInstance(processInstanceId, Apply.APPLY_STATUS_REFUSE); // 中途拒绝，并设置原因
                }
                //如果是最后一个流程直接结束就行了，因为申请状态已经设置为拒绝
            }
            as.updateById(apply);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Map<String, Object> findCoordingByTask(Task task) {
        // 获得流程定义id
        String processDefinitionId = task.getProcessDefinitionId();
        // 获得流程实例id
        String processInstanceId = task.getProcessInstanceId();
        // 返回的流程定义对象中包含坐标信息
        ProcessDefinitionEntity pd = (ProcessDefinitionEntity) pe.getRepositoryService().getProcessDefinition(processDefinitionId);
        ProcessInstanceQuery query = pe.getRuntimeService().createProcessInstanceQuery();
        // 根据流程实例id过滤
        query.processInstanceId(processInstanceId);
        ProcessInstance processInstance = query.singleResult();

        // 根据流程实例对象获得当前的获得节点
        String activityId = processInstance.getActivityId();// usertask2

        ActivityImpl activityImpl = pd.findActivity(activityId);

        int x = activityImpl.getX();
        int y = activityImpl.getY();
        int height = activityImpl.getHeight();
        int width = activityImpl.getWidth();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("x", x);
        map.put("y", y);
        map.put("height", height);
        map.put("width", width);

        return map;
    }
}
