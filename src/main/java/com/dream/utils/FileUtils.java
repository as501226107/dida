package com.dream.utils;

import org.junit.Test;

import javax.servlet.ServletContext;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileUtils {
    public static String rootPath="d:\\pic\\img\\";
    //public static String rootPath="/home/pic/img/";
    // 创建目录---以日期，一天一个文件夹
    public static  File createDir(String realPath) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        File file = new File(realPath, date);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
    // 创建目录--自定义目录，根路径，中间路径，子路径
    public static  File createDirByUsername(String username,String type) {
        StringBuilder builder=new StringBuilder();
        builder.append("user\\").append(username).append("\\"+type);
        File file = new File(rootPath, builder.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
    // 创建目录--自定义目录，功能   功能id 图片的类型
    public static  File createDirByType(String gn,String no,String type) {
        StringBuilder builder=new StringBuilder();
        builder.append(gn).append("\\"+no).append("\\"+type);
        File file = new File(rootPath, builder.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
    // 创建目录---以日期，一天一个文件夹
    public static File createDir(ServletContext context,String path) {
        String realPath = context.getRealPath("/media/"+path);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        File file = new File(realPath, date);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
    // 创建文件名--区分同名文件,在文件名前加上当前的时间
    public static String createName(String name) {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(Calendar.getInstance().getTime()) + "_" + name;
    }
    public static String  getUrl(String fileName){
        return fileName.substring(rootPath.length());
    }
    @Test
    public  void test1(){
        System.out.println("d:\\pic\\img\\admin\\head\\20181108171341658_1540455920426.png".substring("d:\\pic\\img\\".length()));
    }
}
