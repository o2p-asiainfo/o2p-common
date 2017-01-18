package com.ailk.eaap.op2.common;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.ailk.eaap.o2p.common.spring.config.ZKCfgCacheHolder;
import com.asiainfo.foundation.common.ExceptionCommon;
import com.asiainfo.foundation.exception.BusinessException;
import com.asiainfo.foundation.log.LogModel;
import com.asiainfo.foundation.log.Logger;

public class CommonUtils {

	private static final int _C10 = 10;
	private static Logger log = Logger.getLog(CommonUtils.class);
	
	public static String genTransactionID() {
		String orderNum = new Date().hashCode() + "";
		String lpad = lpad(orderNum);
		return "1000010005"
				+ new SimpleDateFormat("yyyyMMdd").format(new Date()) + lpad;
	}

	public static String lpad(String number) {
		String f = "%0" + _C10 + "d";
		return String.format(f, Integer.valueOf(number));
	}

	/**
	 * 获取配置文件值
	 * 
	 * @param proCode
	 * @return
	 */
	public static String getValueByProCode(String proCode) {
		Properties p = new Properties();
		try {
			p.load(CommonUtils.class
					.getResourceAsStream("/InterfaceURL.properties"));
			return (String) p.get(proCode);
		} catch (IOException e) {
			// /log.error(e.getStackTrace());
			log.error(LogModel.EVENT_APP_EXCPT, new BusinessException(
					ExceptionCommon.WebExceptionCode,
					"Gets the configuration file value anomaly", null));
			return null;
		}
	}

	/**
	 * 获取配置文件中的中文值
	 * 
	 * @param proCode
	 * @return
	 */
	public static String getChineseValueByProCode(String proCode) {
		try {
			String value = (String) ZKCfgCacheHolder.PROP_ITEMS.get(proCode);
			if(value != null) {
				return new String(value.getBytes("ISO-8859-1"),
					"utf-8");
			} else {
				return value;
			}
			
		} catch (IOException e) {
			// /log.error(e.getStackTrace());
			log.error(LogModel.EVENT_APP_EXCPT, new BusinessException(
					ExceptionCommon.WebExceptionCode,
					"Gets the configuration file in the Chinese value anomaly",
					e));
			return null;
		}
	}
}
