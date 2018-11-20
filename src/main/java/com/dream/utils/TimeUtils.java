/**
 * 
 */
package com.dream.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
  *  Class Name: TimeUtils.java
  *  Description: 
  *  @author  dragon  DateTime 2018年10月9日 下午2:40:41 
  *  @company bvit 
  *  @email  a501226107@qq.com 
  *  @version 1.0
  */
public class TimeUtils {
	static SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String getTime() {
		return format.format(new Date());
	}
}
