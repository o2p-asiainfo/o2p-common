package com.ailk.eaap.o2p.common.spring.config;

import java.io.IOException;
import java.util.Properties;

/**
 * 
 * @author 颖勤
 * @since 2015/6/14
 *
 */
public interface ICfgCacheHolder {
	
	public static final String ENV_CFG_PROP_RESOURCE = "/env.properties";
	
	/**
	 * 
	 * @return all properties from o2p_conf.conf_properties
	 * @throws IOException
	 */
	public Properties loadPropFromCache() throws IOException;
}
