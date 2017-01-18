package com.ailk.eaap.o2p.common.spring.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;

import com.ailk.eaap.o2p.common.util.LocalUtils;
import com.ailk.eaap.o2p.common.util.PropertiesParseUtil;

/**
 * 
 * @author 颖勤
 *
 */
public class ZKCfgCacheHolder implements ICfgCacheHolder{

	protected static Logger LOG = LoggerFactory.getLogger(ZKCfgCacheHolder.class);

	private static String PATH = "/properties";

	private boolean ignoreResourceNotFound = false;

	public static Properties PROP_ITEMS = new Properties();
	
	private static Properties evnCfgItem = null;
	
	static {
		try {
			if(LocalUtils.getSystemVariable("PROPERTY_NAME_SPACE") != null) {
				PATH = LocalUtils.getSystemVariable("PROPERTY_NAME_SPACE");
			} else {
				evnCfgItem = PropertiesLoaderUtils
	                    .loadProperties(new ClassPathResource(
	                            ICfgCacheHolder.ENV_CFG_PROP_RESOURCE));
	            if (evnCfgItem != null
	                    && evnCfgItem.containsKey("PROPERTY_NAME_SPACE")) {
	            	PATH = evnCfgItem
	                        .getProperty("PROPERTY_NAME_SPACE");
	            }
			}
            if (StringUtils.hasText(PATH)
                    && PATH.startsWith("/")) {
        		PATH = PATH.substring(1);
            }
            if(PATH.contains("/")) {
            	PATH = PATH.substring(PATH.indexOf("/"));
            } 
            else if (StringUtils.hasText(PATH)) {
        		PATH = "/"+PATH;
            }
            LOG.info("ZK NAME_SPACE " + PATH);
        }
        catch (IOException e) {
        	LOG.warn("can not load properties file {0}", ENV_CFG_PROP_RESOURCE);
        	PATH = "/properties";
        }
	}

	@Override
	public Properties loadPropFromCache() throws IOException {
		ZooKeeperConfig config = new ZooKeeperConfig();

		try {
			PROP_ITEMS = config.getConfigItems(PATH, ignoreResourceNotFound);
			registerWatcher();
		} catch (Exception e) {
			throw new IOException("Resource Not Found from ZK znode",e);
		}
		
		return PROP_ITEMS;
	}

	public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
		this.ignoreResourceNotFound = ignoreResourceNotFound;
	}

	private void registerWatcher() throws Exception{
		PathChildrenCache patchCache = new PathChildrenCache(ZooKeeperFactory.get(), PATH, false); 
		patchCache.start();
		PathChildrenCacheListener plis=new PathChildrenCacheListener(){

			@Override
			public void childEvent(CuratorFramework client,
					PathChildrenCacheEvent event) throws Exception {
				String propPath = event.getData().getPath();
				String propName = ZKPaths.getNodeFromPath(event.getData().getPath());
				byte[] propVal = client.getData().forPath(propPath);
				switch(event.getType()){
				case CHILD_ADDED: 
					if(propVal!=null){
						String strVal = new String(propVal,"UTF-8");
						String currentNs = client.getNamespace();
						String zNodefullPath = "/"+currentNs+propPath;
						String value = PropertiesParseUtil.getPropertiesValue(strVal);
						if(value == null) {
							value = "";
						}
						PROP_ITEMS.put(propName, value);
						//LOG.info("Event notify: a znode has been created,the path="+zNodefullPath);						
					}

					break; 
				case CHILD_UPDATED: 
					if(propVal!=null){
						String strVal = new String(propVal,"UTF-8");
						String currentNs = client.getNamespace();
						String zNodefullPath = "/"+currentNs+propPath;
						String oldVal = PROP_ITEMS.getProperty(propName);
						if(!StringUtils.hasText(oldVal)||oldVal.equals(strVal)){
							break;
						}else{
							String value = PropertiesParseUtil.getPropertiesValue(strVal);
							if(value == null) {
								value = "";
							}
							PROP_ITEMS.put(propName, value);
							LOG.info("Event notify: a znode has been updated,the path="+zNodefullPath);
						}						
					}
					break; 
				case CHILD_REMOVED: 
					if(propVal!=null){
						String currentNs = client.getNamespace();
						String zNodefullPath = "/"+currentNs+propPath;
						LOG.warn("Event notify: a znode' path="+zNodefullPath+" has detcted EVENT 'CHILD_REMOVED',that deny to remove property from current system");						
					}

					break; 
				default:
					break;				
				}
			}

		};
		patchCache.getListenable().addListener(plis);
	}

}
