package com.ailk.eaap.op2.common;

 /**
  * 
  * 异常编码<br>
  * 异常编码类，定义EAAP全局的异常编码
  * <p>
  * @version 1.0
  * @author laozhu Jan 30, 2013
  * <hr>
  * 修改记录
  * <hr>
  * 1、修改人员:    修改时间:<br>       
  *    修改内容:
  * <hr>
  */

public class EAAPErrorCodeDef {
	
	public static final String ERROR_TYPE_SUCCESS="0";
	public static final String ERROR_TYPE_SYS_ERROR="9";
	public static final String ERROR_TYPE_BIZ_ERROR="1";
	
	/**返回成功*/
	public static final String RESPONSE_SUCCESS="0000";
	/***************************ESB应答CRM业务异常编码*********************************/
	/**
	 * XML格式校验错误
	 */
	public static final String BUS_BAD_FORMAT_9001="9001";
	/**
	 * 字段超过最大长度
	 */
	public static final String BUS_OVER_LENGTH_9002="9002";
	/**
	 * 字段非空校验
	 */
	public static final String BUS_REQUIRED_9003="9003";
	/**
	 * 查找不到关联值
	 */
	public static final String BUS_NOT_RELA_9004="9004";
	/**
	 * 数据类型错误
	 */
	public static final String BUS_DATA_TYPE_ERROR_9005="9005";
	
	
	/***************************ESB内部异常编码*********************************/
	
	/**
	 * 缓存加载失败
	 */
	public static final String LOAD_CACHE_ERR_9011="9011";
	/**
	 * 写基准库失败
	 */
	public static final String WRITE_QUEUE_ERR_9012="9012"; 
	/**
	 * 写队列失败
	 */
	public static final String READ_QUEUE_ERR_9013="9013"; 
	/**
	 * 协议适配失败
	 */
	public static final String PROTOCOL_ADAPT_ERR_9014="9014"; 
	/**
	 * 写消息发送表错误
	 */
	public static final String WRITE_SEND_ERR_9015="9015"; 
	/**
	 * 时序控制错误
	 */
	public static final String ORDER_CONTRL_ERR_9016="9016";
	/**
	 * 平台回调ESB接口内部错误
	 */
	public static final String CALLBACK_ERR_9017="9017";
	
	/**
	 * 未知的内部错误
	 */
	public static final String UNEXPECT_ERR_9999="9999";
	
	  /***************************ABM应答ESB异常编码*********************************/
	
	public static final String RESP_BUS_ERR_9000="9000";
	
}
