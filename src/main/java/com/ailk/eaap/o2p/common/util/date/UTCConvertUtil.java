package com.ailk.eaap.o2p.common.util.date;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;

import com.ailk.eaap.o2p.common.util.date.UTCTimeUtil.DateType;
import com.ailk.eaap.op2.bo.utc.DateConvertBeanImpl;
import com.ailk.eaap.op2.bo.utc.DateConvertField;
import com.asiainfo.foundation.common.ExceptionCommon;
import com.asiainfo.foundation.exception.BusinessException;
import com.asiainfo.foundation.log.LogModel;
import com.asiainfo.foundation.log.Logger;

/** 
 * @ClassName: UTCConvertUtil 
 * @Description: UTC时间转化的工具类，主要提供给UTC时间处理拦截器内使用
 * @author zhengpeng
 * @date 2014-10-31 下午5:22:34 
 * @version V1.0  
 */
public class UTCConvertUtil {
	
	private static final Logger log = Logger.getLog(UTCConvertUtil.class); 
	
	/**
	 * 对Object里的时间进行转换
	 * @param paramObj
	 * @param timeOffest
	 * @param dateType
	 */
	public static void checkObject(Object paramObj,int timeOffest,DateType dateType){
		if(paramObj instanceof HashMap){ 
			UTCConvertUtil.checkHashMap((HashMap)paramObj, timeOffest,dateType); 
		}else if(paramObj instanceof List){
			UTCConvertUtil.checkList((List) paramObj, timeOffest,dateType);
		} else if(paramObj instanceof DateConvertBeanImpl){
			UTCConvertUtil.checkObjectContent(paramObj, timeOffest,dateType);
		}
	}
	
	/**
	 * 对List对象进行时间转换
	 * @param paramList
	 * @param timeOffest
	 * @param dateType
	 */
	public static void checkList(List paramList,int timeOffest,DateType dateType){
		for(Object paramObject : paramList){
			if(paramObject instanceof HashMap){
				UTCConvertUtil.checkHashMap((HashMap) paramObject, timeOffest,dateType);
			}else if(paramObject instanceof DateConvertBeanImpl){
				UTCConvertUtil.checkObjectContent(paramObject, timeOffest, dateType);
			}else if(paramObject instanceof JSONObject){
				UTCConvertUtil.checkJSONObject((JSONObject) paramObject, timeOffest,dateType);
			}
		}
	}
	
	/**
	 * 对对象里的时间属性进行UTC判断和转换
	 * @param paramObject
	 * @param timeOffest
	 * @param dateType
	 */
	public static void checkObjectContent(Object paramObject,int timeOffest, DateType dateType) {
		Class objectClass = paramObject.getClass();
		Field[] fields = objectClass.getDeclaredFields();
		for (Field field : fields) {
			if (field.getAnnotation(DateConvertField.class) != null) {
				try {
					Object fieldValue = 
							PropertyUtils.getSimpleProperty(paramObject, field.getName());
					if(fieldValue != null){
						//处理属性为字符类型
						if(fieldValue instanceof String){
							String value = UTCConvertUtil.getConvertDateStr((String)fieldValue, timeOffest, dateType);
							PropertyUtils.setSimpleProperty(paramObject, field.getName(), value);
						//处理属性为Date类型
						}else if(fieldValue instanceof Date){
							Date value = UTCTimeUtil.convertDateByType((Date) fieldValue, timeOffest,dateType);
							if(fieldValue instanceof java.sql.Timestamp){
								java.sql.Timestamp timestamp = new java.sql.Timestamp(value.getTime());
								PropertyUtils.setSimpleProperty(paramObject, field.getName(), timestamp); 
							}else{
								PropertyUtils.setSimpleProperty(paramObject, field.getName(), value);
							}
						}
					}
				} catch (Exception e) {
					log.error(LogModel.EVENT_APP_EXCPT, new BusinessException(ExceptionCommon.WebExceptionCode,"UtcAspectForSpring checkObjectContent:" + e.getMessage(),null));
				} 
			}
		}
	}
	
	/**
	 * 对HashMap对象进行时间进行转换
	 * @param paramMap
	 * @param timeOffest
	 * @param dateType  
	 */
	public static void checkHashMap(HashMap paramMap,int timeOffest,DateType dateType){
		 Set<Map.Entry<String, Object>> entries = paramMap.entrySet();
		 for(Map.Entry<String, Object> entry : entries){
			 Object value = entry.getValue();
			 if(value != null){
				 //value值为时间类型
				 if(value instanceof Date){
					 Date tempDate = (Date) value;
					 value = UTCTimeUtil.convertDateByType(tempDate, timeOffest,dateType);
					 
					 if(tempDate instanceof java.sql.Timestamp){
						 java.sql.Timestamp timestamp = new java.sql.Timestamp(((Date) value).getTime());
						 paramMap.put(entry.getKey(), timestamp); 
					 }else{
						 paramMap.put(entry.getKey(), value);
					 }
				 //value值为对象或String
				 }else if(value instanceof String){
					 String valueStr = UTCConvertUtil.getConvertDateStr((String)value, timeOffest, dateType);
					 paramMap.put(entry.getKey(),valueStr);
				 } 
			 }
		 }
	}
	public static void checkJSONObject(JSONObject paramMap,int timeOffest,DateType dateType){
		 Set<Map.Entry<String, Object>> entries = paramMap.entrySet();
		 for(Map.Entry<String, Object> entry : entries){
			 Object value = entry.getValue();
			 if(value != null){
				 //value值为时间类型
				 if(value instanceof Date){
					 Date tempDate = (Date) value;
					 value = UTCTimeUtil.convertDateByType(tempDate, timeOffest,dateType);
					 
					 if(tempDate instanceof java.sql.Timestamp){
						 java.sql.Timestamp timestamp = new java.sql.Timestamp(((Date) value).getTime());
						 paramMap.put(entry.getKey(), timestamp); 
					 }else{
						 paramMap.put(entry.getKey(), value);
					 }
				 //value值为对象或String
				 }else if(value instanceof String){
					 String valueStr = UTCConvertUtil.getConvertDateStr((String)value, timeOffest, dateType);
					 paramMap.put(entry.getKey(),valueStr);
				 } 
			 }
		 }
	}
	
	/**
	 * 对字符格式的时间进行转化
	 * @param value
	 * @param timeOffest
	 * @param dateType
	 * @return
	 */
	public static String getConvertDateStr(String value,int timeOffest,DateType dateType){
		 
		 //对String值进行验证，是否是日期格式的 ，处理“yyyy-MM-dd HH:mm”
		 if(isDefaultDate(value)){
			 value = UTCTimeUtil.convertDateStrByType(value, timeOffest, UTCTimeUtil.DEFAULT_PATTERNS,dateType);
			 
			//对String值进行验证，是否是日期格式的 ，处理“yyyy-MM-dd HH:mm:ss”
		 }else if(isFullDate(value)){
			 value = UTCTimeUtil.convertDateStrByType(value, timeOffest, UTCTimeUtil.getFullPaterns(),dateType);
			 
			//对String值进行验证，是否是日期格式的 ，处理“yyyy-MM-dd HH:mm:ss.SSS” add by niezh at 20150326
		 }else if(isFullMsDate(value)){
			 value = UTCTimeUtil.convertDateStrByType(value, timeOffest, UTCTimeUtil.getFullPaternsms(),dateType);
		 }
		 return value;
	}
	/**
	 * 判断字符串是否为“yyyy-MM-dd HH:mmss.SSS”格式		
	 * @param dateStr
	 * @return
	 */
	public static boolean isFullMsDate(String dateStr) {
		boolean match = false;
		Pattern pattern = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2})\\s([0-5]\\d):([0-5]\\d):([0-5]\\d).\\d{3}$");
		Matcher matcher = pattern.matcher(dateStr);
		if (matcher.matches()) {
			match = true;
		} 
		return match;
	}
	/**
	 * 判断字符串是否为“yyyy-MM-dd HH:mm：ss”格式		
	 * @param dateStr
	 * @return
	 */
	public static boolean isFullDate(String dateStr) {
		boolean match = false;
		Pattern pattern = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2})\\s([0-5]\\d):([0-5]\\d):([0-5]\\d)$");
		Matcher matcher = pattern.matcher(dateStr);
		if (matcher.matches()) {
			match = true;
		} 
		return match;
	}
	
	/**
	 * 判断字符串是否为“yyyy-MM-dd HH:mm”格式
	 * @param dateStr
	 * @return
	 */
	public static boolean isDefaultDate(String dateStr){
		boolean match = false;
		Pattern pattern = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2})\\s([0-5]\\d):([0-5]\\d)$");
		Matcher matcher = pattern.matcher(dateStr);
		if (matcher.matches()) {
			match = true;
		} 
		return match;
	}

}
