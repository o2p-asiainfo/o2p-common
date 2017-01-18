/*
 * Created on 2013-2-4
 * ExcelInfo.java V1.0
 *
 * Copyright Notice =========================================================
 * This file contains proprietary information of Eastcom Technologies Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2012 =======================================================
 */
package com.ailk.eaap.op2.common;

import java.util.Arrays;
import java.util.List;

/**
 * 导出Excel的信息
 * Title: eed <br>
 * Description: <br>
 * Copyright: eastcom Copyright (C) 2009 <br>
 * 
 * @author <a href="mailto:邮箱">jiangxd</a><br>
 * @e-mail: jiangxd@eastcom-sw.com<br>
 * @version 2.0 <br>
 * @creatdate 2013-2-4 下午05:44:28 <br>
 * 
 */
@SuppressWarnings("unchecked")
public class ExcelBean {
	/**
	 * excel文件名称
	 */
	private String title;
	/**
	 * 创建人
	 */
	private String creator;
	/**
	 * 数据集
	 */
	private List list;
	/**
	 * 表格头部（如是合并表头，则为父表头）
	 */
	private String[] hearders;
	/**
	 * 合并表头的子表头（非合并表头不需要设置）
	 */
	private String[][] children;
	/**
	 * Map或JavaBean数据集需指定英文字段顺序
	 */
	private String[] fields;
	
	public ExcelBean(){}
	
	public ExcelBean(String title, String creator, List list, String[] hearders) {
		this.title = title;
		this.creator = creator;
		this.list = list;
		this.hearders = hearders;
	}
	
	public ExcelBean(String title, String creator, List list,
			String[] hearders, String[] fields) {
		this.title = title;
		this.creator = creator;
		this.list = list;
		this.hearders = hearders;
		this.fields = fields;
	}
	
	public ExcelBean(String title, String creator, List list,
			String[] hearders, String[][] children, String[] fields) {
		this.title = title;
		this.creator = creator;
		this.list = list;
		this.hearders = hearders;
		this.children = children;
		this.fields = fields;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	public String[] getHearders() {
		if (null != hearders) { 
			return Arrays.copyOf(hearders, hearders.length); 
		} else { 
			return null; 
		}
	}
	public void setHearders(String[] hearders) {
		if (null != hearders) { 
			this.hearders = Arrays.copyOf(hearders, hearders.length); 
		} else { 
			this.hearders = null; 
		}
	}
	public String[][] getChildren() {
		if (null != children) { 
			return Arrays.copyOf(children, children.length); 
		} else { 
			return null; 
		}
	}
	public void setChildren(String[][] children) {
		if (null != children) { 
			this.children = Arrays.copyOf(children, children.length); 
		} else { 
			this.children = null; 
		}
	}
	public String[] getFields() {
		return fields;
	}
	public void setFields(String[] fields) {
		this.fields = fields;
	}
}
