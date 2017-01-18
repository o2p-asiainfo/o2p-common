/** 
 * Project Name:o2p-common 
 * File Name:DateUtils.java 
 * Package Name:com.ailk.eaap.o2p.common.util 
 * Date:2014年12月27日下午4:23:40 
 * Copyright (c) 2014, www.asiainfo.com All Rights Reserved. 
 * 
*/  
  
package com.ailk.eaap.o2p.common.util;  

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.asiainfo.foundation.log.Logger;

/** 
 * ClassName:DateUtils  
 * Function: TODO ADD FUNCTION.  
 * Reason:   TODO ADD REASON.  
 * Date:     2014年12月27日 下午4:23:40  
 * @author   zhongming 
 * @version   
 * @since    JDK 1.6 
 *        
 */
public class DateUtils {
	private static final Logger LOG = Logger.getLog(DateUtils.class);
	/**
	 * getStringByDate:将Date格式化.  
	 * TODO(这里描述这个方法适用条件 – 可选). 
	 * TODO(这里描述这个方法的执行流程 – 可选). 
	 * TODO(这里描述这个方法的使用方法 – 可选). 
	 * TODO(这里描述这个方法的注意事项 – 可选). 
	 * 
	 * @author zhongming 
	 * @param format
	 * @param date
	 * @return 
	 * @since JDK 1.6
	 */
	public static String getStringByDate(String format, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	/**
	 * getDate:String转成Date.  
	 * TODO(这里描述这个方法适用条件 – 可选). 
	 * TODO(这里描述这个方法的执行流程 – 可选). 
	 * TODO(这里描述这个方法的使用方法 – 可选). 
	 * TODO(这里描述这个方法的注意事项 – 可选). 
	 * 
	 * @author zhongming 
	 * @param format
	 * @param date
	 * @return 
	 * @since JDK 1.6
	 */
	public static Date getDate(String format, String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			LOG.error("something bad happend",e);
		}
		return null;
	}
	/**
	 * getLong:Date转成Long.  
	 * TODO(这里描述这个方法适用条件 – 可选). 
	 * TODO(这里描述这个方法的执行流程 – 可选). 
	 * TODO(这里描述这个方法的使用方法 – 可选). 
	 * TODO(这里描述这个方法的注意事项 – 可选). 
	 * 
	 * @author zhongming 
	 * @param format
	 * @param date
	 * @return 
	 * @since JDK 1.6
	 */
	public static Long getLong(String format, Date date) {
		String str = getStringByDate(format, date);
		return getDate(format, str).getTime();
	}
	/**
	 * Timestamp转成format
	 * getTimestampFormat:(这里用一句话描述这个方法的作用).  
	 * TODO(这里描述这个方法适用条件 – 可选). 
	 * TODO(这里描述这个方法的执行流程 – 可选). 
	 * TODO(这里描述这个方法的使用方法 – 可选). 
	 * TODO(这里描述这个方法的注意事项 – 可选). 
	 * 
	 * @author zhongming 
	 * @param format
	 * @param timestamp
	 * @return 
	 * @since JDK 1.6
	 */
	public static String getTimestampFormat(String format, Timestamp timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);  
		return sdf.format(timestamp);
	}
	
	/**************************************************************************
   	 * 
   	 * 函数名：longToDateTimeString(long longTime)<br>
   	 * <br>
   	 * 功能：将长整形毫秒数转为时间对象<br>
   	 * <br>
   	 * 作者：wuzhy 2007-04-05<br>
   	 * <br>
   	 * 参数表：<br>
   	 * 		long longTime -> 毫秒数<br>
   	 * <br>
   	 * 返回值：<br>
   	 * 		Date -> 时间对象<br>
   	 * <br>
   	 * 修改记录：<br>
   	 * 		日期				修改人			修改说明<br>
   	 * 
   	 **************************************************************************/
	 public static Date longToDateTimeString(long longTime)
	 {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(longTime);
        return calendar.getTime();
	  }

}
