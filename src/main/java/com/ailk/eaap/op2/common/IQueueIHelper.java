package com.ailk.eaap.op2.common;

import com.ailk.eaap.op2.common.EAAPException;


/**
 * 
* @ClassName: IQueueIHelper 
* @Description: 队列操作
* @author 李铁扬 
* @date Aug 24, 2011 4:04:58 PM 
*
 */
public interface IQueueIHelper {
	
	/**
	 * 
	* @Title: writeObj 
	* @Description: 把日志对象写入队列
	* @param @param logModel
	* @param @throws EOPException    
	* @return void    
	* @throws
	 */
	public void writeObj(Object obj) throws EAAPException ;
	/**
	 * 
	* @Title: readObj 
	* @Description: 从日志队列中读取消息
	* @param @return
	* @param @throws EOPException    
	* @return LogModel    
	* @throws
	 */
	public Object readObj() throws EAAPException;
	
	public int getDeap() throws EAAPException;

}
