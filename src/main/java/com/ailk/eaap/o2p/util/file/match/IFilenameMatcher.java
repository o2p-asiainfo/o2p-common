package com.ailk.eaap.o2p.util.file.match;

public interface IFilenameMatcher {
	
	/**
	 * 匹配方法名
	 * @param filename 文件名
	 * @param exp 匹配表达式
	 * @return 是否匹配
	 */
	public boolean match(String filename, String exp);
}
