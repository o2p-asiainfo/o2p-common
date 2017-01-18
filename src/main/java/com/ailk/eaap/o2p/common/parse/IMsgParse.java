/** 
 * Project Name:serviceAgent-core 
 * File Name:IMsgParse.java 
 * Package Name:com.ailk.eaap.integration.o2p.parsing 
 * Date:2014年11月13日上午9:49:28 
 * Copyright (c) 2014, www.asiainfo.com All Rights Reserved. 
 * 
*/  
  
package com.ailk.eaap.o2p.common.parse;  



/** 
 * ClassName:IMsgParse  
 * Function: TODO ADD FUNCTION.  
 * Reason:   TODO ADD REASON.  
 * Date:     2014年11月13日 上午9:49:28  
 * @author   zhongming 
 * @version   
 * @since    JDK 1.6 
 *        
 */
public interface IMsgParse {
	/**
	 * parsing:(解析对象根据路径解析,返回解析后的value或是element).  
	 * TODO(这里描述这个方法适用条件 – 可选). 
	 * TODO(这里描述这个方法的执行流程 – 可选). 
	 * TODO(这里描述这个方法的使用方法 – 可选). 
	 * TODO(这里描述这个方法的注意事项 – 可选). 
	 * 
	 * @author zhongming 
	 * @param obj 解析对象
	 * @param path 路径
	 * @return 
	 * @since JDK 1.6
	 */
	public Object parsing(Object obj, String path);
	/**
	 * parsingValDocument:(解析对象根据路径返回一个对象).  
	 * TODO(这里描述这个方法适用条件 – 可选). 
	 * TODO(这里描述这个方法的执行流程 – 可选). 
	 * TODO(这里描述这个方法的使用方法 – 可选). 
	 * TODO(这里描述这个方法的注意事项 – 可选). 
	 * 
	 * @author zhongming 
	 * @param obj 解析对象
	 * @param path 解析路径
	 * @return 
	 * @since JDK 1.6
	 */
	public Object parsingValToObject(Object obj, String path);

}
