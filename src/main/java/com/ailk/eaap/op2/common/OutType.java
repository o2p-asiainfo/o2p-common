package com.ailk.eaap.op2.common;

import java.util.ArrayList;
import java.util.List;

public class OutType {
	
	public static String rspTime = "RspTime";
	public static String rspType = "RspType";
	public static String rspCode = "RspCode";
	public static String rspDesc = "RspDesc";
	public static String httpStatusCode="HTTPSTATUSCODE";
	public static String transactionID = "TransactionID";
	
	public static List<String> headers = new ArrayList<String>();
	static {
		
		headers.add("Accept".toLowerCase());
		headers.add("Accept-Charset".toLowerCase());
		headers.add("Accept-Encoding".toLowerCase());
		headers.add("Accept-Language".toLowerCase());
		headers.add("Content-Length".toLowerCase());
		headers.add("Cookie".toLowerCase());
		
		headers.add("Server".toLowerCase());
		headers.add("X-Powered-By".toLowerCase());
		headers.add("RspType".toLowerCase());
		headers.add("RspTime".toLowerCase());
		
		headers.add("AppKey".toLowerCase());
		headers.add("SrcSysCode".toLowerCase());
	}
}
