package com.dream.utils;

import com.dream.utils.ip.ArrayOfString;
import com.dream.utils.ip.IpAddressSearchWebService;
import com.dream.utils.ip.IpAddressSearchWebServiceSoap;

import java.util.List;

public class GetIpAddress {
    public static String getAddress(String ip){
        IpAddressSearchWebService ipAddressSearchWebService = new IpAddressSearchWebService();
        IpAddressSearchWebServiceSoap soap=ipAddressSearchWebService.getIpAddressSearchWebServiceSoap();
        ArrayOfString array = soap.getCountryCityByIp(ip);
        List<String> string = array.getString();
        return string.get(1);
    }

}
