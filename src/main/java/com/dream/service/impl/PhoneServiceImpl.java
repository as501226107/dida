package com.dream.service.impl;

import com.dream.service.PhoneService;
import com.dream.utils.mobile.MobileCodeWSSoap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhoneServiceImpl implements PhoneService {
   // @Autowired
    private MobileCodeWSSoap mobile;
    @Override
    public String queryMobile(String phoneNum) {
        return mobile.getMobileCodeInfo(phoneNum,null);
    }

    public MobileCodeWSSoap getMobile() {
        return mobile;
    }

    public void setMobile(MobileCodeWSSoap mobile) {
        this.mobile = mobile;
    }
}
