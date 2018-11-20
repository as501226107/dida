package com.dream.service;

import com.dream.bean.User;
import com.dream.bean.UserRole;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon
 * @since 2018-11-15
 */
public interface UserRoleService extends IService<UserRole> {
    public Boolean updateUserRole(User user,Integer[] rids);
}
