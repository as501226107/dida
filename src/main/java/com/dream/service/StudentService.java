package com.dream.service;

import com.dream.bean.Student;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dragon
 * @since 2018-11-08
 */
public interface StudentService extends IService<Student> {
    public boolean addStudent(Student student);

}
