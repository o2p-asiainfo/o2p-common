package com.ailk.eaap.o2p.common.spring.config;

import java.io.IOException;
import java.util.Properties;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.CollectionUtils;

/**
 * 
 * @author 颖勤
 *
 */
public class CfgCacheHolder implements ICfgCacheHolder {

	private Properties cacheLocationProp ;
	
	private MemcachedClient memcachedClient;
	
	private boolean ignoreResourceNotFound = false;
	
	private static final int PROP_NEED_ENCRYPT = 1;
	// dependcy on the method 'loadPropFromCache' has been called
	public static Properties APP_PROPS = new Properties();

	public CfgCacheHolder() throws IOException{
		cacheLocationProp = PropertiesLoaderUtils.loadProperties(new ClassPathResource(ENV_CFG_PROP_RESOURCE));
		String cacheLocation = cacheLocationProp.getProperty("cacheLocation");
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses (cacheLocation));
		memcachedClient = builder.build();
		memcachedClient.setEnableHeartBeat(false);
		memcachedClient.setOpTimeout(5000l);
	}
	
	public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
		this.ignoreResourceNotFound = ignoreResourceNotFound;
	}


	@Override
	public Properties loadPropFromCache() throws IOException {
		Properties cacheProp = new Properties();
		CollectionUtils.mergePropertiesIntoMap(cacheProp, APP_PROPS);
		return cacheProp;
	}
}

