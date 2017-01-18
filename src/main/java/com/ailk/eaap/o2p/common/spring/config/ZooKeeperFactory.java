package com.ailk.eaap.o2p.common.spring.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import com.asiainfo.foundation.log.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;

import com.ailk.eaap.o2p.common.util.LocalUtils;

/**
 * 
 * @author 颖勤
 * 
 */
public class ZooKeeperFactory
{
	private static final Logger LOG = Logger.getLog(ZooKeeperFactory.class);
    private static String CONNECT_STRING = "localhost:2181";

    public static final int MAX_RETRIES = 3;

    public static final int BASE_SLEEP_TIMEMS = 3000;

    protected static String NAME_SPACE = "cfg";
    
    private ZooKeeperFactory(){
    	
    }

    public static String getNAME_SPACE()
    {
        return NAME_SPACE;
    }

    private static CuratorFramework client = null;

    private static Properties evnCfgItem = null;

    public static Properties getEvnCfgItem()
    {
        return evnCfgItem;
    }

    static
    {
        try
        {
        	if(LocalUtils.getSystemVariable("CONNECT_STRING") == null) {
	            evnCfgItem = PropertiesLoaderUtils
	                    .loadProperties(new ClassPathResource(
	                            ICfgCacheHolder.ENV_CFG_PROP_RESOURCE));
        	}
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            LOG.error("something bad happend",e);
        }
    }

    public static CuratorFramework get()
    {
        if (client == null) {
        	synchronized (ZooKeeperFactory.class){
	            RetryPolicy retryPolicy = new ExponentialBackoffRetry(
	                    BASE_SLEEP_TIMEMS, MAX_RETRIES);
	            if(LocalUtils.getSystemVariable("CONNECT_STRING") != null) {
	            	CONNECT_STRING = LocalUtils.getSystemVariable("CONNECT_STRING");
	            } else if (evnCfgItem != null && evnCfgItem.containsKey("CONNECT_STRING")) {
	                CONNECT_STRING = evnCfgItem.getProperty("CONNECT_STRING");
	            }
	            String zkStartPath = null;
	            if(LocalUtils.getSystemVariable("PROPERTY_NAME_SPACE") != null) {
	            	zkStartPath = LocalUtils.getSystemVariable("PROPERTY_NAME_SPACE");
	            } else if (evnCfgItem != null && evnCfgItem.containsKey("PROPERTY_NAME_SPACE")) {
	            	zkStartPath = evnCfgItem.getProperty("PROPERTY_NAME_SPACE");
	            }
	            if (StringUtils.hasText(zkStartPath)) {
	            	if(zkStartPath.startsWith("/")) {
	            		NAME_SPACE = zkStartPath.substring(1);
	            	} else {
	            		NAME_SPACE = zkStartPath;
	            	}
	            }
                if(NAME_SPACE.contains("/")) {
                	NAME_SPACE = NAME_SPACE.substring(
                			0, NAME_SPACE.indexOf("/"));
                }
                LOG.info("ZK connection string "+CONNECT_STRING+"，BASE NAME_SPACE " + NAME_SPACE);
	            client = CuratorFrameworkFactory.builder()
	                    .connectString(CONNECT_STRING).retryPolicy(retryPolicy)
	                    .namespace(NAME_SPACE).build();
	            client.start();
	        }
        }
        return client;
    }
}
