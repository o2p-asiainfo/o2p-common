/*
 * @(#)LogServerConstant.java        2013-2-1
 *
 * Copyright (c) 2013 asiainfo-linkage
 * All rights reserved.
 *
 */
package com.ailk.eaap.o2p.common.jdbc.datasource;

import java.util.HashMap;
import java.util.Map;

import com.ailk.eaap.op2.common.InType;

/**
 * 类名称 LogServerConstant <br>
 * 日志服务端常用固定变量
 * <p>
 * @version 1.0
 * @author linfeng 2013-2-1
 * <hr>
 * 修改记录
 * <hr>
 * 1、修改人员:    修改时间:<br>       
 *    修改内容:
 * <hr>
 */
public final class DataSourceConstant {
	
    
    
    private DataSourceConstant(){
        
    }
    
    //跟数据源选择相关
	//数据源路由，线程变量中保存数据源名的ContextName
	public static final String DepDataSourceContextName = "DepDataSource";
	
	//数据源路由，线程变量中保存的内部变易流水
	public static final String CiiContextName = "cii";	
	
	//数据源路由，线程变量中保存物理表名的ContextName
	public static final String DepTabSuffixContextName = "DepTabSuffix";
	
	//SQL语句中，物理表名编写方式
	public static final String DepTabSuffixSQLPattern = "\\$\\{DepTabSuffix\\}";
	
	public static final String DATA_SOURCE_ROUTE_LIST = "dataSourceRouteList";
	
	public static final String DATA_SOURCE_MAP = "dataSourceMap";
	
	public static final Map<String,String> JAVA_FIELD_KEY_MAP = new HashMap<String,String>();

	public static final String DATA_SOURCE_NAME = "DATA_SOURCE_NAME";
	
	public static final String IS_DEFAULT = "IS_DEFAULT";

	public static final String YES = "Y";
	
	static{
		JAVA_FIELD_KEY_MAP.put(InType.APINAME, "API_NAME");
		JAVA_FIELD_KEY_MAP.put(InType.APPKEY, "APP_KEY");
		JAVA_FIELD_KEY_MAP.put(InType.SrcSysCode, "SRC_SYS_CODE");
		JAVA_FIELD_KEY_MAP.put(InType.ReqTime, "REQ_TIME");
		JAVA_FIELD_KEY_MAP.put(InType.DstSysID, "DST_SYS_ID");
		JAVA_FIELD_KEY_MAP.put(InType.Format, "FORMAT");
		JAVA_FIELD_KEY_MAP.put(InType.TRANSID, "TRANSID");
		JAVA_FIELD_KEY_MAP.put(InType.Method, "METHOD");
		JAVA_FIELD_KEY_MAP.put(InType.BusiCode, "BUS_CODE");
		JAVA_FIELD_KEY_MAP.put(InType.AccessToken, "ACCESS_TOKEN");
		JAVA_FIELD_KEY_MAP.put(InType.ServiceContractVer, "SVC_CONTRACT_VER");
		JAVA_FIELD_KEY_MAP.put(InType.IP, "IP");
		JAVA_FIELD_KEY_MAP.put(InType.Sign, "SIGN");
		JAVA_FIELD_KEY_MAP.put("srcSysSign", "SRC_SYS_SIGN");
		JAVA_FIELD_KEY_MAP.put(InType.tenantId, "TENANT_ID");
	}
}