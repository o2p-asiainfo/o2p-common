package com.ailk.eaap.o2p.common.spring.config;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.CollectionUtils;


/**
 * 
 * @author 颖勤
 *
 */
public class O2pPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer{
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private boolean localOverride;
	
	private boolean remoteOverride;
	
	protected Properties[] localProperties;
	/** the field can work that when the operation mergeProperties has been hited **/
	public final static Properties globalProperties = new Properties();
	
	private ICfgCacheHolder cacheHolder;
	
	public void setLocalOverride(boolean localOverride) {
		this.localOverride = localOverride;
	}
	public void setLocalProperties(Properties[] localProperties) {
		this.localProperties = localProperties;
	}

	public void setRemoteOverride(boolean remoteOverride) {
		this.remoteOverride = remoteOverride;
	}
	
	public void setCacheHolder(ICfgCacheHolder cacheHolder) {
		this.cacheHolder = cacheHolder;
	}
	@SuppressWarnings("rawtypes")
	protected String parseStringValue(String strVal, Properties props, Set visitedPlaceholders)
			throws BeanDefinitionStoreException {
			return super.parseStringValue(strVal, props, visitedPlaceholders);
	}
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		try {
			Properties mergedProps = mergeProperties();

			// Convert the merged properties, if necessary.
			convertProperties(mergedProps);

			// Let the subclass process the properties.
			processProperties(beanFactory, mergedProps);
		}
		catch (IOException ex) {
			throw new BeanInitializationException("Could not load properties", ex);
		}
	}
	public Properties mergeProperties() throws IOException {
		Properties result = new Properties();

		if (this.localOverride) {
			// Load properties from file upfront, to let local properties override.
			loadProperties(result);
		}

		if (this.localProperties != null) {
			for (Properties localProp : this.localProperties) {
				CollectionUtils.mergePropertiesIntoMap(localProp, result);
			}
		}
		
		if(this.remoteOverride){
			Properties _cacheProp = cacheHolder.loadPropFromCache();
			CollectionUtils.mergePropertiesIntoMap(_cacheProp, result);
		}

		if (!this.localOverride) {
			// Load properties from file afterwards, to let those properties override.
			loadProperties(result);
		}
		CollectionUtils.mergePropertiesIntoMap(result, globalProperties);
		return result;
	}
	
}

