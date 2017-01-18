package com.ailk.eaap.op2.common;
/**
 * 
* @ClassName: MemcacheKey 
* @Description: 内存中的key
* @author 李铁扬 
* @date Aug 22, 2011 3:52:12 PM 
*
 */
public class MemcacheKey {

	public static final String exceptionTypeCache = "exceptionTypeCache";
	/**
	 * 内存中业务功能标识
	 */
	public static final String BusCode = "BUSCODE";
	
	/**
	 * 内存中API标识
	 */
	public static final String Api = "API";
	public static final String MainData = "MainData";
	public static final String ProofValues = "ProofValues";
	/**
	 * 内存中wsdl标识
	 */
	public static final String Wsdl = "WSDL";
	/**
	 * 内存中webservice方法标识
	 */
	public static final String ApiOperationName = "APIOPERATIONNAME";
	/**
	 * 内存中协议格式对应webservice方法标识
	 */
	public static final String ContractOperationName = "CONTRACTOPERATIONNAME";
	/**
	 * 内存中头协议节点描述标识
	 */
	public static final String HeadNodeDesc = "HEADNODEDESC";
	/**
	 * 内存中rest配置标识
	 */
	public static final String Rest = "REST";
	/**
	 * 内存中协议标识
	 */
	public static final String Contract="CONTRACT";
	
	/**
	 * 内存中协议版本标识
	 */
	public static final String ContractVer ="CONTRACTVER";
	
	/**
	 * 内存中机构标识
	 */
	public static final String Org = "ORG";
	
	/**
	 * 内存中组件标识
	 */
	public static final String Component = "COMPONENT";
	
	/**
	 * 业务开通标识，该标识+业务功能+组件为key,认证密码为value
	 */
	public static final String OrgBiz = "ORGBIZ";
	
	/**
	 * 业务开通密码加密方式标识，存放以该标识+业务功能+组件+发起落地标识为key,加密方式为value，1表示加密，0未加密
	 */
	public static final String EncryptType = "ENCRYPTTYPE";
	
	/**
	 * IP认证方式,存放以该标识+业务功能+协议版本+组件为key,ip认证方式为value
	 */
	public static final String IPCert = "IPCERTTYPE";
	
	/**
	 * IP标识，存放以该标识+业务功能+协议版本+组件为key,IP名单列表的value
	 */
	public static final String IPList = "IPLIST";
	/**
	 * 服务，存放以该标识+业务功能+协议版本+组件为key的服务
	 */
	public static final String Service = "SERVICE";
	public static final String serInvokeIns = "serInvokeIns";
	public static final String token = "accesstoken";
	public static final String refToken = "refToken";
	public static final String App = "App";
	public static final String AppComp = "AppComp";
	public static final String userInfo = "userInfo";
	public static final String techImpl = "techImpl";
	
	public static final String TimeoutControl = "TimeoutControl";
	public static final String TimeoutControlTime = "TimeoutControlTIME";
	
	public static final String serviceTemp = "serviceTemp";//服务模板
	public static final String task = "task";//任务
	/**
	 * 流量控制类型
	 */
	public static final String FLOW = "FLOWCONTROLCONF"; 
	
	public static final String DSTFLOW = "DSTFLOWCONTROLCONF";

	/**
	 * 流量周期时间
	 */
	public static final String FLOWENDTIME = "ENDTIME";
	
	
	/**
	 * 流量周期时间
	 */
	public static final String DSTFLOWENDTIME = "DSTENDTIME";
	/**
	 * 可用性探测时间
	 */
	public static final String AVALIABLETIME = "AVALIABLETIME";
	public static final String AVALIABLENUM = "AVALIABLENUM";
	
	/**
	 * 次数流量大小
	 */
	public static final String TIMESFLOWCOUNT = "TIMESFLOWCOUNT";
	
	/**
	 * 消息大小流量大小
	 */
	public static final String SIZEFLOWCOUNT = "SIZEFLOWCOUNT";

	/**
	 * 线程大小流量大小
	 */
	public static final String THREADFLOWCOUNT = "THREADFLOWCOUNT";
	
	
	
	/**
	 * 次数流量大小
	 */
	public static final String DSTTIMESFLOWCOUNT = "DSTTIMESFLOWCOUNT";
	
	/**
	 * 消息大小流量大小
	 */
	public static final String DSTSIZEFLOWCOUNT = "DSTSIZEFLOWCOUNT";

	/**
	 * 线程大小流量大小
	 */
	public static final String DSTTHREADFLOWCOUNT = "DSTTHREADFLOWCOUNT";
	
	/**
	 * soo配置标识
	 */
	public static final String SOO = "SOO";
	/**
	 * 数据源缓存标识
	 */
	public static final String DataSourceMap = "DataSourceMap";
	/**
	 * 授权
	 */
	public static final String AuthRole = "authRole";
	
	public static final String AuthorizeCode = "AuthorizeCode";
	/**
	 * 模糊处理标识
	 */
	public static final String HideProcess = "HideProcess";
	/**
	 * 白名单列表标识
	 */
	public static final String WHITELIST = "WHITELIST";
	/**
	 * 自定义端点标识
	 */
	public static final String AutoEndPoint = "AutoEndPoint";
	
}
