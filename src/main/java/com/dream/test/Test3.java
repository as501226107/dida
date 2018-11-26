package com.dream.test;

import org.junit.Test;

public class Test3 {
    @Test
    public void test3(){
        Integer i=new Integer(1);
        Integer b=new Integer(1);
        int c=1;
        System.out.println(i==b);
        System.out.println(i==c);
        System.out.println(b==c);
        System.out.println(b==Integer.valueOf(c));

        Integer e=100;//自动装箱Integer e = Integer.valueOf(100);
        int f=e;//自动拆箱int f = e.intValue();
    }
    @Test
    public void test4(){

    }
}
