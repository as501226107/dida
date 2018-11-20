package com.dream.controller;

import com.dream.service.PhoneService;
import com.dream.utils.ip.IpAddressSearchWebServiceSoap;
import com.dream.utils.mobile.MobileCodeWSSoap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
@RequestMapping("/util")
@Controller
public class UtilController {
    @Autowired
    PhoneService ps;
    @RequestMapping("/getLocation")
    public String phone(String phone, Model model){
        String s = ps.queryMobile(phone);
        model.addAttribute("phone",s);
        return "/phone.jsp";
    }

}
