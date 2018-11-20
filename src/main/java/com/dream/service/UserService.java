package com.dream.service;

import com.baomidou.mybatisplus.service.IService;
import com.dream.bean.User;

import java.util.List;

public interface UserService extends IService<User> {
    public User login(User user);
    public User selectUserWithRole(User user);
    public User getMyTeachers(String no);
    public User getMyDaoyuan(String no);
}
