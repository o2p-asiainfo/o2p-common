package com.ailk.eaap.o2p.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.ailk.eaap.o2p.common.security.SecurityUtil;

import net.sf.json.JSONObject;

public class PropertiesParseUtil {
	public static final String VALUE_NAME = "value";
	public static final String VALUE_BUFFER_NAME = "proValueBuffer";
	public static final String LOCK_NAME = "lock";
	public static final Integer IS_LOCK = 1;
	public static final Integer NON_LOCK = 0;
	
	private final static Logger LOG = LoggerFactory.getLogger(PropertiesParseUtil.class);
	
	public static String getPropertiesValue(String propertiesJson) {
		try {
			JSONObject obj = JSONObject.fromObject(propertiesJson);
			String value = obj.getString(VALUE_NAME);
			String proValueBuffer = obj.getString(VALUE_BUFFER_NAME);
			String val = StringUtils.hasText(value)?value:proValueBuffer;
			Integer lock = obj.getInt(LOCK_NAME);
			if(lock == IS_LOCK) {
				return SecurityUtil.getInstance().decryMsg(val);
			} else {
				return val;
			}
		} catch(Exception e) {
			LOG.error("invalid json string:"+propertiesJson, e);
			return null;
		}
	}
}
