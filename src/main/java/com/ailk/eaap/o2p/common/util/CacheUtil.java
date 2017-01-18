package com.ailk.eaap.o2p.common.util;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;

import com.ailk.eaap.o2p.common.cache.CacheKey;
import com.ailk.eaap.o2p.common.cache.ICache;
import com.ailk.eaap.o2p.common.spring.config.ZKCfgCacheHolder;
import com.ailk.eaap.op2.bo.SerInvokeIns;
import com.asiainfo.foundation.util.RandomUtils;

public final class CacheUtil {
	
	private static String serverIpPort = "";
	private static String runningId = "";
	private static String runningCount = "";
	private static String lastRunningId = "";
	private static String lastRunningCount = "";
	private static int stopCount = 0;
	private static String type = "";
	private static final String KEY_TOKEN = "KEY_TOKEN";
	
	private static final Log log = LogFactory.getLog(CacheUtil.class);
	
	private CacheUtil(){}
	
	public static String getPort() {
    	try{
	    	// 读取容器xml
	    	String url = readHomeProperty();
	    	if(url==null){
	    		log.error("There is error for get url!");
	    		return null;
	    	}
	    	// 解析XML文件获取端口
	    	String port = analyzeConfigXML(url);
	    	if(StringUtils.isEmpty(port)){
	    		log.error("There is error to get server port!");
	    		return null;
	    	}
	    	return port;
    	}catch(Exception e){
    		log.error("error occured!", e);
    	}
    	return null;
    }
	
	// 根据home路径 获取配置文件路径，解析端口
 	public static String analyzeConfigXML(String url) throws DocumentException, IOException {
 		if ("".equals(type)) {
 			type = (String) ZKCfgCacheHolder.PROP_ITEMS.get("Container_Type");
 		}
 		if (type.equals(CacheKey.TOMCAT)) {
 			return LocalUtils.getLocalTomcatPort(url);
 		} else if (type.equals(CacheKey.WEBLOGIC)) {
 			return LocalUtils.getLocalWebLogicPort(url);
 		}
 		return "";
 	}
	
	// 读取容器参数home路径
	public static String readHomeProperty() {
		if (ZKCfgCacheHolder.PROP_ITEMS.get("Container_Type") == null) {
			log.error("you must set param Container_Type in config file!");
			return null;
		}
		type = (String) ZKCfgCacheHolder.PROP_ITEMS.get("Container_Type");
		String url = "";
		if (type.equals(CacheKey.TOMCAT)) {
			url = System.getProperty("catalina.base") + "/conf/server.xml";
		} else if (type.equals(CacheKey.WEBLOGIC)) {
			url = System.getProperty("user.dir") + "/config/config.xml";
		}
		
		if(log.isDebugEnabled()){
			log.debug(url);
		}
		url = url.replace('\\', '/');
				
		if (!new File(url).exists()) {
			log.error("error occured when get container configuration file!");
			return null;
		}
		return url;
	}
	
	public static boolean isNeedSynchronize(ICache<String, Object> cache, String mName){
		try{
			Object obj = cache.get(mName);
			if(obj==null){
				cache.put(mName, 1);
				if(StringUtils.isEmpty(serverIpPort)){
					serverIpPort = getServerId();
				}
				cache.put(mName + "_RUNNING_ID", serverIpPort);
				cache.put(mName + "_RUNNING_COUNT", 1);
				return true;
			}else{
				runningId = (String)cache.get(mName + "_RUNNING_ID");
				if(StringUtils.isEmpty(serverIpPort)){
					serverIpPort = CacheUtil.getServerId();
				}
				if(runningId.equals(serverIpPort)){
					cache.put(mName + "_RUNNING_COUNT", 1 + (Integer.valueOf(cache.get(mName + "_RUNNING_COUNT").toString())));
					stopCount = 0;
					return true;
				}else{
					runningCount = cache.get(mName + "_RUNNING_COUNT").toString();
					if(runningId.equals(lastRunningId) && runningCount.equals(lastRunningCount)){
						stopCount = stopCount + 1;
						if(stopCount>2){
							String nowRunningId = cache.get(mName + "_RUNNING_ID").toString();
							if(nowRunningId.equals(runningId)){
								cache.put(mName + "_RUNNING_ID", serverIpPort);
								cache.put(mName + "_RUNNING_COUNT", 1);
								return true;
							}
						}
						return false;
					}else{
						stopCount = 0;
						lastRunningId = runningId;
						lastRunningCount = runningCount;
						return false;
					}
				}
			}
		}catch(Exception e){
			log.error("error occured!", e);
			return false;
		}
	}

    public static String getFirstDay(long longTime){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();   
        calendar.setTimeInMillis(longTime);      
        Date theDate =calendar.getTime();
     
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
        return str.toString();

    }
    
    public static String getLastDay(long longTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(longTime);      
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));        
        Date theDate =calendar.getTime();              
        String s = df.format(theDate);
        StringBuffer str = new StringBuffer().append(s).append(" 23:59:59");
        return str.toString();
    }
    
    public static String getServerId() {
    	try{
    		if(LocalUtils.getLocalRealIp()==null || getPort()==null){
    			throw new Exception("get ip or port error!");
    		}
    		return LocalUtils.getLocalRealIp() + getPort();
    	}catch(Exception e){
    		log.error(e);
    		final int offset = (int) ((RandomUtils.nextInt()*9+1)*100000); 
			long seed = System.currentTimeMillis() + offset; 
			SecureRandom sr;
			try {
				sr = SecureRandom.getInstance("SHA1PRNG");
				sr.setSeed(seed);
				return String.valueOf(sr.nextInt());
			} catch (NoSuchAlgorithmException e1) {
				log.error(e1);
				return null;
			}
    	}
    }
    
	public static String getTokenKey(SerInvokeIns sii, String tenantId, String username){
		return KEY_TOKEN + sii.getComponentCode() + tenantId + username + sii.getSerInvokeInsId().toString();
	}
}
