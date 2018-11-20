package com.dream.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.dream.bean.User;
import com.dream.bean.UserRole;
import com.dream.mapper.UserMapper;
import com.dream.mapper.UserRoleMapper;
import com.dream.service.UserRoleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
    @Autowired
    UserMapper um;
    @Autowired
    UserRoleMapper  urm;
    @Override
    public Boolean updateUserRole(User user, Integer[] rids) {
        try {
            //更新用户
             Integer re = um.updateById(user);

            //删除该user对应得中间表角色
            Integer s= urm.delete(new EntityWrapper<UserRole>().eq("sys_user_id", user.getId()));
            //如果用户选择的角色不等于空，则为其添加相应的角色，如果rids为null，表示用户取消了所有的角色，上面已经删除
           if(rids!=null){
               //创建userRole中间对象
               for (Integer rid : rids) {
                   UserRole ur=new UserRole();
                   ur.setSysUserId(user.getId());
                   ur.setSysRoleId(rid);
                   urm.insert(ur);
               }
           }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
