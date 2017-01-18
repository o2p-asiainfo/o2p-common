package com.ailk.eaap.o2p.common.spring.config;

import java.util.Properties;

/**
 * 
 * @author 颖勤
 *
 */
public interface Config {
	byte[] getConfig(String path) throws Exception;
	public Properties getConfigItems(String path,boolean isIgnoreUnresolvableItem)throws Exception;
}
