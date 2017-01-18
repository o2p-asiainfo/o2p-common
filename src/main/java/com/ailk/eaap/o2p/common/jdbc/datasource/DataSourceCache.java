package com.ailk.eaap.o2p.common.jdbc.datasource;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.FactoryBean;

import com.ailk.eaap.o2p.common.cache.CacheFactory;


public final class DataSourceCache implements Serializable, FactoryBean<Map<String, DataSource>>{
	@SuppressWarnings({"rawtypes" })
	private CacheFactory cacheFactory;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -291612420355960646L;
	
	private DataSourceCache(){
	    
	}
	
	private static Map<String, DataSource> dataSourceMap = null;
	private static List<Map<String, Object>> dataSourceRouteMap = null;

	public static List<Map<String, Object>> getDataSourceRouteMap() {
		return dataSourceRouteMap;
	}

	public static void setDataSourceRouteMap(List<Map<String, Object>> dataSourceRouteMap) {
		DataSourceCache.dataSourceRouteMap = dataSourceRouteMap;
	}

	@Override
	public Map<String, DataSource> getObject() throws Exception {
		if(dataSourceMap == null) {
			dataSourceMap = DataSourceRouteUtil.loadDataSourceList(cacheFactory);
		}
		if(dataSourceRouteMap == null) {
			dataSourceRouteMap = DataSourceRouteUtil.loadDataSourceRouteList(cacheFactory);
		}
		return dataSourceMap;
	}

	@Override
	public Class<?> getObjectType() {
		return Map.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	public void setCacheFactory(CacheFactory cacheFactory) {
		this.cacheFactory = cacheFactory;
	}
}