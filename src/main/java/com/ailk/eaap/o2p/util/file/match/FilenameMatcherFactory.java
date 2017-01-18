package com.ailk.eaap.o2p.util.file.match;
import com.ailk.eaap.o2p.util.file.match.impl.FunctionFilenameMatcher;
import com.ailk.eaap.o2p.util.file.match.impl.RegexpFilenameMatcher;
import com.ailk.eaap.o2p.util.file.match.impl.WildcardFilenameMatcher;

public class FilenameMatcherFactory {
	
	private FilenameMatcherFactory(){}
	private static FilenameMatcherFactory filenameMatcherFactory;
	
	public static FilenameMatcherFactory getInstance() {
		if(filenameMatcherFactory == null) return new FilenameMatcherFactory();
		else return filenameMatcherFactory;
	}
	
	/**
	 * 获取指定的过滤器，并格式化表达式
	 * @param expression 过滤表达式
	 * @return
	 */
	public IFilenameMatcher getFilenameMatch(StringBuffer expression) {
		if(expression == null) return new WildcardFilenameMatcher();
		else if(expression.toString().toUpperCase().matches("^F\\{\\S+\\}$")) {
			expression.delete(0, 2).delete(expression.length()-1, expression.length());
			return new FunctionFilenameMatcher();
		} else if(expression.toString().toUpperCase().matches("^R\\{\\S+\\}$")) {
			expression.delete(0, 2).delete(expression.length()-1, expression.length());
			return new RegexpFilenameMatcher();
		} else return new WildcardFilenameMatcher();
	}
}
