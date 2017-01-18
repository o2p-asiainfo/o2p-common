package com.ailk.eaap.o2p.util.file.filters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.wltea.expression.ExpressionEvaluator;
import org.wltea.expression.datameta.Variable;

import com.ailk.eaap.o2p.util.file.match.FilenameMatcherFactory;
import com.ailk.eaap.o2p.util.file.match.IFilenameMatcher;
import com.asiainfo.foundation.exception.BusinessException;
import com.asiainfo.foundation.log.LogModel;
import com.asiainfo.foundation.log.Logger;
import com.linkage.rainbow.util.StringUtil;

public class FileFilterExpression {
	
	private static final Logger LOG = Logger.getLog(FileFilterExpression.class);
	
	private FileFilterExpression(){
		
	}
	public static String validateExpression(String filterExpression) {
		
		boolean valid = true;
		String retStr = filterExpression;
		if(filterExpression == null) {
			return "";
		}
		
		while(filterExpression.startsWith("&&") || filterExpression.startsWith("||")) {
			valid = false;
			filterExpression = filterExpression.substring(2);
			
		}
		
		if(!valid) {
			LOG.warn("Illegal begin: '&&' or '||', which will delete");
			valid = true;
		}
		while(filterExpression.endsWith("&&") || filterExpression.endsWith("||")) {
			valid = false;
			filterExpression = filterExpression.substring(0,filterExpression.length()-2);
		}
		if(!valid) {
			LOG.warn("Illegal end: '&&' or '||', which will delete");
			valid = true;
		}
		
		while(filterExpression.contains("&&&&") || filterExpression.contains("||||")) {
			valid = false;
			filterExpression = filterExpression.replaceAll("\\&\\&\\&\\&", "\\&\\&").replaceAll("\\|\\|\\|\\|", "\\|\\|");
		}
		if(!valid) {
			LOG.warn("Illegal substring: '&&&&' or '||||', which will be replaced with '&&' or '||'");
			valid = true;
		}
		while(filterExpression.contains("&&||") || filterExpression.contains("||&&")) {
			valid = false;
			filterExpression = filterExpression.replaceAll("\\&\\&\\|\\|", "\\&\\&").replaceAll("\\|\\|\\&\\&", "\\|\\|");
		}
		
		if(!valid) {
			LOG.warn("Illegal substring: '&&||' or '||&&', which will be replaced with '&&' or '||'");
			valid = true;
		}
		
		return filterExpression;
	}
	
	public static boolean validateFileName(String fileName, String filterExpression) {
		if(StringUtil.isBlank(filterExpression)){
			return true;
		}
		String[] expressions = filterExpression.split("\\&\\&|\\|\\|");
		List<Boolean> expressionsBooleanList = new ArrayList<Boolean>(expressions.length);
		//运算所有子表达式中非的值，并加入到expressionsBooleanList中
		StringBuffer expression = null;
		IFilenameMatcher filenameMatcher = null;
		for(int i=0; i<expressions.length; i++) {
			if("".equals(expressions[i])) {
				continue;
			}
			int notCount = 0;
			expression = new StringBuffer(expressions[i]);
			if(expressions[i].charAt(0) == '!') {
				for(int j=0; j<expressions[i].length(); j++) {
					if(expressions[i].charAt(j) == '!') {
						notCount++;
					}
					else {
						break;
					}
				}
				expression = new StringBuffer(expressions[i].substring(notCount));
			}
			filenameMatcher = FilenameMatcherFactory.getInstance().getFilenameMatch(expression);
			expressions[i] = expression.toString();
			if(notCount % 2 == 0) expressionsBooleanList.add(filenameMatcher.match(fileName, expressions[i]));
			else expressionsBooleanList.add(!filenameMatcher.match(fileName, expressions[i]));
		}
		boolean resultValidate = expressionsBooleanList.get(0);
		int index = 1;
		if(expressionsBooleanList.size() == 1) {
			return resultValidate;
		}
		for(int i=0; i<filterExpression.length(); i++) {
			char c = filterExpression.charAt(i);
			if(c == '&' && i < filterExpression.length()-1 && filterExpression.charAt(i+1) == '&') {
				resultValidate = resultValidate & expressionsBooleanList.get(index);
				index++;
				i++;
			} else if(c == '|' && i < filterExpression.length()-1 && filterExpression.charAt(i+1) == '|') {
				resultValidate = resultValidate | expressionsBooleanList.get(index);
				index++;
				i++;
			}
		}
		return resultValidate;
	}
	
	/**
	 * 格式化文件名称
	 * @param fileName
	 * @param dirBean
	 * @return
	 */
	public static String getFileName(String fileName,String fileFormats) throws Exception{
		String filename = null;										//格式化后文件名称
		String name =null;
		String suffix = "";
		Assert.hasText(fileName, "the parameter fileName:"+fileName+" must not be empty!");
		int flag = fileName.lastIndexOf('.');
		if(flag != -1) {
			filename = fileName.substring(0,flag);
			suffix = fileName.substring(flag+1,fileName.length());	//获取后缀名
		}
		
		if(suffix.length() == 0){
			filename = fileName;
		}
		try {
			if(fileFormats==null || "".equals(fileFormats)){
				name = filename+new SimpleDateFormat("_yyyyMMdd").format(new Date());
				if(suffix.length() > 0) name = name + "."+suffix;
			}else{
				LOG.debug("filename"+filename+"suffix"+suffix+"fileFormats"+fileFormats);
				name =fileExpr(filename, suffix,fileFormats);
				LOG.debug("name"+name);
			}
			return name;
		} catch (Exception e) {
			String errorInfo="Expression does not regulate "+fileFormats;
			BusinessException be = new BusinessException(9211, errorInfo, e.getCause());
			LOG.error(LogModel.EVENT_APP_EXCPT,be);
			throw be;
		}
	}
	
	/**
	 * IKexpression 解析表达式
	 * @param filename
	 * @param expr
	 * @return
	 */
	public static String fileExpr(String filename,String suffix, String expression){
		String retStr = "";
	    List<Variable> variables = new ArrayList<Variable>();  
	    variables.add(Variable.createVariable("filename", filename));
	    if(!expression.contains("changeSuffix") && suffix.length() > 0) {
	    	retStr = expression + "+$changeSuffix(suffix)";
	    	variables.add(Variable.createVariable("suffix", suffix));
	    }
	    //执行表达式  
	    Object result = ExpressionEvaluator.evaluate(retStr,variables);  
	    return result == null ? "" : result.toString();
	}

}
