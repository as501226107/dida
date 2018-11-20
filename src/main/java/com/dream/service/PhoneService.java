package com.dream.service;

import javax.jws.WebService;

@WebService
public interface PhoneService {
    //根据手机号查询手机归属地
    public String queryMobile(String phoneNum);
}
