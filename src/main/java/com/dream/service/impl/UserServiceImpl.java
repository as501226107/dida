package com.dream.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dream.bean.User;
import com.dream.mapper.UserMapper;
import com.dream.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service

public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    UserMapper um;
    @Override
    public User login(User user) {
        return um.login(user);
    }

    @Override
    public User selectUserWithRole(User user) {
        return um.selectUserWithRole(user);
    }

    @Override
    public User getMyTeachers(String no) {
        return um.getMyTeachers(no);
    }

    @Override
    public User getMyDaoyuan(String no) {
        return um.getMyDaoyuan(no);
    }
}
