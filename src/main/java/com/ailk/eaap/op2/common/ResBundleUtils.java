package com.ailk.eaap.op2.common;

import java.util.Enumeration;
import java.util.ResourceBundle;  
import java.util.Locale; 
/**
 * 
 * @author 颖勤
 *
 */
public class ResBundleUtils {
	//默认
	private String lang = "en_US";

	private String messagelocation = "messages/un/eaap-op2-conf-adapter-conf-messages" ;
	
	private static ResourceBundle rb;
	
	public String getMessagelocation() {
		return messagelocation;
	}

	public void setMessagelocation(String messagelocation) {
		this.messagelocation = messagelocation;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	/**
	 * 将国际化资源文件的变量转化为JS常量，需要注意key的命名需要符合变量命名规范
	 * @return
	 */
	public String writeMsg2Page(){
		if(rb==null){
			String[] localeSpec = lang.split("_");
			String lan = localeSpec[0];
			String country;
			if(localeSpec.length>1){
				country = (localeSpec[1]==null?"":localeSpec[1]);
			}else
				country = "";
			Locale locale = new Locale(lan,country); 
			rb = ResourceBundle.getBundle(messagelocation,locale);  
		}

		Enumeration<String> enum1 = rb.getKeys();
		StringBuffer buffer = new StringBuffer("");
		buffer.append("<script>\n");
		while (enum1.hasMoreElements()) {
			String key = enum1.nextElement().toString();
			buffer.append("var " + key + "= '" + rb.getString(key) + "';\n");
		}
		buffer.append("</script> \n");
		return buffer.toString();
	}
}
