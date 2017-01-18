package com.ailk.eaap.o2p.util.file.match.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FunctionEntity {
	/**
	 * 得到当前时间，并且格式化输出
	 * @param format 格式化的字符串，如yyyy-MM-dd
	 * @return 格式化后的字符串
	 */
	public String currentDate(String format) {
		if(format == null) return "";
		return new SimpleDateFormat(format).format(new Date());
	}
	
	/**
	 * 得到距离今天几天的日期，并且格式化输出
	 * @param format 格式化的字符串，如yyyy-MM-dd
	 * @param dateNum 距离今天几天的天数 eg:-1代表昨天,1代表明天
	 * @return 格式化后的字符串
	 */
	public String getDate(String dateNum, String format) {
		if(format == null || dateNum == null) return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, Integer.valueOf(dateNum));
		return new SimpleDateFormat(format).format(cal.getTime());
	}
}
