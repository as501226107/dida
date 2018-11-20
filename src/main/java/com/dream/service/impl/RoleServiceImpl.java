package com.dream.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.dream.bean.Role;
import com.dream.bean.RolePermission;
import com.dream.mapper.RoleMapper;
import com.dream.mapper.RolePermissionMapper;
import com.dream.service.RoleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dragon
 * @since 2018-11-11
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    RoleMapper rm;
    @Autowired
    RolePermissionMapper rpm;
    @Override
    public Role getRoleWithPermission(Integer id) {
        return rm.getRoleWithPermission(id);
    }

    @Override
    public Boolean addRole(Role role, Integer[] rids) {
        role.setAvailable("1");
        //1.添加role
        Integer id = rm.insert(role);
        if(id>0){
            for (Integer rid : rids) {
                RolePermission rp=new RolePermission();
                rp.setSysRoleId(role.getId());
                rp.setSysPermissionId(rid);
                rpm.insert(rp);
            }
            return true;
        }
        return false;
    }

    @Override
    public Boolean update(Role role, Integer[] ids) {
        try {
            //1.首先更新role
            Integer integer = rm.updateById(role);

            //2.将当前的role对应的中间表的关联关系清空
            Integer integer1 = rpm.delete(new EntityWrapper<RolePermission>().eq("sys_role_id",role.getId()));

            //3.重新添加关联关系
            for (Integer id : ids) {
                RolePermission rp=new RolePermission();
                rp.setSysRoleId(role.getId());
                rp.setSysPermissionId(id);
                rpm.insert(rp);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
