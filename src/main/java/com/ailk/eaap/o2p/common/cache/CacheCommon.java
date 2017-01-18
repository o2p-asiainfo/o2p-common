package com.ailk.eaap.o2p.common.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.asiainfo.foundation.log.Logger;

import com.ailk.eaap.op2.bo.Api;
import com.ailk.eaap.op2.bo.App;
import com.ailk.eaap.op2.bo.BizFunction;
import com.ailk.eaap.op2.bo.CacheStrategy;
import com.ailk.eaap.op2.bo.Component;
import com.ailk.eaap.op2.bo.ContractVersion;
import com.ailk.eaap.op2.bo.CsvTemplate;
import com.ailk.eaap.op2.bo.CtlCounterms2Comp;
import com.ailk.eaap.op2.bo.FuzzyEncryption;
import com.ailk.eaap.op2.bo.Org;
import com.ailk.eaap.op2.bo.RemoteCallInfo;
import com.ailk.eaap.op2.bo.Service;
import com.ailk.eaap.op2.bo.TechImpl;
import com.ailk.eaap.op2.bo.Template;


public class CacheCommon {
	
	private static final Logger LOG = Logger.getLog(CacheCommon.class);
	
	private CacheCommon(){}
	
	public static void dataRemoveHandler(ICache<String, Object> cacheClient, List<String> preKeyList, List<String> nowKeyList){
		try{
			if(preKeyList==null || preKeyList.isEmpty() || nowKeyList==null || nowKeyList.isEmpty())
			{
				return;
			}
			List<String> tempList = new ArrayList<String>();
			for(String nowkey : nowKeyList){
				if(preKeyList.contains(nowkey))
				{
					tempList.add(nowkey);
				}
			}
			preKeyList.removeAll(tempList);
				for(String deleteKey : preKeyList){
					cacheClient.remove(deleteKey);
					if(LOG.isDebugEnabled()){
						LOG.debug("remove object from cache with key="+deleteKey);
					}
				}
		}catch(RuntimeException e){
			LOG.error("remove data from cache faild!", e.getCause());
		}
	}
	
	public static String prefixReplace(String sourceStr, int type){
		switch(type){
			case 1:
				return sourceStr.replace("MODULE", "KEY_LIST");
			case 2:
				return sourceStr.replace("MODULE", "ERROR_KEY_LIST");
			case 3:
				return sourceStr.replace("MODULE", "ERROR_CAUSE");
			default : return null;
		}
	}
	
	public static void logDebugMessage(final Logger logger, String tenantId, String moduleName, int type) {
		if (logger.isDebugEnabled()) {
			logger.debug("load " + moduleName + " " + (type == 0 ? "start" : "end") + "[tenantId=" + tenantId +"]");
		}
	}

	public static boolean excetionHandler(final Logger logger, ICache<String, Object> cacheClient, String tenantId, String moduleName, Exception e) {
		logger.error("load " + moduleName + " failed[tenantId=" + tenantId + "]", e);
		cacheClient.put(tenantId + moduleName, CacheKey.MODULE_RELOAD_EXCEPTION);
		cacheClient.put(tenantId + prefixReplace(moduleName, 3), e);
		return false;
	}

	public static void beforePutHandler(String moduleName, Object obj) {
		if (CacheKey.MODULE_COMPONENT.equals(moduleName)) {
			Component component = (Component) obj;
			Org org = new Org();
			org.setOrgCode(component.getOrgCode());
			org.setState(component.getOrgState());
			component.setOrg(org);
		}
	}

	public static void putObjectListIntoCache(String tenantId, ICache<String, Object> cacheClient, String moduleName, List<?> objectList, Map<String, String> keyRules, String dbVersion) throws Exception {
		if (objectList != null && !objectList.isEmpty()) {
			List<String> keyList = new ArrayList<String>();
			List<String> errorKeyList = new ArrayList<String>();
			Set<Map.Entry<String, String>> entry = keyRules.entrySet();
			for (Object obj : objectList) {
				beforePutHandler(moduleName, obj);
				for (Map.Entry<String, String> kv : entry) {
					putObjectIntoCache(tenantId, cacheClient, obj, keyList, errorKeyList, kv.getValue(), kv.getKey());
				}
			}
			afterPutHandler(tenantId, cacheClient, moduleName, keyList, errorKeyList);
		}
		cacheClient.put(tenantId + moduleName, dbVersion);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void putObjectMapIntoCache(String tenantId, ICache<String, Object> cacheClient, String moduleName, Map objectMap,
			String dbVersion) throws Exception {
		if (objectMap != null && !objectMap.isEmpty()) {
			List<String> keyList = new ArrayList<String>();
			List<String> errorKeyList = new ArrayList<String>();
			Set<Map.Entry> entry = objectMap.entrySet();
			for (Map.Entry<String, ?> kv : entry) {
				putObjectIntoCache(tenantId, cacheClient, kv.getValue(), keyList, errorKeyList, kv.getKey(), "key");
			}
			afterPutHandler(tenantId, cacheClient, moduleName, keyList, errorKeyList);
		}
		cacheClient.put(tenantId + moduleName, dbVersion);
	}

	public static void afterPutHandler(String tenantId, ICache<String, Object> cacheClient, String moduleName, List<String> keyList, List<String> errorKeyList) throws Exception {
		@SuppressWarnings("unchecked")
		List<String> preKeyList = (List<String>) cacheClient.get(tenantId + prefixReplace(moduleName, 1));
		cacheClient.put(tenantId + prefixReplace(moduleName, 1), keyList);
		if (errorKeyList.isEmpty()) {
			cacheClient.remove(tenantId + prefixReplace(moduleName, 2));
		} else {
			cacheClient.put(tenantId + prefixReplace(moduleName, 2), errorKeyList);
			throw new Exception("Load data into cache failed! ErrorKeyList is " + errorKeyList.toString());
		}
		keyList.addAll(errorKeyList);
		dataRemoveHandler(cacheClient, preKeyList, keyList);
	}

	public static void putObjectIntoCache(String tenantId, ICache<String, Object> cacheClient, Object obj, List<String> keyList, List<String> errorKeyList, String keyPrefix, String keyType) {
		String key = keyPrefix + getKeySuffix(obj, keyType);
		if (cacheClient.put(tenantId + key, obj)) {
			keyList.add(tenantId + key);
		} else {
			errorKeyList.add(tenantId + key);
		}
	}

	public static void putObjectIntoCache(String tenantId, ICache<String, Object> cacheClient, Object obj, List<String> keyList, List<String> errorKeyList, String key) {
		if (cacheClient.put(tenantId + key, obj)) {
			keyList.add(tenantId + key);
		} else {
			errorKeyList.add(tenantId + key);
		}
	}

	public static Object getKeySuffix(Object obj, String keyType) {
		if ("key".equals(keyType)) {
			return "";
		}
		if ("value".equals(keyType)) {
			return obj;
		} else if ("ApiId".equals(keyType)) {
			return ((Api) obj).getApiId();
		} else if ("Template".equals(keyType)) {
			return ((Template)obj).getTcpCtrFId();
		} else if ("ApiMethod".equals(keyType)) {
			return ((Api) obj).getApiMethod();
		} else if ("ApiServiceId".equals(keyType)) {
			return ((Api) obj).getServiceId();
		} else if ("Appkey".equals(keyType)) {
			return ((App) obj).getAppkey();
		} else if ("AppComponentCode".equals(keyType)) {
			return ((App) obj).getComponentCode();
		} else if ("BusCode".equals(keyType)) {
			return ((BizFunction) obj).getCode();
		} else if ("ComponentCode".equals(keyType)) {
			return ((Component) obj).getCode();
		} else if ("ContractVersion".equals(keyType)) {
			return ((ContractVersion) obj).getVersion();
		} else if ("FlowSerInvokeInsId".equals(keyType)) {
			return ((CtlCounterms2Comp) obj).getSerInvokeInsId()+""+((CtlCounterms2Comp) obj).getCcCd();
		} else if ("OrgCode".equals(keyType)) {
			return ((Org) obj).getOrgCode();
		} else if ("ServiceCode".equals(keyType)) {
			return ((Service) obj).getServiceCode();
		} else if ("SerTechId".equals(keyType)) {
			return ((TechImpl) obj).getSerTechId();
		} else if ("ServiceId".equals(keyType)) {
			return ((Service) obj).getServiceId();
		} else if ("FuzzyEncryptionId".equals(keyType)) {
			return ((FuzzyEncryption) obj).getFuzzyEncryptionId();
		} else if ("RemoteCallUrlId".equals(keyType)) {
			return ((RemoteCallInfo) obj).getRemoteCallUrlId();
		} else if ("CsvTemplateId".equals(keyType)) {
			return ((CsvTemplate) obj).getCsvTemplateId();
		} else if ("OrgId".equals(keyType)) {
			return ((Org) obj).getOrgId();
		} else if ("CacheStrategyId".equals(keyType)) {
			return ((CacheStrategy) obj).getId();
		}
		return null;
	}

	public static boolean addByList(String tenantId, ICache<String, Object> cacheClient, Logger logger, String moduleName, List<?> objectList, String dbVersion, Map<String, String> keyRules) {
		logDebugMessage(logger, tenantId, moduleName, 0);
		try {
			putObjectListIntoCache(tenantId, cacheClient, moduleName, objectList, keyRules, dbVersion);
			logDebugMessage(logger, tenantId, moduleName, 1);
		} catch (Exception e) {
			return excetionHandler(logger, cacheClient, tenantId, moduleName, e);
		}
		return true;
	}

	public static boolean addByMap(final Logger logger, ICache<String, Object> cacheClient, String tenantId, String moduleName, String dbVersion, Map<String, ?> map) {
		logDebugMessage(logger, tenantId, moduleName, 0);
		try {
			putObjectMapIntoCache(tenantId, cacheClient, moduleName, map, dbVersion);
			logDebugMessage(logger, tenantId, moduleName, 1);
		} catch (Exception e) {
			return excetionHandler(logger, cacheClient, tenantId, moduleName, e);
		}
		return true;
	}
}

