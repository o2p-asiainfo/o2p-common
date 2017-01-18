package com.ailk.eaap.o2p.common.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CacheThread implements Runnable{

	private IReloadCache reloadCache;
	private boolean run = false;
	private String refreshTime = "60000";
	public void setRefreshTime(String refreshTime) {
		this.refreshTime = refreshTime;
	}
	private static Log log = LogFactory.getLog(CacheThread.class);
	public void run() {
		while(run){
			try{
				reloadCache.reloadCache();
				Thread.sleep(Integer.valueOf(refreshTime));
			}catch(Exception e ){
				log.error(e);
			}
		}
	}
	public IReloadCache getReloadCache() {
		return reloadCache;
	}
	public void setReloadCache(IReloadCache reloadCache) {
		this.reloadCache = reloadCache;
	}
	public boolean isRun() {
		return run;
	}
	public void setRun(boolean run) {
		this.run = run;
	}
}
