package com.ailk.eaap.o2p.util.file.match.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import com.ailk.eaap.o2p.util.file.match.IFilenameMatcher;

public class FunctionFilenameMatcher implements IFilenameMatcher {
	private String funReg = "fn:[^(]+\\([^(]*\\)";
	public boolean match(String filename, String exp) {
		Pattern pattern = Pattern.compile(funReg);
        Matcher matcher = pattern.matcher(exp);
        String methodName = null;
        String[] param = null;
        String executeRet = null;
        while(matcher.find()) {
        	String functionGroup = matcher.group();
        	methodName = getMethodName(functionGroup);
        	param = getParams(functionGroup);
        	executeRet = executeMethod(FunctionEntity.class, methodName, param);
        	exp = exp.replace(functionGroup, executeRet);
        }
		return FilenameUtils.wildcardMatch(filename, exp);
	}
	
	/**
	 * 执行方法
	 * @param excClass 方法所在类
	 * @param methodName 方法名
	 * @param param 参数
	 * @return 执行返回的结果
	 */
	private String executeMethod(Class<FunctionEntity> excClass, String methodName, String[] param) {
		@SuppressWarnings("rawtypes")
		Class[] paramClass = null;
		try {
			paramClass = new Class[param.length];
			for(int i=0; i<paramClass.length; i++) {
				paramClass[i] = Class.forName("java.lang.String");
			}
			Method m = excClass.getMethod(methodName, paramClass);
			Object obj = (Object)excClass.newInstance();
			Object args[] = new Object[param.length];
			for(int i=0; i<args.length; i++) {
				args[i] = new String(param[i]);
			}
			return m.invoke(obj, args).toString();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("executeMethod " + methodName + ", class "+ excClass.getName()+" error", e);
		} catch (SecurityException e) {
			throw new RuntimeException("executeMethod " + methodName + ", class "+ excClass.getName()+" error", e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("executeMethod " + methodName + ", class "+ excClass.getName()+" error", e);
		} catch( InstantiationException e) {
			throw new RuntimeException("executeMethod " + methodName + ", class "+ excClass.getName()+" error", e);
		} catch( IllegalAccessException e) {
			throw new RuntimeException("executeMethod " + methodName + ", class "+ excClass.getName()+" error", e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("executeMethod " + methodName + ", class "+ excClass.getName()+" error", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("executeMethod " + methodName + ", class "+ excClass.getName()+" error", e);
		}
	}

	/**
	 * 从得到的函数字符串获取方法名
	 * @param functionGroup 函数字符串
	 * @return 方法名
	 */
	public String getMethodName(String functionGroup) {
		int endIndex = functionGroup.indexOf("(");
		return functionGroup.substring(3, endIndex);
	}
	
	/**
	 * 从得到的函数字符串获取参数
	 * @param functionGroup 函数字符串
	 * @return 参数数组
	 */
	public String[] getParams(String functionGroup) {
		int beginIndex = functionGroup.indexOf("(");
		int endIndex = functionGroup.indexOf(")");
		String subString = functionGroup.substring(beginIndex+1, endIndex);
		return subString.split(",");
	}
}
