package com.ailk.eaap.o2p.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.asiainfo.foundation.log.Logger;

public class XssCheckUtil {
	
	private static final Logger LOG = Logger.getLog(XssCheckUtil.class);
	private static final String[] regExprs = {"(?i)(<script[^>]*>[\\s\\S]*?<\\/script[^>]*>|<script[^>]*>[\\s\\S]*?<\\/script[[\\s\\S]]*[\\s\\S]|<script[^>]*>[\\s\\S]*?<\\/script[\\s]*[\\s]|<script[^>]*>[\\s\\S]*?<\\/script|<script[^>|*]+>[\\s\\S]*|</script[^>]*>?)",
		"(?i)([\\s\\\"'`;\\/0-9\\=]+on\\w+\\s*=)",
		"(?i)((?:=|U\\s*R\\s*L\\s*\\()\\s*[^>]*\\s*S\\s*C\\s*R\\s*I\\s*P\\s*T\\s*:|&colon;|[\\s\\S]allowscriptaccess[\\s\\S]|[\\s\\\"'`;\\/0-9\\=]+src\\s*=|[\\s\\S]data:text\\/html[\\s\\S]|[\\s\\S]xlink:href[\\s\\S]|[\\s\\S]base64[\\s\\S]|[\\s\\S]xmlns[\\s\\S]|[\\s\\S]xhtml[\\s\\S]|[\\s\\\"'`;\\/0-9\\=]+style\\s*=|<style[^>]*>[\\s\\S]*?|[\\s\\S]@import[\\s\\S]|<applet[^>]*>[\\s\\S]*?|<meta[^>]*>[\\s\\S]*?|<object[^>]*>[\\s\\S]*?)",
		"(?i)((?:\\W|^)(?:eval|alert|setTimeout)\\(.*\\)|(?:\\W|^)(?:document|window)\\.\\w+\\(.*\\))"};
	
	public static boolean xssCheck(String paramStr){
		if(paramStr==null || "".equals(paramStr)){
			return true;
		}
		for(String regExpr : regExprs){
			Pattern pattern = Pattern.compile(regExpr);
	        Matcher matcher = pattern.matcher(paramStr);
	        if(matcher.find()){
	        	LOG.warn("the request may be a xss attach, param=" + paramStr);
	        	return false;
	        }
		}
        return true;
	}
	
	public static void main(String[] args){
		System.out.println(xssCheck("<script>alert(document.sdfsd)</script>"));
		System.out.println(xssCheck("11232"));
	}
}
