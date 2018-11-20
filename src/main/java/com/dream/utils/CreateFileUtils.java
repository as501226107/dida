package com.dream.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletContext;

public class CreateFileUtils {



	// 创建目录---以日期，一天一个文件夹
	public static File createDirModel(ServletContext context, String model) {
		String realPath = context.getRealPath("/media/" + model);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar
				.getInstance().getTime());
		File file = new File(realPath, date);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	// 创建文件名--区分同名文件,在文件名前加上当前的时间
	public static String createName(String name) {
		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(Calendar
				.getInstance().getTime()) + "_" + name;
	}

}
