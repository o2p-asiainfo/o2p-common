package com.ailk.eaap.o2p.common.cache;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang.ArrayUtils;

import com.ailk.eaap.op2.common.IThreadManagerService;

public class ThreadManagerServiceImpl implements IThreadManagerService{
	
	@SuppressWarnings("rawtypes")
	private Future[] futureHandles ;
	private int threadNum=1;
	private ExecutorService sendMsgService ;
	private CacheThread[] cacheThreades;	
	
	public void start() {
		futureHandles = new Future[threadNum];
		sendMsgService = Executors.newFixedThreadPool(threadNum);
		for(int i = 0 ; i < threadNum ; i ++){
			cacheThreades[i].setRun(true);
			futureHandles[i] = sendMsgService.submit(cacheThreades[i]);
		}
	}

	public void stop() {
		if(cacheThreades.length>0){
			for (int i = 0; i < cacheThreades.length; i++) {
				cacheThreades[i].setRun(false);
			}
		}
		sendMsgService.shutdown();
	}

	@SuppressWarnings("rawtypes")
	public void setFutureHandles(Future[] futureHandles) {
		if(!ArrayUtils.isEmpty(futureHandles)){
			this.futureHandles = (Future[]) ArrayUtils.clone(futureHandles);
		}
	}
	public int getThreadNum() {
		return threadNum;
	}
	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
	public ExecutorService getSendMsgService() {
		return sendMsgService;
	}
	public void setSendMsgService(ExecutorService sendMsgService) {
		this.sendMsgService = sendMsgService;
	}
	public CacheThread[] getCacheThreades() {
		if (null != cacheThreades) { 
			return Arrays.copyOf(cacheThreades, cacheThreades.length); 
		} else { 
			return null; 
		} 
	}
	public void setCacheThreades(CacheThread[] cacheThreades) {
		if (null != cacheThreades) { 
			this.cacheThreades = Arrays.copyOf(cacheThreades, cacheThreades.length); 
		} else { 
			this.cacheThreades = null; 
		} 
	}
}
