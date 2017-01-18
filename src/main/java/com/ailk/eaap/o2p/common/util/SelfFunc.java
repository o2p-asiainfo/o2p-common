/** 
 * Project Name:o2p-serviceAgent-transformer 
 * File Name:FuncUtil.java 
 * Package Name:com.ailk.eaap.integration.o2p.transformer.common 
 * Date:2016年1月14日上午11:06:44 
 * Copyright (c) 2016, www.asiainfo.com All Rights Reserved. 
 * 
*/  
  
package com.ailk.eaap.o2p.common.util;  

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.asiainfo.foundation.log.Logger;

/** 
 * ClassName:ConcatFunc  
 * Function: TODO ADD FUNCTION.  
 * Reason:   TODO ADD REASON.  
 * Date:     2016年1月14日 上午11:06:44  
 * @author   wuwz 
 * @version   
 * @since    JDK 1.6 
 *        
 */
public  class SelfFunc {
	
	private static final Logger LOG = Logger.getLog(SelfFunc.class);
	
	public String concat(String str1,String str2,String str3,
			String str4,String str5,String str6,String str7,
			String str8,String str9,String str10) {
		
		return str1+str2+str3+str4+str5+str6+str7+str8+str9+str10;
	}
	
	public String subString(String value, int beginIndex, int endIndex) {
		
		return value.substring(beginIndex, endIndex);
	}
	
	public String lower(String value) {
		
		return value.toLowerCase();
	}

	public String upper(String value) {
		
		return value.toUpperCase();
	}
	
	public String dateFormat(String dateStr, String srcDateFormat, String tarDateFormat) {
		
		SimpleDateFormat src = new SimpleDateFormat(srcDateFormat);
		SimpleDateFormat tar = new SimpleDateFormat(tarDateFormat);
		
		Date date = null;  
		try {
			date = src.parse(dateStr);
			return tar.format(date);
		} catch (ParseException e) {
			LOG.error("dateFormat function parse date error:", e);
		}
		
		return null;
	}
	
	public String sysdate(String dateFormat) {
		
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		return format.format(date);
	}
	
	public static String Md5(String str1, String str2) throws NoSuchAlgorithmException {
		
		String str = str1+str2;
		StringBuffer result = new StringBuffer("");
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes());
		byte b[] = md.digest();
		int i = 0;
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				result.append("0");
			result.append(Integer.toHexString(i));
		}
		return result.toString();
	}
}
