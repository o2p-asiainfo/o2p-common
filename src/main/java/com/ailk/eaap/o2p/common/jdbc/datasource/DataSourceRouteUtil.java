package com.ailk.eaap.o2p.common.jdbc.datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.springframework.util.StringUtils;

import com.ailk.eaap.o2p.common.cache.CacheFactory;
import com.ailk.eaap.o2p.common.cache.CacheKey;
import com.ailk.eaap.o2p.common.security.SecurityUtil;
import com.ailk.eaap.op2.bo.Tenant;
import com.asiainfo.foundation.log.Logger;
import com.ailk.eaap.op2.bo.JdbcDataSource;
import com.linkage.rainbow.util.ExprUtil;
import com.linkage.rainbow.util.context.ContextUtil;
import com.linkage.rainbow.util.expr.core.Expr;

public final class DataSourceRouteUtil {
	private final static Logger LOG =Logger.getLog(DataSourceRouteUtil.class);
	
	private DataSourceRouteUtil(){
	    
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, DataSource> loadDataSourceList(@SuppressWarnings("rawtypes") CacheFactory cacheFactory) {
		Map<String, DataSource> result = new HashMap<String, DataSource>();
		List<Tenant> allTenant = (List<Tenant>) cacheFactory.getCacheClient().get(CacheKey.TENANT_ALL);
		List<JdbcDataSource> dsList = new ArrayList<JdbcDataSource>();
		if(allTenant != null) {
			for(Tenant t: allTenant) {
				List<JdbcDataSource> jds = (List<JdbcDataSource>) cacheFactory.getCacheClient().get(t.getTenantId()+CacheKey.ALL_DATA_SOURCE);
				if(jds != null) {
					dsList.addAll(jds);
				}
			}
		}
		for(JdbcDataSource jd: dsList) {
			DataSource ds = getRealDataSource(jd);
			if(ds != null) {
				result.put(jd.getDataSourceName(), ds);
			}
		}
		return result;
	}
	
	private static DataSource getRealDataSource(JdbcDataSource jd) {
		if(DataSourceConstant.YES.equalsIgnoreCase(jd.getIsBeginInit())) {
			try {
				if(StringUtils.hasText(jd.getJndiName())) {
					Context context = new InitialContext();
					return (DataSource) context.lookup("java:comp/env/"+jd.getJndiName());
				} else {
					jd.setPassword(SecurityUtil.getInstance().decryMsg(jd.getPassword()));
					return jd;
				}
			} catch(Exception e) {
				LOG.error("init datasource error，datasource name :" + jd.getDataSourceName(), e);
				return null;
			}
		}
		return null;
	}

	/**
	 * 取得数据源缓存列表 
	 * @return  List<Map>  主要字段包括DATA_SOURCE_NAME,COMPONENT_ID,TAB_SUFFIX
	 */
	public static List<Map<String, Object>> getDataSourceRouteList(){
		return (List<Map<String, Object>>)DataSourceCache.getDataSourceRouteMap();
	}
	
	/**
	 * 设置线程变量中的数据源名称
	 * @param dsName
	 */
	public static void putContextDataSourceName(String dsName){
		ContextUtil.put(DataSourceConstant.DepDataSourceContextName,dsName); //数据源名称
	}
	
	/**
	 * 取得线程变量中的数据源名称
	 * @param dsName
	 */
	public static String getContextDataSourceName(){
		return (String)ContextUtil.get(DataSourceConstant.DepDataSourceContextName); //数据源名称
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Map<String, Object>> loadDataSourceRouteList(CacheFactory cacheFactory) {
		List<Tenant> allTenant = (List<Tenant>) cacheFactory.getCacheClient().get(CacheKey.TENANT_ALL);
		List<Map<String, Object>> dataSourceRouteList = new ArrayList<Map<String, Object>>();
		if(allTenant != null) {
			for(Tenant t: allTenant) {
				List<Map<String, Object>> jds = (List<Map<String, Object>>) cacheFactory.getCacheClient().get(t.getTenantId()+CacheKey.ALL_DATA_SOURCE_ROUTE);
				if(jds != null) {
					dataSourceRouteList.addAll(jds);
				}
			}
		}
		Iterator dataSourceRouteIterator = dataSourceRouteList.iterator();
		while(dataSourceRouteIterator.hasNext()){
			Map map = (Map)dataSourceRouteIterator.next();
			String decisionRule = (String)map.get("DECISION_RULE");
			Expr expr = ExprUtil.createExpr(decisionRule); 
			map.put("EXPR", expr);
		}
		return dataSourceRouteList;
	}
}