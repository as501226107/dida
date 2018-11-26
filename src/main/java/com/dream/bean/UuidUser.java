package com.dream.bean;

import java.util.HashMap;
import java.util.Map;

public class UuidUser {
    private static Map<String, User> map;
    public static Map<String, User> getUUMaper(){
        if(map==null){
            map=new HashMap<String, User>();
        }
        return map;
    }
}
