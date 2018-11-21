package com.dream.test;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.dream.bean.Grade;
import com.dream.bean.Permission;
import com.dream.bean.Role;
import com.dream.bean.User;
import com.dream.mapper.GradeMapper;
import com.dream.mapper.PermissionMapper;
import com.dream.mapper.RoleMapper;
import com.dream.mapper.UserMapper;

import javax.annotation.Generated;
import javax.annotation.Resource;

import com.dream.service.GradeService;
import com.dream.service.UserService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-mybatis.xml")
public class Test01 {

    @Resource
    private UserService userService;
    @Autowired
    UserMapper mapper;
    @Autowired
    GradeService gs;
    @Autowired
    RoleMapper rm;
    @Autowired
    PermissionMapper pm;
    @Test
    public void test1() {
        Grade grade = new Grade();
        grade.setId(1);
        grade.setDel(1);
        boolean b = gs.updateById(grade);
    }

    @Test
    public void test2() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("123");
        System.out.println(mapper.selectOne(user));
    }

    @Test
    public void test3() {
        User selectById = mapper.selectById(1);
        System.out.println(selectById);
    }



    /*测试删除*/
    @Test
    public void test5() {
        Integer insert = mapper.deleteById(3);
        System.out.println(insert);
    }

    /*测试更新*/
    @Test
    public void test6() {
        User user = new User();
        user.setId(2);
        user.setUsername("asdasdasd");
        System.out.println(mapper.updateById(user));//带有动态sql
    }

    @Test
    public void test7() {
        User user = new User();
        user.setId(2);
        user.setUsername("asdsadA");
        user.setPassword("asd");
        System.out.println(mapper.updateAllColumnById(user));
    }
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
                .setTablePrefix("t_")
                .setInclude("t_approve");
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
    public void t (){
        User user=new User();
        user.setUsername("admin");
//        User user1 = mapper.selectUserByUP(user);
//        System.out.println(user1.getRoles());
        User user1 = mapper.login(user);
        System.out.println(user1);
        System.out.println(user1.getRoles());
//        List<User> users = mapper.selectUserWithRole(user);
//        System.out.println(users);
//        Role roleById = rm.getRoleById(1);
//        System.out.println(roleById);
    }
    @Test
    public void  test10(){
        List<Permission> menus = pm.getMenus(1);
        System.out.println(menus);
    }
}
