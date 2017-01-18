package com.ailk.eaap.op2.common;

import com.asiainfo.foundation.log.Logger;

import com.ailk.eaap.op2.common.EAAPException;


public class JavaQueueHelper implements IQueueIHelper{

	//private static QueueSingleTon queueSingleTon = QueueSingleTon.getInstance();
	private  JavaQueue javaQueue;
	private  static Logger logger = Logger.getLog(JavaQueueHelper.class);
	public Object readObj() throws EAAPException {
		// TODO Auto-generated method stub
		try{
			return javaQueue.take();
		}catch(Exception e){
			logger.error("get object from queue error:", e);
			throw new EAAPException( EAAPTags.SEG_DRAVER_SIGN,"9999","get_queue_error",e);
		}
		
	}

	public void writeObj(Object logModel) throws EAAPException {
		// TODO Auto-generated method stub
		try{
			javaQueue.addObj(logModel);
		}catch(Exception e){
			logger.error("write object to queue error:", e);
			throw new EAAPException( EAAPTags.SEG_DRAVER_SIGN,"9999","write_queue_error",e);
		}
	}

	public JavaQueue getJavaQueue() {
		return javaQueue;
	}

	public void setJavaQueue(JavaQueue javaQueue) {
		this.javaQueue = javaQueue;
	}

	public int getDeap() throws EAAPException {
		// TODO Auto-generated method stub
		return javaQueue.getDeepth();
	}


}
