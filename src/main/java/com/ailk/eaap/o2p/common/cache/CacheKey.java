package com.ailk.eaap.o2p.common.cache;

import com.ailk.eaap.o2p.common.spring.config.ZKCfgCacheHolder;

/**
 * 
* @ClassName: MemcacheKey 
* @Description: 内存中的key
* @author 李铁扬 
* @date Aug 22, 2011 3:52:12 PM 
*
 */
public class CacheKey {
	/**
	 * 多语言国际化涉及的注册表
	 */
	public static final String LocaleSet = "LocaleSet_";
	public static final String LocaleRegConfig = "LocaleRegConfig_";
	
	public static final String LOCALSEQ = "LOCALSEQ";
	public static final String DB_SERVER_LOCAL_SEQ = "DB_SERVER_LOCAL_SEQ";
	public static final String DB_SERVER_LOCAL_LOGO = "DB_SERVER_LOCAL_LOGO";
	public static final String DB_SERVER_COMPONENT_SEQ = "DB_SERVER_COMPONENT_SEQ";
	public static final String SYNCHRONIZE_DATA_FROM_CACHE_DB = "SYNCHRONIZE_DATA_FROM_CACHE_DB";

	public static final String exceptionTypeCache = "exceptionTypeCache";
	public static  Integer defaultTenantId = 22;
	static{
		try {
			String defaultTenantIdStr = ZKCfgCacheHolder.PROP_ITEMS.getProperty("default_tenant_id");
			if(defaultTenantIdStr!=null){
				defaultTenantId = new Integer(defaultTenantIdStr);
			}
		} catch (Exception e) {
			
		}
	}
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
	public static final String CONF_PROPERTIES = "ConfProperties";
	public static final String ALL_CONF_PROPERTIES = "ALL_CONF_PROPERTIES";
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
	 * 内存中异常信息配置标识
	 */
	public static final String Exception = "EXCEPTION";
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
	public static final String techImpl = "techImpl";
	
	public static final String TimeoutControl = "TimeoutControl";
	public static final String TimeoutControlTime = "TimeoutControlTIME";

	public static final String task = "task";//任务
	public static final String logLevelList = "logLevelList";//日志级别
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
	
	public static final String template = "TEMPLATE";
	public static final String body = "BODY";
	public static final String header = "HEADER";
	
	public static final String MODULE_RELOAD_EXCEPTION = "MODULE_RELOAD_EXCEPTION";
	
	/**
	 * 模块名称
	 */
	public static final String MODULE_ALL_FORCE = "MODULE_ALL_FORCE";
	public static final String MODULE_VERSION_MAP = "MODULE_VERSION_MAP";
	public static final String MODULE_ALL = "MODULE_ALL";//全量缓存
	public static final String MODULE_DATA_SOURCE = "MODULE_DATA_SOURCE";
	public static final String MODULE_CSV_TEMPLATE = "MODULE_CSV_TEMPLATE";
	public static final String MODULE_COMPONENT = "MODULE_COMPONENT";
	public static final String MODULE_API = "MODULE_API";
	public static final String MODULE_TEMPLATE = "MODULE_TEMPLATE";
	public static final String MODULE_CONTRACT_VER = "MODULE_CONTRACT_VER";
	public static final String MODULE_ORG = "MODULE_ORG";
	public static final String MODULE_BUS_CODE = "MODULE_BUS_CODE";
	public static final String MODULE_MAIN_DATA = "MODULE_MAIN_DATA";
	public static final String MODULE_TOKEN = "MODULE_TOKEN";
	public static final String MODULE_FLOW = "MODULE_FLOW";
	public static final String MODULE_APP = "MODULE_APP";
	public static final String MODULE_SERVICE = "MODULE_SERVICE";
	public static final String MODULE_SER_INVOKE_INS = "MODULE_SER_INVOKE_INS";
	public static final String MODULE_TECH_IMPL = "MODULE_TECH_IMPL";
	public static final String MODULE_WSDL = "MODULE_WSDL";
	public static final String MODULE_API_OPERATION_NAME = "MODULE_API_OPERATION_NAME";
	public static final String MODULE_REST = "MODULE_REST";
	public static final String MODULE_EXCEPTION = "MODULE_EXCEPTION";
	public static final String MODULE_TRANSFORMER_RULE = "MODULE_TRANSFORMER_RULE";
	public static final String MODULE_REMOTE_AUTH = "MODULE_REMOTE_AUTH";
	public static final String MODULE_FUZZY_ENCRYPTION = "MODULE_FUZZY_ENCRYPTION";
	public static final String MODULE_CONTRACT_NODE_FUZZY = "MODULE_CONTRACT_NODE_FUZZY";
	public static final String MODULE_REMOTE_CALL_INFO = "MODULE_REMOTE_CALL_INFO";
	public static final String MODULE_CONF_PROPERTIES = "MODULE_CONF_PROPERTIES";
	public static final String MODULE_RATING_PROD_OFFER = "MODULE_RATING_PROD_OFFER";
	public static final String MODULE_RATING_ORG = "MODULE_RATING_ORG";
	public static final String MODULE_RATING_API = "MODULE_RATING_API";
	public static final String MODULE_RATING_API_ORG = "MODULE_RATING_API_ORG";
	public static final String MODULE_RATING_PRICING_CASE = "MODULE_RATING_PRICING_CASE";
	public static final String MODULE_RATING_BILLING_CYCLE = "MODULE_RATING_BILLING_CYCLE";
	public static final String MODULE_RATING_SETTLEMENT="MODULE_RATING_SETTLEMENT";
	public static final String MODULE_RATING_SERVICE_PRODUCT_RELA = "MODULE_RATING_SERVICE_PRODUCT_RELA";
	public static final String MODULE_LOG_LEVEL = "MODULE_LOG_LEVEL";
	public static final String MODULE_AUTH_API = "MODULE_AUTH_API";
	public static final String MODULE_TENANT = "MODULE_TENANT";
//	public static final String MODULE_CACHE_OBJ="MODULE_CACHE_OBJ";
	public static final String MODULE_CACHE_STRATEGY="MODULE_CACHE_STRATEGY";
	
	public static final String ALL_DATA_SOURCE = "ALL_DATA_SOURCE";
	public static final String DATA_SOURCE = "DATA_SOURCE";
	public static final String CSV_TEMPLATE = "CSV_TEMPLATE";
	public static final String SERVICE = "SERVICE";
	public static final String PROOF = "PROOF";
	public static final String DATA_SOURCE_MAP = "DATA_SOURCE_MAP";//数据源缓存标识
	
	public static final String FUZZY_ENCRYPTION = "FUZZY_ENCRYPTION";
	public static final String CONTRACT_NODE_FUZZY = "CONTRACT_NODE_FUZZY";
	public static final String REMOTE_CALL_INFO = "REMOTE_CALL_INFO";
	public static final String QUARTZ_TASK_LIST = "QUARTZ_TASK_LIST";
	public static final String TASK_COUNT = "TASK_COUNT";
	public static final String TASK_NAME = "TASK_NAME";
	public static final String SERVICE_NAME = "SERVICE_NAME";
	public static final String TASK_ID = "TASK_ID";
	public static final String SER_INVOKE_INS_NAME = "SER_INVOKE_INS_NAME";
	public static final String TASK_TYPE = "TASK_TYPE";
	public static final String STATE_PAGE = "STATE_PAGE";
	public static final String END_PAGE = "END_PAGE";
	public static final String EVENT_TYPE = "LOG_TYPE";
	public static final String TASK_CYCLE_NAME = "LOG_TYPE";
	public static final String TASK = "TASK";
	public static final String JOB_NAME = "JOB_NAME";
	public static final String LOG_TYPE = "LOG_TYPE";
	public static final String TASK_COUNT_GC_CD = "TASK_COUNT_GC_CD";
	public static final String ALL_TASK_CYCLE = "ALL_TASK_CYCLE";
	public static final String TASK_CYCLE = "TASK_CYCLE";
	public static final String GATHER_CYCLE = "GATHER_CYCLE";
	public static final String TASK_CYCLE_NUM = "TASK_CYCLE_NUM";
	public static final String TASK_JOB_LOG = "TASK_JOB_LOG";
	public static final String TASK_LOG_COUNT = "TASK_LOG_COUNT";
	public static final String TASK_CYCLE_CD = "TASK_CYCLE_CD";
	public static final String TASK_CONTENT = "TASK_CONTENT";
	public static final String TENANT = "TENANT";
	public static final String TENANT_ALL = "TENANT_ALL";
	public static final String DATA_SOURCE_ROUTE = "DATA_SOURCE_ROUTE";
	public static final String ALL_DATA_SOURCE_ROUTE = "ALL_DATA_SOURCE_ROUTE";
	/**
	 *协议转换缓存部分标识  add by linwf
	 */
	public static final String TRANSFORMER_RULE = "transformerRule";
	public static final String PARAM_VAR_MAP = "ParamVarMap";
	
	//服务器定义
	public static final String TOMCAT = "1";
	public static final String WEBLOGIC = "2";

	public static final String LOG_STATISTIC_MEMCACHE_KEYS = "memcacheKeys";
	//跟数据源选择相关
		//数据源路由，线程变量中保存数据源名的ContextName
		public static final String DepDataSourceContextName = "DepDataSource";
		//数据源路由，线程变量中保存的内部变易流水
		public static final String CiiContextName = "cii";	
		//SQL语句中，物理表名编写方式
		public static final String DepTabSuffixSQLPattern = "\\$\\{DepTabSuffix\\}";
		//数据源路由，线程变量中保存物理表名的ContextName
		public static final String DepTabSuffixContextName = "DepTabSuffix";
		
	public static final String KEY_NAME = "key_name";

	public static final String[] MODULE_LIST = {MODULE_DATA_SOURCE, MODULE_CSV_TEMPLATE, MODULE_COMPONENT, MODULE_API,
		MODULE_CONTRACT_VER, MODULE_ORG, MODULE_BUS_CODE, MODULE_MAIN_DATA, MODULE_FLOW, MODULE_APP, MODULE_SERVICE,
		MODULE_SER_INVOKE_INS, MODULE_TECH_IMPL,
		MODULE_WSDL, MODULE_API_OPERATION_NAME, MODULE_REST, MODULE_EXCEPTION,
		MODULE_TRANSFORMER_RULE, MODULE_REMOTE_CALL_INFO, MODULE_CONF_PROPERTIES,
		MODULE_FUZZY_ENCRYPTION, MODULE_CONTRACT_NODE_FUZZY, 
		MODULE_LOG_LEVEL, MODULE_AUTH_API, MODULE_TEMPLATE,MODULE_CACHE_STRATEGY};
	
	public static final String[] BILLING_MODULE_LIST = {MODULE_RATING_PROD_OFFER, MODULE_RATING_BILLING_CYCLE, MODULE_RATING_ORG, MODULE_RATING_API, MODULE_RATING_API_ORG, MODULE_RATING_PRICING_CASE,MODULE_RATING_SETTLEMENT};
	
	public static final String[] MODULE_LIST_LEVEL2 = {MODULE_DATA_SOURCE, MODULE_CSV_TEMPLATE, MODULE_COMPONENT, MODULE_API,
		MODULE_CONTRACT_VER, MODULE_ORG, MODULE_BUS_CODE, MODULE_MAIN_DATA, MODULE_FLOW, MODULE_APP, MODULE_SERVICE,
		MODULE_SER_INVOKE_INS, MODULE_TECH_IMPL,
		MODULE_WSDL, MODULE_API_OPERATION_NAME, MODULE_REST, MODULE_EXCEPTION,
		MODULE_TRANSFORMER_RULE, MODULE_REMOTE_CALL_INFO, MODULE_CONF_PROPERTIES,
		MODULE_FUZZY_ENCRYPTION, MODULE_CONTRACT_NODE_FUZZY,
		MODULE_LOG_LEVEL, MODULE_AUTH_API,MODULE_TEMPLATE,MODULE_CACHE_STRATEGY};
	
	public static final String KEY_LIST_COMPONENT= "KEY_LIST_COMPONENT";
	public static final String AUTH_API_LIST= "AUTH_API_LIST";
	public static final String RATING_PROD_OFFER = "RATING_PROD_OFFER";
	public static final String RATING_PRICING_CASE = "RATING_PRICING_CASE";
	public static final String RATING_ORG = "RATING_ORG";
	public static final String RATING_BILLING_CYCLE = "RATING_BILLING_CYCLE";
	public static final String RATING_API = "RATING_API";
	public static final String RATING_SETTLEMENT="RATING_SETTLEMENT";
	public static final String KEY_LIST_RATING_COMBINE="KEY_LIST_RATING_COMBINE";
	
	//token缓存对象key
	public static final String CacheObj = "CACHE_OBJ";
	//token缓存策略key
	public static final String CacheStrategy="CACHE_STRATEGY";
}
