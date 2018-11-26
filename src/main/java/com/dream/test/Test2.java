package com.dream.test;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.dream.bean.Apply;
import com.dream.utils.PasswordUtils;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-mybatis.xml")
public class Test2 {
    @Autowired
    ProcessEngine pe;
        @Test
        public void test8(){
            /*获取当前工程的路径*/
            String property = System.getProperty("user.dir");
            System.out.println();
            GlobalConfig config=new GlobalConfig();
            config.setActiveRecord(false)
                    .setAuthor("dragon")
                    .setOutputDir("d://src//main//java")
                    .setFileOverride(true)
                    .setIdType(IdType.AUTO)
                    .setServiceName("%sService")
                    .setBaseResultMap(true)
                    .setBaseColumnList(true);
            //2.配置数据源
            DataSourceConfig dataSourceConfig = new DataSourceConfig();
            dataSourceConfig.setDbType(DbType.MYSQL)  // 设置数据库类型
                    .setDriverName("com.mysql.jdbc.Driver")
                    .setUrl("jdbc:mysql:///didadb?useUnicode=true&characterEncoding=utf-8")
                    .setUsername("root")
                    .setPassword("as501226107A.");

            //3. 策略配置
            StrategyConfig stConfig = new StrategyConfig();
            stConfig.setCapitalMode(true) //全局大写命名
                    .setDbColumnUnderline(true)
                    .setNaming(NamingStrategy.underline_to_camel)
                    .setTablePrefix("sys_")
                    .setInclude("sys_user");
            //4. 包名策略配置
            PackageConfig pkConfig = new PackageConfig();
            pkConfig.setParent("com.dream")
                    .setMapper("mapper")
                    .setService("service")
                    .setController("controller")
                    .setEntity("bean")
                    .setXml("mapper");

            //5. 整合配置
            AutoGenerator ag = new AutoGenerator();
            ag.setGlobalConfig(config)
                    .setDataSource(dataSourceConfig)
                    .setStrategy(stConfig)
                    .setPackageInfo(pkConfig);
            //6. 执行
            ag.execute();
    }
    @Test
    public void test(){
        TaskQuery taskQuery = pe.getTaskService().createTaskQuery();//创建任务查询
        taskQuery.taskAssignee("沈志龙");//设置查询的名字
        taskQuery.orderByTaskCreateTime().desc();//根据创建时间降序查询
        Task task=taskQuery.taskId("22536").singleResult();

        System.out.println(task.getId());
//        List<Task> list = taskQuery.list();//查询任务集合
//        List<MyTask> myTasks=new ArrayList<>();
//        for (Task task : list) {
//            Apply apply=(Apply)pe.getRuntimeService().getVariable(task.getExecutionId(),"apply");//获取流程变量需要Task中的ExecutionId
//            System.out.println(task.getId());
//
//        }

    }
    @Test
    public void test1(){
        List<HistoricTaskInstance> list= pe.getHistoryService()
        .createHistoricTaskInstanceQuery()
       .taskAssignee("刘德华")
               .finished().list();

        for(HistoricTaskInstance hti:list){
            String executionId = hti.getExecutionId();
            Apply apply=(Apply)pe.getRuntimeService().getVariable(hti.getExecutionId(),"apply");//获取流程变量需要Task中的ExecutionId
            System.out.println(apply.getStatus());
            System.out.println("任务ID:"+hti.getId());
            System.out.println("流程实例ID:"+hti.getProcessInstanceId());
            System.out.println("班里人："+hti.getAssignee());
            System.out.println("创建时间："+hti.getCreateTime());
            System.out.println("结束时间："+hti.getEndTime());
            System.out.println("===========================");
        }
    }

}
