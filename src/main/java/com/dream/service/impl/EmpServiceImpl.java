package com.dream.service.impl;

import com.dream.bean.Emp;
import com.dream.mapper.EmpMapper;
import com.dream.service.EmpService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dragon
 * @since 2018-11-09
 */
@Service
public class EmpServiceImpl extends ServiceImpl<EmpMapper, Emp> implements EmpService {

}
