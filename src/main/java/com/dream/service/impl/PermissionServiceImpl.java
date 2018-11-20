package com.dream.service.impl;

import com.dream.bean.Permission;
import com.dream.mapper.PermissionMapper;
import com.dream.service.PermissionService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dragon
 * @since 2018-11-11
 */
@Service
@Transactional
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Autowired
    PermissionMapper pm;
    @Override
    public List<Permission> getMenus(Integer id) {
        return pm.getMenus(id);
    }
}
