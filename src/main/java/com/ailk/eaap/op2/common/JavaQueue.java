package com.ailk.eaap.op2.common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class JavaQueue {

	private  BlockingQueue logQueue = new LinkedBlockingQueue();
	
	public Object take() throws InterruptedException{
		return logQueue.take();
	}
	
	public int getDeepth(){
		return logQueue.size();
	}
	public  boolean addObj(Object logModel) {
        return logQueue.add(logModel);
    }
	
}
