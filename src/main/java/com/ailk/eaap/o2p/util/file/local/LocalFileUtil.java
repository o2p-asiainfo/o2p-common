package com.ailk.eaap.o2p.util.file.local;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class LocalFileUtil {
	
	/**
	 *  递归扫描子目录下的文件
	 * 
	 * @author mf 
	 * @param fileArray 文件数组
	 * @param filterExpression 过滤表达式
	 * @since JDK 1.6
	 */
	public static File[] dealWithDirectoryFile(File[] fileArray,String filterExpression){
		List<File> fileList = new ArrayList<File>();	
		//先检查本地目录有没有需要处理的文件
		for (int i = 0; i < fileArray.length; i++) {
			File locf = fileArray[i];
			if(validateFileName(locf.getName(),filterExpression)){
				fileList.add(locf);
			}else{
				FileUtils.deleteQuietly(locf);
			}
		}
		
		return  (File[])fileList.toArray(new File[fileList.size()]);
	}
	
	
	/**
	 * 
	 * 过滤文件规则  
	 * @author mf 
	 * @param fileName  文件路径
	 * @param filterExpression 过滤表达式
	 * @return 
	 * @since JDK 1.6
	 */
	public static boolean validateFileName(String fileName, String filterExpression) {
		if(filterExpression==null || "".equals(filterExpression)){
			return true;
		}
		String[] expressions = filterExpression.split("\\&\\&|\\|\\|");
		List<Boolean> expressionsBooleanList = new ArrayList<Boolean>(expressions.length);
		//运算所有子表达式中非的值，并加入到expressionsBooleanList中
		for(int i=0; i<expressions.length; i++) {
			if("".equals(expressions[i])) {
				continue;
			}
			if(expressions[i].charAt(0) == '!') {
				int notCount = 0;
				for(int j=0; j<expressions[i].length(); j++) 
					if(expressions[i].charAt(j) == '!') {
						notCount++;
					}
					else {
						break;
					}
				if(notCount % 2 == 0) {
					expressionsBooleanList.add(FilenameUtils.wildcardMatch(fileName, expressions[i].substring(notCount)));
				}
				else {
					expressionsBooleanList.add(!FilenameUtils.wildcardMatch(fileName, expressions[i].substring(notCount)));
				}
			} else expressionsBooleanList.add(FilenameUtils.wildcardMatch(fileName, expressions[i]));
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

}
