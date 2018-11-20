package com.dream.service;

import com.dream.bean.Permission;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon
 * @since 2018-11-11
 */
public interface PermissionService extends IService<Permission> {
    public List<Permission> getMenus(Integer id);
}
