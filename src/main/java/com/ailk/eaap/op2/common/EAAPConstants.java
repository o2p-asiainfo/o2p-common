package com.ailk.eaap.op2.common;

import com.ailk.eaap.o2p.common.util.CommonUtil;

/**
 * 常量类
 * 
 * <p>
 * @version 1.0
 * @author laozhu Jan 31, 2013
 * <hr>
 * 修改记录
 * <hr>
 * 1、修改人员:   wanglm   
 *    修改时间:   2013年5月8日 11:04:31      
 *    修改内容:   add  MAINDATATYPE_PROD_SPEC_UNIT 
 * <hr>
 * <hr>
 * 1、修改人员:   wanglm   
 *    修改时间:   2013年8月9日 11:28:47      
 *    修改内容:   add PARENT_FUNCTION 、BUSINESS_SYSTEM  (动态菜单)
 * <hr>
 */

public class EAAPConstants {
	/** 系统表 应用表 状态主数据标识 **/
	public static final String MAINDATATYPE_SIGN_COMAPP = "state_comp_app";
	
	/** 机构表状态主数据标识 **/
	public static final String MAINDATATYPE_SIGN_ORG = "state_org";
	
	/** 机构表证件类型主数据标识 **/
	public static final String MAINDATATYPE_SIGN_ORG_CERT_TYPE = "ORG_CERT_TYPE";
	
	/** ContractFormat表CON_TYPE协议类型 **/
	public static final String MAINDATATYPE_SIGN_CONTRACTCON = "type_contract";
	
	/** API表状态主数据标识 **/
	public static final String MAINDATATYPE_SIGN_API = "state_api";
	
	/** app_api_list表状态主数据标识 **/
	public static final String MAINDATATYPE_SIGN_APPAPILIST = "state_app_api_list";
	
	/** 数据列状态 **/
	public static final String MAINDATATYPE_SIGN_COL = "state_col";
	
	/** prod_channel_type表状态主数据标识 **/
	public static final String MAINDATATYPE_SIGN_PRODCHANNELTYPE = "prod_channel_type";
	
    /** prod_goods_state表状态主数据标识 **/
	public static final String MAINDATATYPE_SIGN_PRODGOODSSTATE = "prod_goods_state";

	/** prod_state **/
	public static final String MAINDATATYPE_SIGN_PRODSTATE = "prod_state";
	
    /** AttrSpec表IsCustomized标识**/
	public static final String MAINDATATYPE_SIGN_ATTRSPECISCUSTOMIZED = "AttrSpec_IsCustomized";
    /** AttrSpec表PageInType1标识 **/
	public static final String MAINDATATYPE_SIGN_ATTRSPEC_TYPE = "ATTR_SPEC_TYPE";
	
	/**异常人工处理 异常类型标识	* */
	public static final String MAINDATATYPE_SIGN_EXCEPTION_TYPE = "EXCEPTION_TYPE";
	
	/**apphub业务系统的角色类型 roleType标识**/
	public static final String HUB_OPERATOR="Operator";
	public static final String HUB_CUSTOMER="Customer";
	public static final String HUB_VISITOR="Visitor";
	
	/** appHub 预置系统用户名及密码**/
	public static final String HUB_USERNAME="O2P";
	public static final String HUB_PASSWORD="123456";
	
	/** appHub 21租户**/
	public static final String HUB_TENANT="21";
	/**appHub appToken标识**/
	public static final String HUB_APP_TOKEN = "hubAppToken";
	/**appHub 认证控制标志**/
    public static final String IS_NEED_APP_HUB="isNeedAppHub";
    
	/** prod_goods_state可销售状态主数据标识 **/
	public static final String MAINDATATYPE_SIGN_SALE_STATE = "prod_channel_sale_state";
	/** 201代表着是文档问题的内容 **/
	public static final String DOCPROBLEM = "201";
	
	/** 201代表着是文档问题的内容 **/
	public static final String FADIRIDAPI = "401";
	
	/** 201代表着是文档问题的内容 **/
	public static final String MAINDATACONTYPE = "203";
	
	/**卡号申请状态**/
	public static final String MAINDATATYPE_APPLY_INFO = "reseller_number_type";
	/**合作伙伴类型--企业分类**/
	public static final String RESALE_CORPORATION_TYPE = "RESALE_CORPORATION_TYPE";
	/**公司性质**/
	public static final String RESALE_CORPORATION_NATURE = "RESALE_CORPORATION_NATURE";
	/**法人证件类型**/
	public static final String LEGAL_CERT_TYPE = "RESALE_LEGAL_CERT_TYPE";
	/**合作伙伴 合同状态**/
	public static final String MAINDATATYPE_CONTRACT_STATE = "RESALE_STATUS_CD";
	/**转售商业务牌照**/
	public static final String RESALE_BUSINESS_LICENSE_ID = "RESALE_BUSINESS_LICENSE_ID";
	/**  **/
	public static final String NEVLCONSTYPE= "302";
	
	public static final String NODENUMBERCONS= "301";

	public static final String NODETYPECONS= "303";

	/**协议管理节点描述NODE_TYPE字段数据字典配置**/
	public static final String NODETYPE= "304";
	
	/**协议管理节点描述Java Field字段数据字典配置(REQ)**/
	public static final String JAVA_FIELD_REQ= "2024";	
	/**协议管理节点描述Java Field字段数据字典配置(RSP)**/
	public static final String JAVA_FIELD_RSP= "2025";
	
	/** 做为参数传入，表明属于API文档目录 **/
	public static final String APIDOCDIRID = "101,102,103";
	
	/** 601代表着是应用问题的内容 **/
	public static final String SUPPORTPROBLEM = "601";
	
	/** 做为参数传入，表明属于技术文档目录 **/
	public static final String SUPPORTDIRID = "('104','105')";
	
	/** 常量有效 **/
	public static final String COMM_STATE_VALID = "A";
	
	/** 常量失效 **/
	public static final String COMM_STATE_FAIL = "R";
	
	/** 常量新建 **/
	public static final String COMM_STATE_NEW = "A";
	
	/** 常量值待审核 **/
	public static final String COMM_STATE_WAITAUDI = "B";
	
	/** 常量值审核不通过 **/
	public static final String COMM_STATE_NOPASSAUDI = "C";
	
	/** 常量值上线 **/
	public static final String COMM_STATE_ONLINE = "D";
	
	/** 常量值下线 **/
	public static final String COMM_STATE_DOWNLINE = "G";
	
	/** 常量值删除 **/
	public static final String COMM_STATE_DELETE = "X";
	
	/** 常量值升级中 **/
	public static final String COMM_STATE_UPGRADE = "E";
	
	/** 常量值等待升级审核 **/
	public static final String COMM_STATE_WAITUPGRADE = "H";
	
	/** 常量值升级不通过 **/
	public static final String COMM_STATE_NOUPGRADE = "F";
	
	/** 常量值密码 **/
	public static final String COMM_STATE_PASSWEORD = "123";
	
	/** 组件类加常量值 **/
	public static final String COMM_STATE_TYPE_ID = "1";
	
	/** 用户注册流程ID **/
	public static final String PROCESS_MODEL_ID_ORGREG = "orgAuditProcess";
	
	/** 应用上线申请流程ID **/
	public static final String PROCESS_MODEL_ID_APPONLINE = "appAuditProcess";
	
	/** 应用升级申请流程ID **/
	public static final String PROCESS_MODEL_ID_APPUPGRADE = "appAuditProcess";
	
	/** 系统上线申请流程ID **/
	public static final String PROCESS_MODEL_ID_SYSONLINE = "systemAuditProcess";
	
	/** 系统升级申请流程ID **/
	public static final String PROCESS_MODEL_ID_SYSUPGRADE = "systemAuditProcess";
	 
	/** 融合产品审核流程ID  offerAuditProcess_mobile **/
	public static final String PROCESS_MODEL_ID_PRODOFFER = "offerAuditProcess";
	
	/** rti规则审计 **/
	public static final String PROCESS_MODEL_ID_RTI_RULE = "rtiRuleAuditProcess";
	 
	/** 测试桩接口测试流程ID **/
	public static final String PROCESS_TEST_PILES_FLOW_ID = "80000055";
	
	/**审核流程执行者**/
	public static final String PROCESS_AUTHENTICATED_USER_ID = "123456";
 
	
	
	
	/** 可销售产品申请 **/
	public static final String PROCESS_APPLY_PRODUCT_ORGREG = "20000";
	/** 节点类型包头 **/
	public static final String NODE_TYPE_HEAD = "1";
	
	/** 节点类型包体 **/
	public static final String NODE_TYPE_BODY = "2";
	
	/** 节点是否需要校验 **/
	public static final String IS_NEED_CHECK_YES = "Y";
	
	public static final String IS_NEED_CHECK_NO = "N";
	
	/** 节点签名 **/
	public static final String IS_NEED_SIGN_YES = "Y";
	
	public static final String IS_NEED_SIGN_NO = "N";
	
	public static final String ATTR_SPEC_ID = "19";

	
	/** 机构表状态主数据标识 - 属性规格单位 **/
	public static final String MAINDATATYPE_PROD_SPEC_UNIT = "prod_spec_unit";
	
	/** 套餐议价审核流程 ID **/
	public static final String PROCESS_MODEL_ID_MEALRATE = "20041";
	/** 合作伙伴审核流程 ID  产品>销售品**/
	public static final String PROCESS_MODEL_ID_PORDPARD = "20021";
	/** 新合作伙伴审核流程 ID 服务>产品**/
	public static final String PROCESS_MODEL_ID_PRODUCT = "productAuditProcess";
	/** 产品属性表在线状态值 **/
	public static final String PRODUCT_ATTR_ONLINE = "1000";
	
	/** 产品属性表下线状态值 **/
	public static final String PRODUCT_ATTR_GIVEUP = "1300";
	
	/** 产品定价在线状态值 **/
	public static final String PRODUCT_PRCING_ONLINE = "10";
	
	/** 产品定价表下线状态值 **/
	public static final String PRODUCT_PRCING_GIVEUP = "11";
	
	/** 产品定价类型 to最终用户 **/
	public static final String PRCING_TYPE_PRICETOUSER = "10";
	/** 产品定价类型 to合作伙伴 **/
	public static final String PRCING_TYPE_PRICETOWORK = "12";
	/** 产品定价类型 佣金**/
	public static final String PRCING_TYPE_COMMISSION = "11";
	
	/** 电信类产品类型**/
	public static final String PRODUCT_TYPE_IS_TEL = "14";
	
	
	/**分页显示条数**/
	public static final int EAAP_PAGE_RECORE_5 = 5;
	public static final int EAAP_PAGE_RECORE_10 = 10;
	public static final int EAAP_PAGE_RECORE_20 = 20;
	
	public static final String EAAP_MAIN_DATA_ONLINE = "A";
	public static final String EAAP_MAIN_DATA_GIVEUP = "R";

	public static final String DOUBLE_SLASH = "\\";
	public static final String SLASH = "/";
	
	/**转售     菜单是否是一级菜单标识**/
	public static final int PARENT_FUNCTION = -1 ;
	
	public static final String CONTRACT_FORMAT_REQ = "REQ";
	
	public static final String CONTRACT_FORMAT_RSP = "RSP";

	/** 是否是默认账户**/
	public static final String RESALE_PARTNER_ACCOUNT = "RESALE_PARTNER_ACCOUNT" ;
	/** 付款方式编码**/
	public static final String RESALE_PAYMENT_METHOD_CD = "RESALE_PAYMENT_METHOD_CD" ;
	/** 消息流管理调用url**/
	public static final String MESSAGE_FLOW_URL = "/messageArrange/messageFlow/toSomeMessageArrangeConfig.shtml" ;
	
	/**协议属性规格**/
	public static final String WEBSERVICE_OPERATION = "webserviceOperation";
	public static final String WEBSERVICE_Input = "webserviceInput";
	public static final String WEBSERVICE_Output = "webserviceOutput";
	
	/**报文模板类型**/
	public static final String MSG_FORMAT_TYPE = "msg_format_type";
	/**报文模板状态**/
	public static final String MSG_FORMAT_MOD_STATE = "test_msg_mod_state";
	/**模板关联对象类型**/
	public static final String MOD_OBJ_RELA_TYPE = "mod_obj_rela_type";
	/**是否启用通过表达式判断响应报文**/
	public static final String TEST_MSG_MOD_EXPRESSION_FLAG = "test_msg_mod_expression_flag";
	
	//-----------------------------------------------
	public static final String SPEC_MAINTAIN_TYPE = "SPEC_MAINTAIN_TYPE";
	public static final String DATE_TYPE = "data_type";
	public static final String COMPONENTPRICE_PRICETYPE = "ComponentPrice_PriceType";
	public static final String CURRENCY_UNIT_TYPE = "BasicTariff_CurrencyUnitType";
	public static final String BASE_ITEM_TYPE = "BasicTariff_BaseItemType";
	public static final String PRICINGPLAN_CYCLETYPE = "PricingPlan_CycleType";

	public static final String TAXINCLUDED = "BasicTariff_TaxIncluded";
	public static final String RATING_UNIT_TYPR = "BasicTariff_RatingUnitType";
	public static final String RATING_UNIT_TYPR_JL = "RatingUnitType_";

	public static final String PRICEITEM_ITEMTYPE = "PriceItem_ItemType";
	public static final String CHARGE_TYPE = "OneTimeCharge_ChargeType";
	public static final String PROMTYPE = "BillingDiscount_PromType";
	
	public static final String RATING_TYPE = "RATING_DISCOUNT_TYPE";
	public static final String RATING_BASIC = "RATING_DISCOUNT_STANDARD";
	
	public static final String TIME_SPEC_DATE_PATTERN = "TimeSegDef_DateMode";
	public static final String TIME_SPEC_TIME_PATTERN = "TimeSegDef_TimeMode";
	public static final String TimeSegDtl_DateWeek = "TimeSegDtl_DateWeek";
	
	/**默认名称**/
	public static final String DEFAULT_PRODUCT_NAME = "Product_";
	public static final String DEFAULT_PRODOFFER_NAME = "ProdOffer_";
	
	
	/**WSDL导入对象类型**/
	public static final String WSDL_IMPORT_OBJECT_TYPE = "wsdlImportObjectType";
	
	/** 时区在Cookie里的命名 **/
	public static final String TIME_OFFSET = "timeOffset"; 
	
	/** 用户名在Cookie里的命名 **/
	public static final String O2P_USER_NAME = "o2p_username"; 
	
	/** 用户名Cookie验证的命名 **/
	public static final String WEB_COOKIE_USERNAME_FILTER = "web_cookie_username_filter";
	
	/** 国际化 国家和语言的 配置名 **/
	public static final String LANGUAGE = "i18n.language";
	public static final String COUNTRY = "i18n.country"; 

	public static final String ZUOBI = "zuobi"; 
	public static final String SLA_RANGE = "SLA_RANGE"; 
	
	/** 定价计划生失效时间 **/
	public static final String ACTIVATION_OFFSET_TYPE = "Activation_Offset_Type"; 
	public static final String ACTIVATION_OFFSET = "Activation_Offset"; 
	public static final String PRICE_PLAN_BILLING_PERIOD_1 = "Price_Plan_Billing_Period_1"; 
	public static final String PRICE_PLAN_BILLING_PERIOD_2 = "Price_Plan_Billing_Period_2"; 
	public static final String PRICE_PLAN_VALIDITY_PERIOD = "Price_Plan_Validity_Period"; 
	public static final String PRICE_PLAN_BILLING_CYCLE_TYPE = "Price_Plan_Billing_Cycle_Type"; 
	
	/** 定价计划免费资源下拉框 **/
	/** Free Resource Cycle 免费资源赠送周期 **/
	public static final String PRICE_PLAN_FREE_RESOURCE_CYCLE		= "PricePlan_Free_Resource_Cycle";
	/** Free Resource Period  免费资源生效期**/
	public static final String PRICE_PLAN_FREE_RESOURCE_PERIOD	= "PricePlan_Free_Resource_Period";
	/** Expiry Type 无效标识**/
	public static final String PRICE_PLAN_EXPIRY_TYPE 						= "PricePlan_Expiry_Type";
	/** 流量单位**/
	public static final String PRICEPLAN_TRAFFIC_UNIT 						= "PricePlan_traffic_unit";
	/** 用户状态**/
	public static final String PRICEPLAN_SUBSCRIBER_STATUS 			= "PricePlan_Subscriber_Status";
	
	public static final String OFFER_PROD_REL_ROLE = "10600000"; 
	public static final String PROVIDER_ID = "800000003"; 
	public static final String SUB_BUSINESS = "2200028"; 
	public static final String THIRD_PARTY_PLATFORM = "2200024"; 
	public static final String SERVICE_PROVIDER = "2200025"; 
	public static final String DEPENDENT_TYPE="2800006"; 		//用于标识是“基础”还是“依赖”产品
	
	/**销售品状态**/
	public static final String STATUS_CD_FOR_PRODUCTOFFER_OFFSHEFL = "1600";
	public static final String STATUS_CD_FOR_PRODUCTOFFER_UP_PRICE = "1800";
	
	/**产品，销售品开发，生产配置参数**/
	public static final String STATUS_CD_FOR_TEST = "1000";
	public static final String STATUS_CD_FOR_ADD = "1200";
	public static final String SETTLE_RULE_TYPE = "SETTLE_RULE";
	public static final String SETTLE_CYCLE_TYPE = "SETTLE_CYCLE_TYPE";
	public static final String SETTLE_DIRECTORY_TYPE = "SETTLE_DIRECTORY_TYPE";
	public static final String SETTLE_CHARGE_DIR = "SETTLE_CHARGE_DIR";
	public static final String SETTLE_BILL_SOURCE_TYPE="RTI_SETTLE_BILL_SOURCE_TYPE";
	public static final String SETTLE_ACCUM_TYPE = "SETTLE_ACCUM_TYPE";
	public static final String SETTLE_ACCUM_DATE_TYPE = "SETTLE_ACCUM_DATE_TYPE";
	public static final String SETTLE_AGGREGATION_ITEM = "SETTLE_AGGREGATION_ITEM";
	public static final String ORGANIZATION_TYPE="RTI_ORGANIZATION_TYPE";
	public static final String ORGANIZATION_STATUS="RTI_ORGANIZATION_STATUS";
	public static final String SETTLE_PRIORITY_NUM = "500";
	public static final String SETTLE_ACCUM_DATE_TYPE_MONTH = "1";
	/**月份日期列表字符**/
	public static final String MONTH_DAY_TO_STRING = "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28";
	
	/**AC相关常量定义**/
	public static final String AUTH_CODE = "AUTH_CODE";
	public static final String REQ_TIME = "REQ_TIME";
	public static final String SOURCE_CODE = "SOURCE_CODE";
	public static final String ENCODE_TYPE = "md5";
	
	/**real ip address**/
	public static final String REAL_IP_ADDRESS = "REAL_IP_ADDRESS";
	
	/**界面操作日志类型**/
	public static final String SQL_INSERT = "I";
	public static final String SQL_UPDATE = "U";
	public static final String SQL_DELETE = "D";
	public static final String SQL_QUERY = "Q";
	
	/**消息、待办提醒**/
	public static final Integer MESSAGE_MSG_TYPE_STATION = 1;
	public static final Integer MESSAGE_MSG_TYPE_WORKFLOW = 2;
	public static final Integer MESSAGE_MSG_TYPE_TODO = 3;
	public static final String MESSAGE_MSG_TYPE = "MESSAGE_TYPE";
	public static final String MESSAGE_STATUS = "MESSAGE_STATUS";
	public static final String MESSAGE_MSG_WAY = "MESSAGE_WAY";
	public static final String MESSAGE_MSG_REC_TYPE = "MESSAGE_REC_TYPE";
	public static final String MESSAGE_ADD_MSG = "1000";//新建
	public static final String MESSAGE_DELETE_MSG = "1300";//删除
	//操作查看消息状态、忽略状态
	public static final String MESSAGE_LOOK_MSG = "1500";//已查看,已处理
	public static final String MESSAGE_IGNORE_MSG = "1600";//以忽略
	public static final String MESSAGE_LOOK_MSG_FLOW = "1700";//已查看，未处理
	//消息标题
	public static final String WORK_FLOW_MESSAGE_TITLE = "[OFFER ID=#id]Name=#name ";
	public static final String WORK_FLOW_MESSAGE_QUERY = "[OFFER ID=#id]";
	public static final String WORK_FLOW_MESSAGE_TITLE_PRODUCT = "[PRODUCT ID=#id]Name=#name ";
	public static final String WORK_FLOW_MESSAGE_QUERY_PRODUCT = "[PRODUCT ID=#id]";
	public static final String WORK_FLOW_MESSAGE_TITLE_COM = "[COMPONENT ID=#id]Name=#name ";
	public static final String WORK_FLOW_MESSAGE_QUERY_COM = "[COMPONENT ID=#id]";
	public static final String WORK_FLOW_MESSAGE_TITLE_APP = "[APP ID=#id]Name=#name ";
	public static final String WORK_FLOW_MESSAGE_QUERY_APP = "[APP ID=#id]";
	public static final String WORK_FLOW_MESSAGE_TITLE_ORG = "[Organization ID=#id]Name=#name ";
	public static final String WORK_FLOW_MESSAGE_QUERY_ORG = "[Organization ID=#id]";
	//待办消息O2P审核不通过 副标题内容
	public static final String WORK_FLOW_MESSAGE_O2P_CHECK_NOT_PASS_SUB_TITLE = "o2p audit is fail.";
	
	//UPC product flow
	public static final String WORK_FLOW_PRODUCT_AUDIT_SUCCESS_ACT_MODEL = "80000832";
	public static final String WORK_FLOW_PRODUCT_AUDIT_FAIL_ACT_MODEL = "80000830";
	
	/** 界面操作日志有效属性 **/
	public static final String SQLLOG_ISINTERCEPTOR = "sqlLog.isInterceptor";
	
	public static final String WEB_CONF_PATHS = "web.conf.paths";
	public static final String WEB_PORTAL_PATHS = "web.portal.paths";
	public static final String WEB_CROSS_DOMAIN = "web.cross.domain";
	public static final String WEB_SESSION_REPOSITORY = "web.session.repository";
	
	/**消息流异常处理信息状态**/
	public static final String EXCEPTION_DEAL_INFO_TRY_STATUS = "exceptionDealInfoTryStatus";

	/**业务对象类型**/
	public static final String OBJECT_TYPE_PRICE_EVENT = "1001";
	public static final String OBJECT_TYPE_SERVICE_PRICE_EVENT = "1002";
	public static final String OBJECT_TYPE_TIMDE_DEF = "1003";
	public static final String OBJECT_TYPE_CHAR_SPEC = "1004";
	
	
	/**异常日志对象编码**/
	public static final String OBJECT_TYPE_EXCEPTION_PRODUCT="1006";
	public static final String OBJECT_TYPE_EXCEPTION_PROD_OFFER="1007";
	public static final String OBJECT_TYPE_EXCEPTION_SERVICE="1008";
	public static final String OBJECT_TYPE_EXCEPTION_SETTLEMENT="1009";
	public static final String OBJECT_TYPE_EXCEPTION_PARTNER="1010";
	public static final String LOCAL_OBJECT_PRODUCT = "2004";
	public static final String LOCAL_OBJECT_OFFER = "2005";
	
	
	/**定单类型**/
	public static final String CRM_ORDER_TYPE = "CRM_ORDER_TYPE";
	
	/**
	 * 多租户ID portal
	 */
	public static final String TENANT_CODE = "TENANT_CODE";
	public static final String TENANT_CODE_DEFAULT = "Default";
	/** 默认租户ID **/
	public static final Integer TENANT_ID_DEFAULT =CommonUtil.getDefalutTenantId();
 
	/**服务规格类型——私有**/
	public static final String SERVICE_SPEC_TYPE_PRIVATE = "0";
	/**服务规格类型——公有**/
	public static final String SERVICE_SPEC_TYPE_PUBLIC = "1";
	
	/** CHAR SPEC 维护类型**/
	public static final String CHAR_SPEC_MAINTAIN_TYPE_PARTNER_SELF = "2";
	public static final String CHAR_SPEC_MAINTAIN_TYPE_OPERATOR = "3";
	public static final String CHAR_SPEC_IS_CUSTOMIZED_Y = "Y";
	public static final String CHAR_SPEC_IS_CUSTOMIZED_N = "N";
	public static final String CHAR_SPEC_TYPE_SERV_SPEC = "SERVICE";
	public static final String CHAR_SPEC_TYPE_PRODUCT_SPEC = "PRODUCT";
	public static final String CHAR_SPEC_TYPE_OFFER = "OFFER";
	public static final String CHAR_SPEC_TYPE_PRICE = "4";
	
	//channel 
	public static final String SALE_CHANNEL_TYPE_PARTNER = "1";
	public static final String SALE_CHANNEL_TYPE_OPERATOR = "2";
	
	
	/** 映射对象类型**/
	public static final String ENT_MAP_SRC_TYPE_CHAR_SPEC = "110";
	
	public static final String ENT_MAP_SRC_CODE_UPC = "UPC";
	
	/** 版本类型**/
	public static final String O2P_WEB_DOMAIN_CLOUD = "cloud";
	public static final String O2P_WEB_DOMAIN_LOCAL = "local";
	public static final String PORTAL_CACHE_MAP = "cacheMap";
	
	public static final String CLOUD_LOCAL_PRODUCT_PROCESS = "cloudSyncLocalProductProcess";
	public static final String CLOUD_LOCAL_OFFER_PROCESS = "cloudSyncLocalOfferProcess";
	
	public static final String CLOUD_LOCAL_AUDIT_INTERFACE_RESULTCODE_TREU = "1";
	public static final String CLOUD_LOCAL_AUDIT_INTERFACE_RESULTCODE_FALSE = "0";
	public static final String CLOUD_LOCAL_AUDIT_INTERFACE_BUSICODE_PRODUCT = "ProductExamineResults";
	public static final String CLOUD_LOCAL_AUDIT_INTERFACE_BUSICODE_PRODUCT_MODALE_OFFER = "PRODUCT";
	public static final String CLOUD_LOCAL_AUDIT_INTERFACE_BUSICODE_OFFER = "OfferExamineResults";
	public static final String CLOUD_LOCAL_AUDIT_INTERFACE_BUSICODE_OFFER_MODALE_OFFER = "OFFER";
	public static final String CLOUD_LOCAL_AUDIT_INTERFACE_BUSICODE_OFFER_MODALE_SETTLE = "SETTLE";
	
	public static final String O2P_WEB_DOMAIN = CommonUtil.getPropertyValue("o2p_web_domin");
	
	// 工作流待办url 变量
	public static final String FRONT_END_URL = "$FRONT_END_URL";
	public static final String CONTENT_ID = "$CONTENT_ID";

	/**
	 * 判断是否是o2p_cloud
	 * @return
	 */
	public static final Boolean isCloud(){
		if(EAAPConstants.O2P_WEB_DOMAIN_CLOUD.equals(O2P_WEB_DOMAIN)){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
