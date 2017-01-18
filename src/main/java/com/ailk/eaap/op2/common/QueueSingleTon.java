package com.ailk.eaap.op2.common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class QueueSingleTon {

	private static BlockingQueue logQueue = new LinkedBlockingQueue();
	public static boolean addObj(Object logModel) {
        return logQueue.add(logModel);
    }
	private static QueueSingleTon queueSingleTon = null;
	private QueueSingleTon(){
	}
	
/*	public static synchronized   QueueSingleTon getInstance(){
		if(queueSingleTon==null){
			queueSingleTon =  new QueueSingleTon();
		}
		return queueSingleTon;
	}*/
	
	public Object take() throws InterruptedException{
		return queueSingleTon.logQueue.take();
	}
	
	public int getDeepth(){
		return queueSingleTon.logQueue.size();
	}
}
