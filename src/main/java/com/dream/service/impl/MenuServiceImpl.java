package com.dream.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dream.bean.Permission;
import com.dream.mapper.PermissionMapper;
import com.dream.service.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements MenuService {
}
