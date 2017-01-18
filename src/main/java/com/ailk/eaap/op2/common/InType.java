package com.ailk.eaap.op2.common;

import java.util.ArrayList;
import java.util.List;


public class InType {

	public static String RESTGET = "RESTGET";
	public static String RESTPOST = "RESTPOST";
	public static String CEPHTTP = "CEPHTTP";
	public static String SOCKET = "SOCKET";
	public static String CEPWEBSERVICE = "CEPWEBSERVICE";
	
	public static String GeneralWebService = "GeneralWebService";
	
	public static String Rest = "REST";
	public static String delete = "delete";
	public static String put = "put";
	public static String post = "post";
	public static String get = "get";
	public static String head = "head";
	public static String options = "options";
	
	public static String APINAME = "apiname";
	public static String APPKEY = "AppKey";
	public static String SrcSysCode = "SrcSysCode";
	public static String ReqTime = "ReqTime";
	public static String DstSysID = "DstSysID";
	public static String Format = "Format" ;
	public static String TRANSID = "TransactionID";
	public static String Method = "Method";
	public static String BusiCode = "BusiCode";
	public static String AccessToken = "AccessToken";
	public static String ServiceContractVer = "ServiceContractVer";
	public static String MsgBody = "msgBody";
	public static String IP = "ip";
	public static String Sign = "Sign";
	public static String serviceCode = "ServiceCode";
//	public static String TenantId = "TenantId";
	public static final String tenantId = "tenantId";
	public static String ServiceName = "servicename";
	public static String OperatorCode = "OperatorCode";
	public static String Image = "image";
	
	public static String scopeId = "scopeId";
	
	public final static String CONTENT_TYPE_XML_UTF8 = "text/xml;charset=UTF-8";
	public final static String CONTENT_TYPE_PLAIN_UTF8 = "text/plain;charset=UTF-8";
	public final static String CONTENT_TYPE_JSON_UTF8 = "text/json;charset=UTF-8";
	
	public static List<String> headers = new ArrayList<String>();
	static {
		
		headers.add("Host".toLowerCase());
		headers.add("User-Agent".toLowerCase());
		headers.add("Accept-Language".toLowerCase());
		headers.add("Accept-Encoding".toLowerCase());
		headers.add("Transfer-Encoding".toLowerCase());
		headers.add("Connection".toLowerCase());
		headers.add("Pragma".toLowerCase());
		headers.add("Cache-Control".toLowerCase());
		headers.add("Content-Length".toLowerCase());
	}
	
	//public static String SRC_REQ_MSG = "SRC_REQ_MSG";
	//public static String RSP_MSG = "RSP_MSG";
	//public static String SRC_SYS_CODE = "SRC_SYS_CODE";
	//public static String contractVer = "contractVer";
}
