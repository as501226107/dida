package com.dream.service;

import javax.jws.WebService;

@WebService
public interface IpService {
    public String getIpAddress(String ip);
}
