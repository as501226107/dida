package com.dream.mapper;

import com.dream.bean.Role;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dragon
 * @since 2018-11-11
 */
public interface RoleMapper extends BaseMapper<Role> {
    public Role getRoleById(Integer id);
    public Role getRoleWithPermission(Integer id);
}
