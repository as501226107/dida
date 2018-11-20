package com.dream.utils;

import org.apache.shiro.crypto.hash.SimpleHash;

public class PasswordUtils {
    public static String getMD5(String type,String password,String salt,Integer iterations){
        return  new SimpleHash(type, password, salt,iterations).toString();
    }
}
