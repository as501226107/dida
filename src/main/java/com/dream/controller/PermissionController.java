package com.dream.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dream.bean.Depart;
import com.dream.bean.Emp;
import com.dream.bean.Permission;
import com.dream.service.DepartService;
import com.dream.service.EmpService;
import com.dream.service.PermissionService;
import com.dream.utils.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dragon
 * @since 2018-11-11
 */
@Controller
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    PermissionService ps;
    @Autowired
    EmpService es;


}

