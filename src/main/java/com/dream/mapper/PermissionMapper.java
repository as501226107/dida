package com.dream.mapper;

import com.dream.bean.Permission;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dragon
 * @since 2018-11-11
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    public List<Permission> getMenus(Integer id);
    public List<Permission> getPermissions(Integer id);
}
