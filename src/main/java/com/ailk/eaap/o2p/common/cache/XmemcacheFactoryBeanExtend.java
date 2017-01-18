package com.ailk.eaap.o2p.common.cache;  

import com.ailk.eaap.o2p.common.spring.config.ZKCfgCacheHolder;

import net.rubyeye.xmemcached.XMemcachedClient;

/** 
 * ClassName:XmemcacheFactoryBeanExtend  
 * Function: TODO ADD FUNCTION.  
 * Reason:   TODO ADD REASON.  
 * Date:     2015年5月30日 下午6:09:32  
 * @author   daimq 
 * @version   
 * @since    JDK 1.6 
 *        
 */
public class XmemcacheFactoryBeanExtend extends net.rubyeye.xmemcached.utils.XMemcachedClientFactoryBean{

    private boolean enableHeartBeat = false;
    private long sessionIdleTimeout = 6000;
    private String cacheMode;
    private String servers = "localhost:11211";
    private int connectionPoolSize = 5;
    private boolean failureMode = false;
    private int opTimeout = 6000;
    
	@Override
    public Object getObject() throws Exception {
    	if(cacheMode==null || !"memcache".equals(cacheMode)){
    		return null;
    	}
    	
    	if(ZKCfgCacheHolder.PROP_ITEMS.get("memcache.runtimeserver")!=null){
    		servers = ZKCfgCacheHolder.PROP_ITEMS.get("memcache.runtimeserver").toString();
    	}
    	super.setServers(servers);
    	
    	if(ZKCfgCacheHolder.PROP_ITEMS.get("memcache.runtimeconnectionPoolSize")!=null){
    		connectionPoolSize = Integer.valueOf(ZKCfgCacheHolder.PROP_ITEMS.get("memcache.runtimeconnectionPoolSize").toString());
    	}
    	super.setConnectionPoolSize(connectionPoolSize);
    	
    	if(ZKCfgCacheHolder.PROP_ITEMS.get("memcache.runtimefailureMode")!=null){
    		failureMode = Boolean.valueOf(ZKCfgCacheHolder.PROP_ITEMS.get("memcache.runtimefailureMode").toString());
    	}
    	super.setFailureMode(failureMode);

    	if(ZKCfgCacheHolder.PROP_ITEMS.get("memcache.runtimeOpTimeout")!=null){
    		opTimeout = Integer.valueOf(ZKCfgCacheHolder.PROP_ITEMS.get("memcache.runtimeOpTimeout").toString());
    	}
    	super.setOpTimeout(opTimeout);
    	
    	if(ZKCfgCacheHolder.PROP_ITEMS.get("memcache.sessionIdleTimeout")!=null){
    		sessionIdleTimeout = Integer.valueOf(ZKCfgCacheHolder.PROP_ITEMS.get("memcache.sessionIdleTimeout").toString());
    	}
    	
    	if(ZKCfgCacheHolder.PROP_ITEMS.get("memcache.enableHeartBeat")!=null){
    		enableHeartBeat = Boolean.valueOf(ZKCfgCacheHolder.PROP_ITEMS.get("memcache.enableHeartBeat").toString());
    	}
    	

        XMemcachedClient memcache = (XMemcachedClient)super.getObject();
        super.getConfiguration().setSessionIdleTimeout(sessionIdleTimeout);
        memcache.setEnableHeartBeat(enableHeartBeat);
        return memcache;
    }
    public boolean isEnableHeartBeat() {
        return enableHeartBeat;
    }
    public void setEnableHeartBeat(boolean enableHeartBeat) {
        this.enableHeartBeat = enableHeartBeat;
    }
    public long getSessionIdleTimeout() {
        return sessionIdleTimeout;
    }
    public void setSessionIdleTimeout(long sessionIdleTimeout) {
        this.sessionIdleTimeout = sessionIdleTimeout;
    }
    public void setCacheMode(String cacheMode) {
		this.cacheMode = cacheMode;
	}
    
}
