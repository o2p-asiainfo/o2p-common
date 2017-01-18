package com.ailk.eaap.o2p.common.cache;

public interface IReloadCache {
	void reloadCache();
	void synCacheToDb(int type);
	boolean addAll(String tenantId);
	//提供调用接口给前台 add by lwf
	boolean cacheRefreshService(String tenantId, String moduleName, String serviceType);
}
