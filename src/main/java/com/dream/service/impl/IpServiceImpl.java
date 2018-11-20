package com.dream.service.impl;

import com.dream.service.IpService;
import com.dream.utils.ip.IpAddressSearchWebServiceSoap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class IpServiceImpl implements IpService {
    @Autowired
    IpAddressSearchWebServiceSoap ipAddress;
    @Override
    public String getIpAddress(String ip) {
        List<String> string = ipAddress.getCountryCityByIp(ip).getString();
        return string.get(1);
    }

    public IpAddressSearchWebServiceSoap getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(IpAddressSearchWebServiceSoap ipAddress) {
        this.ipAddress = ipAddress;
    }
}
