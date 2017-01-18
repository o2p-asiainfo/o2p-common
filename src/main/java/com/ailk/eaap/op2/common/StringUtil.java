package com.ailk.eaap.op2.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.asiainfo.foundation.log.Logger;


public class StringUtil {

	protected transient final static Logger logger = Logger.getLog(StringUtil.class);
	public final static int BUFFER_LEN = 4096; 
	private static char[] cacheByteBuffer = new char[BUFFER_LEN];
	private static boolean cacheByteBufferUsed = false;
	
	/**
	 * 判断是否可能是XML字符串。
	 * @param value
	 * @return
	 */
	public static boolean isPossibleXML(String value){
    	if(value == null) return false;
    	if((value.trim().charAt(0) == '<') && (value.trim().charAt(value.trim().length()-1)=='>')) return true;
    	else return false;
    }
    
    /**
     * 判断是否可能是JSON字符串。
     * @param value
     * @return
     */
    public static boolean isPossibleJSON(String value){
    	if(value == null) return false;
    	if((value.trim().charAt(0) == '[') && (value.trim().charAt(value.trim().length()-1)==']')) return true;
    	else if((value.trim().charAt(0) == '{') && (value.trim().charAt(value.trim().length()-1)=='}')) return true;
    	else return false;
    }
    
  
    
    /**
	 * 解析参数类型，如果没有配置参数类型，则根据输入的参数自动解析参数类型，但这可能会导致错误。
	 * @param paramTypes，格式如：“String,Integer...,POJO类名（带包路径）,String"
	 * @return
	 */
	public static Class<?>[] introspectParamTypes(String paramTypes){
		if(paramTypes == null) return null;
		
		//去掉空格，前后的括号
		String types = paramTypes.replaceAll("\\(|\\s|\\)", "");
		if("".equals(types)) return null;
		
		String[] typeArr = types.split(",");
		ArrayList<Class<?>> clzList = new ArrayList<Class<?>>();
		String type;
		for(String nameType : typeArr){
			String[] arrayNameType = nameType.split(":");	
			if(arrayNameType.length == 2) type = arrayNameType[1];
			else type = arrayNameType[0];
				
			if("String".equalsIgnoreCase(type)) clzList.add(String.class);
			else if("Integer".equalsIgnoreCase(type)) clzList.add(Integer.class);
			else if("Short".equalsIgnoreCase(type)) clzList.add(Short.class);
			else if("Long".equals(type)) clzList.add(Long.class);
			else if("Double".equals(type)) clzList.add(Double.class);
			else if("Float".equals(type)) clzList.add(Float.class);
			else if("int".equalsIgnoreCase(type)) clzList.add(int.class);
			else if("short".equalsIgnoreCase(type)) clzList.add(short.class);
			else if("long".equals(type)) clzList.add(long.class);
			else if("double".equals(type)) clzList.add(double.class);
			else {
				try {
					clzList.add(Class.forName(type));
				} catch (ClassNotFoundException e) {					
					logger.error("type:" + type + " not exists! Maybe the jar is not imported!");
					return null;
				}
			}
		}
		
		Class<?>[] clzs = new Class<?>[clzList.size()];
		return clzList.toArray(clzs);
	}
	
	
	
	public static String getString(InputStream inputStream, String charset) throws IOException {
		BufferedReader in = null;
		//char[]使用频繁,尽量使用缓存的char[],减少垃圾回收,优化无极限!
		char[] buffer = new char[BUFFER_LEN]; //getCachedByteBuffer();
		try {
			in = new BufferedReader(new InputStreamReader(inputStream, charset));
	        StringBuilder sb = new StringBuilder();
	        
	        int len = 0;
	        while ((len = in.read(buffer)) > 0) {
	            sb.append(buffer, 0, len);
	        }
			return sb.toString();
		} finally {
			if (in != null)
				in.close();
			if (inputStream != null) {
				inputStream.close();
			}
//			releaseCachedByteBuffer(buffer);
			buffer = null;
		}
	}
	
	/**
	 * 取得char[]数组,别的线程已经在用的话，只好自己new一个罗
	 * @return
	 * @author zhaoxin
	 */
    private static synchronized char[] getCachedByteBuffer() {
        synchronized(cacheByteBuffer) {
            if (!cacheByteBufferUsed) {
            	cacheByteBufferUsed = true;
                return cacheByteBuffer;
            }
        }
        
        return new char[BUFFER_LEN];
    }
    /**
     * 是否cached的char[]
     * @param buffer
     */
    private static void releaseCachedByteBuffer(char[] buffer) {
        synchronized(cacheByteBuffer) {
            if (buffer == cacheByteBuffer) {
            	cacheByteBufferUsed = false;
            }
        }
    }
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


}
