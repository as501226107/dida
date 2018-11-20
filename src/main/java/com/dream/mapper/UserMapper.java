package com.dream.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dream.bean.User;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dragon
 * @since 2018-11-11
 */
public interface UserMapper extends BaseMapper<User> {
    public User login(User user);
    public User selectUserWithRole(User user);
    public User getMyTeachers(String no);
    public User getMyDaoyuan(String no);
}
