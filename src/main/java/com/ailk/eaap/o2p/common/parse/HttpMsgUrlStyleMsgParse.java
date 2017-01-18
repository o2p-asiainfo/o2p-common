/** 
 * Project Name:serviceAgent-core 
 * File Name:JsonMsgParse.java 
 * Package Name:com.ailk.eaap.integration.o2p.parsing 
 * Date:2014年11月13日上午9:52:48 
 * Copyright (c) 2014, www.asiainfo.com All Rights Reserved. 
 * 
 */

package com.ailk.eaap.o2p.common.parse;


/**
 * ClassName:HttpMsgUrlStyleMsgParse 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON. 
 * Date: 2014年11月13日 上午9:52:48 
 * 
 * @author zhongming
 * @version
 * @since JDK 1.6
 * 
 */
public class HttpMsgUrlStyleMsgParse implements IMsgParse {

	@Override
	public Object parsing(Object obj, String path) {
		if(obj == null) {
			return null;
		}
		String[] strArray = obj.toString().split("&");
		for(String keyValue: strArray) {
			String[] kv = keyValue.split("=");
			if(kv.length >= 2) {
				if(kv[0].equals(path)) {
					return kv[1];
				}
			}
		}
		return null;
	}

	@Override
	public Object parsingValToObject(Object obj, String path) {
		return parsing(obj, path);
	}

}
