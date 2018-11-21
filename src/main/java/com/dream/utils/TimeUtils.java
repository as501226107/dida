/**
 * 
 */
package com.dream.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
  *  Class Name: TimeUtils.java
  *  Description: 
  *  @author
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
