package com.dream.service;

import com.dream.bean.Role;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon
 * @since 2018-11-11
 */
public interface RoleService extends IService<Role> {
    public Role getRoleWithPermission(Integer id);
    public Boolean addRole(Role role,Integer[] rids);
    public Boolean update(Role role,Integer[] ids);
}
