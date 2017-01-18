package com.ailk.eaap.o2p.common.util.date;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import com.ailk.eaap.o2p.common.spring.config.CustomPropertyConfigurer;
import com.asiainfo.foundation.common.ExceptionCommon;
import com.asiainfo.foundation.exception.BusinessException;
import com.asiainfo.foundation.log.LogModel;
import com.asiainfo.foundation.log.Logger;
import com.asiainfo.foundation.util.ExceptionUtils;

/** 
 * @ClassName: UTCTimeUtil 
 * @Description: UTC和本地时间转换的工具类
 * @author zhengpeng
 * @date 2014-10-27 上午10:12:00 
 * @version V1.0  
 */
public class UTCTimeUtil {
	private static final Logger log = Logger.getLog(UTCTimeUtil.class);
	protected static final String[] DEFAULT_PATTERNS = {"yyyy-MM-dd HH:mm"};
	public static final String[] SIMPLE_PATTERNS = {"yyyy-MM-dd"}; 
	private static final String[] FULL_PATERNS = {"yyyy-MM-dd HH:mm:ss"};
	private static final String[] FULL_PATERNS_MS = {"yyyy-MM-dd HH:mm:ss.SSS"};
	
	public static String[] getFullPaterns(){
		return FULL_PATERNS;
	}
	
	public static String[] getFullPaternsms(){
		return FULL_PATERNS_MS;
	}
	
	public static String[] getDefaultPatterns(){
		return DEFAULT_PATTERNS;
	}
	
	
	private UTCTimeUtil(){
		
	}
	public enum DateType{
		//本地时间类型
		LOCAL_DATE(1),  
		//UTC时间类型
		UTC_DATE(2);
		
		private int value;
		private DateType(int value){
			this.value = value;
		}
		
		public String toString(){
			return String.valueOf(this.value);
		}
	}
	/**
	 * 根据时间类型将本地时间字符转UTC时间字符，或UTC时间字符转本地时间字符
	 * @param dateStr
	 * @param timeOffset
	 * @param patterns
	 * @param dateType  
	 * @return
	 */
	public static String convertDateStrByType(String dateStr,int timeOffset,String[] patterns,DateType dateType){
		if(dateType == DateType.LOCAL_DATE){
			 return UTCTimeUtil.getUTCStrByLocalDateStr(dateStr, timeOffset, patterns);
		 }else if(dateType == DateType.UTC_DATE){
			 return UTCTimeUtil.getLocalDateStrByUTCStr(dateStr, timeOffset, patterns);
		 }else{
			 return dateStr;
		 }
	}
	
	/**
	 * 根据时间类型将本地时间转UTC时间，或UTC时间转本地时间
	 * @param date
	 * @param timeOffset
	 * @param dateType
	 * @return
	 */
	public static Date convertDateByType(Date date,int timeOffset,DateType dateType){
		 if(dateType == DateType.LOCAL_DATE){
			 return UTCTimeUtil.getUTCByLocalDate(date, timeOffset);
		 }else if(dateType == DateType.UTC_DATE){
			 return UTCTimeUtil.getLocalDateByUTC(date, timeOffset);
		 }else{
			 return date;
		 }
	 }
	
	
	/**
	 * 根据UTC时间和时间偏移量获得本地时间
	 * @param utcDate UTC时间
	 * @param timeOffset 时间偏移量（包含夏令时）
	 * @return
	 */
	public static Date getLocalDateByUTC(Date utcDate,int timeOffset){
		if(utcDate != null){
			Calendar cal=Calendar.getInstance();
			cal.setTime(utcDate);
			cal.add(Calendar.MINUTE, -timeOffset);
			return cal.getTime();
		}
		return null;
	}
	
	/**
	 * 根据UTC时间String和时间偏移量获得本地时间的String
	 * @param localDateStr
	 * @param timeOffset
	 * @return
	 */
	public static String getLocalDateStrByUTCStr(String utcDateStr,int timeOffset,String[] patterns){
		String localDateStr = utcDateStr;
		try {
			Date utcDate = DateUtils.parseDate(utcDateStr, patterns);
			Calendar cal = Calendar.getInstance();
			cal.setTime(utcDate);
			cal.add(java.util.Calendar.MINUTE, -timeOffset);
			localDateStr = DateFormatUtils.format(cal, patterns[0]); 
		} catch (ParseException e) {
			String error = "UTCTimeUtil getLocalDateStrByUTCStr utcDateStr:" + utcDateStr + " || timeOffset:" + timeOffset;
			log.error(LogModel.EVENT_APP_EXCPT, new BusinessException(ExceptionCommon.WebExceptionCode,error + e.getMessage(),null));
		} 
		return localDateStr;
	}
	
	/**
	 * 根据本地时间和时间偏移量获得UTC时间
	 * @param localDate 本地的时间
	 * @param timeOffset 时间偏移量（包含夏令时）
	 * @return
	 */
	public static Date getUTCByLocalDate(Date localDate,int timeOffset){
		Calendar cal=Calendar.getInstance();
		cal.setTime(localDate);
		cal.add(Calendar.MINUTE, timeOffset);
		return cal.getTime();
	}
	
	/**
	 * 根据本地时间String和时间偏移量获得UTC时间的String
	 * @param localDateStr
	 * @param timeOffset
	 * @return
	 */
	public static String getUTCStrByLocalDateStr(String localDateStr,int timeOffset,String[] patterns){
		String utcDateStr = localDateStr;
		try {
			Date localDate = DateUtils.parseDate(localDateStr, patterns);
			Calendar cal = Calendar.getInstance();
			cal.setTime(localDate);
			cal.add(java.util.Calendar.MINUTE, timeOffset);
			utcDateStr = DateFormatUtils.format(cal, patterns[0]); 
		} catch (ParseException e) {
			log.error("parse dataFormat exception:{0}", ExceptionUtils.populateExecption(e, 500));
		} 
		return utcDateStr;
	}
	
	
	/**
	 * 获取系统的UTC时间
	 * @return
	 */
	public static Date getUTCDate(){
		//取得本地时间：    
	    Calendar calendar = Calendar.getInstance();   
	    //取得时间偏移量：    
	    int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);   
	    //取得夏令时差：    
	    final int dstOffset = calendar.get(Calendar.DST_OFFSET);     
	    //从本地时间里扣除这些差量，即可以取得UTC时间：    
	    calendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));    
	    return calendar.getTime();  
	}
	
	/**
	 * 得到UTC时间，类型为字符串，格式为"yyyy-MM-dd HH:mm"
	 * 如果获取失败，返回null
	 * @return
	 */
	public static String getUTCDateStr(){
		Date date = UTCTimeUtil.getUTCDate();
		return DateFormatUtils.format(date, DEFAULT_PATTERNS[0]); 
	}
	
	/**
	 * 得到UTC时间，类型为字符串
	 * 如果获取失败，返回null
	 * @return
	 */
	public static String getUTCDateStrByFormat(String[] patterns){
		Date date = UTCTimeUtil.getUTCDate();
		return DateFormatUtils.format(date, patterns[0]); 
	}


	/**
	 * 将指定时间转化成UTC，偏移量为操作系统的时区偏移量
	 * @param dateTime
	 * @return  Date
	 */
	public static  Date getUTCTime(Date dateTime) {
		java.util.Calendar cal = java.util.Calendar.getInstance();
		int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
		int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
		int timeOffset=-(zoneOffset + dstOffset)/(60*1000);
		return UTCTimeUtil.getUTCByLocalDate(dateTime, timeOffset);	
	}
	
	/**
	 * 将指定时间转化成UTC，偏移量为操作系统的时区偏移量
	 * @param dateTime
	 * @return Timestamp
	 */
		public static  Timestamp getUTCTimestamp(Date dateTime) {
			java.util.Calendar cal = java.util.Calendar.getInstance();
			int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
			int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
			int timeOffset=-(zoneOffset + dstOffset)/(60*1000);
			return new Timestamp(UTCTimeUtil.getUTCByLocalDate(dateTime, timeOffset).getTime());
		}
		
		/**
		 * 获取系统的UTC时间
		 * @return
		 */
		public static Timestamp getUTCTimestamp(){
			//取得本地时间：    
		    Calendar calendar = Calendar.getInstance();   
		    //取得时间偏移量：    
		    int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);   
		    //取得夏令时差：    
		    final int dstOffset = calendar.get(Calendar.DST_OFFSET);     
		    //从本地时间里扣除这些差量，即可以取得UTC时间：    
		    calendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));    
		    return new Timestamp(calendar.getTime().getTime());  
		}
		
		public static String getHadoopLogUTCTimeStr(String HadoopLogTime,int timeOffset,String[] patterns){
			String ctrUTCTime=CustomPropertyConfigurer.getProperty("ctrUTCTime");
			//转为UTC时间
			if("true".equals(ctrUTCTime)){
				return convertDateStrByType(HadoopLogTime,timeOffset,patterns,DateType.LOCAL_DATE);
			//不需要转，直接返回原时间
			}else{
				return HadoopLogTime;
			}
		}
		public static void main(String[] args) {
			//convert to utc time by niezh at 20150326
			//utc time
			Date utcDate=UTCTimeUtil.getUTCDate();
			Calendar cal = Calendar.getInstance();   
			cal.setTime(utcDate);
			cal.set(Calendar.HOUR_OF_DAY , cal.get(Calendar.HOUR_OF_DAY) - 1 ) ; //把时间设置为当前时间-1小时，同理，也可以设置其他时间
			//utc to client time
		}
		
		public static Timestamp getTimestamp(Date date, boolean ctrUTCTime){
			if(ctrUTCTime){
				return getUTCTimestamp(date);
			}else{
				return new Timestamp(date.getTime());
			}
		}
}
