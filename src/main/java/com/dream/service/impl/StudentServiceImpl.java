package com.dream.service.impl;

import com.dream.bean.Student;
import com.dream.bean.User;
import com.dream.bean.UserRole;
import com.dream.mapper.RoleMapper;
import com.dream.mapper.StudentMapper;
import com.dream.mapper.UserMapper;
import com.dream.mapper.UserRoleMapper;
import com.dream.service.StudentService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dream.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dragon
 * @since 2018-11-08
 */
@Service
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Autowired
    StudentMapper sm;
    @Autowired
    UserMapper um;
    @Autowired
    RoleMapper rm;
    @Autowired
    UserRoleMapper urm;
    @Override
    public boolean addStudent(Student student) {
        try {
            //1.向学生表中添加学生
            Integer sStatus = sm.insert(student);
            //2.配置user
            User user=new User();
            String cardno = student.getCardno();
            user.setUsername(cardno);//使用身份证作为username
            //选取身份证后六位作为密码
            user.setSalt(UUID.randomUUID().toString().replace("-", ""));
            user.setPassword(PasswordUtils.getMD5("MD5",cardno.substring(cardno.length()-6),user.getSalt(),1024));//为学生密码加盐加密
            user.setLocked("0");
            user.setDel(0);
            user.setPhoto("head/default/default.png");
            //3.添加user
            Integer userStatus = um.insert(user);
            //4.为其配置角色
            UserRole ur=new UserRole();
            ur.setSysUserId(user.getId());
            ur.setSysRoleId(5);//学生角色id
            Integer insert = urm.insert(ur);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
