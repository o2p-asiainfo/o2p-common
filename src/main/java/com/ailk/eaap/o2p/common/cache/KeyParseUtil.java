package com.ailk.eaap.o2p.common.cache;

import org.apache.commons.codec.digest.DigestUtils;

public class KeyParseUtil {
	public static Object parse(Object key) {
		if(key != null && key instanceof String) {
			return DigestUtils.md5Hex((String)key);
		}
		return key;
	}
	
	public static void main(String[] args){
		System.out.println(parse("1SERVICEufo-20160613-1"));
	}
}
