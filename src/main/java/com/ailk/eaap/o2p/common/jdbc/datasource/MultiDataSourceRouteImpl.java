package com.ailk.eaap.o2p.common.jdbc.datasource;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.asiainfo.foundation.log.LogModel;
import com.asiainfo.foundation.log.Logger;
import com.linkage.rainbow.dao.IMultiDataSourceRoute;
import com.linkage.rainbow.util.expr.core.Expr;

public class MultiDataSourceRouteImpl implements IMultiDataSourceRoute{
private final static Logger logger =Logger.getLog(MultiDataSourceRouteImpl.class);
	
	//取得BusCode，正则表达式
	private final static Pattern busCodePattern = Pattern.compile("<TcpCont>.*?<BusCode>(.*?)</BusCode>.*?</TcpCont>",Pattern.DOTALL);
	//取得ServiceCode，正则表达式
	private final static Pattern svcCodePattern = Pattern.compile("<TcpCont>.*?<ServiceCode>(.*?)</ServiceCode>.*?</TcpCont>",Pattern.DOTALL);
	//取得SrcOrgID，正则表达式
	private final static Pattern orgIdPattern = Pattern.compile("<TcpCont>.*?<SrcOrgID>(.*?)</SrcOrgID>.*?</TcpCont>",Pattern.DOTALL);

	/**
	 * 数据源路由操作
	 * 支持buscode,svccode,regionID 三个维度的判断
	 * @param originalMessage 接收到的报文
	 */
	@Override
	public void setRoutingRules(String originalMessage) {
		String orgId = null;
		String busCode = null;
		String svcCode = null;
		//从报文头中获取orgId,busCode,svcCode
		try {
			Matcher matcher = busCodePattern.matcher(originalMessage);
			if (matcher.find()) {
				busCode = matcher.group(1).trim();
			}
			matcher = svcCodePattern.matcher(originalMessage);
			if (matcher.find()) {
				svcCode = matcher.group(1).trim();
			}			
			matcher = orgIdPattern.matcher(originalMessage);
			if (matcher.find()) {
				orgId = matcher.group(1).trim();
			}
		} catch (RuntimeException e) {
			logger.error(LogModel.EVENT_APP_EXCPT, e);
		}
		if(orgId != null && busCode != null && svcCode!= null){
			List<Map<String, Object>> dataSourceRouteList = DataSourceRouteUtil.getDataSourceRouteList();
			Iterator<Map<String, Object>> it = dataSourceRouteList.iterator();
			Map<String, Object> para = new ConcurrentHashMap<String, Object>();
			para.put("ORG_ID", orgId);
			para.put("BUS_CODE", busCode);
			para.put("SVC_CODE", svcCode);
			while(it.hasNext()){
				Map<String, Object> map = (Map<String, Object>)it.next();
				Expr expr = (Expr)map.get("EXPR");
				Object result = expr.calculate(para);
				if(result != null && result.toString().equals("true")){
					DataSourceRouteUtil.putContextDataSourceName((String)map.get(DataSourceConstant.DATA_SOURCE_MAP));
					break;
				}
			} 
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setRoutingRules(@SuppressWarnings("rawtypes") Map paraMap) {
		Map<String,Object> msgRouteMap = new ConcurrentHashMap<String,Object>();//消息过来的路由条件最终形式
		msgRouteMap.putAll(paraMap);
		if(logger.isDebugEnabled()) {
			logger.debug("route rule， param map:{0}", paraMap);
		}
		List<Map<String, Object>> dataSourceRouteList = DataSourceRouteUtil.getDataSourceRouteList();
		Iterator<Map<String, Object>> it = dataSourceRouteList.iterator();
		String dataSourceName="";
		while(it.hasNext()){
			Map<String, Object> map = (Map<String, Object>)it.next();
			Expr expr = (Expr)map.get("EXPR");
			Object result = expr.calculate(msgRouteMap);
			if(result != null && result.toString().equals("true")){
				dataSourceName = (String)map.get(DataSourceConstant.DATA_SOURCE_NAME);
				break;
			}else {
				if(map.get(DataSourceConstant.IS_DEFAULT)!=null && map.get(DataSourceConstant.IS_DEFAULT).toString().equals("0")){
					dataSourceName = (String)map.get(DataSourceConstant.DATA_SOURCE_NAME);
				}
			}
		}
		logger.info("route dataSourceName: {0}", dataSourceName);
		DataSourceRouteUtil.putContextDataSourceName(dataSourceName);
	}

	@Override
	public String getDataSourceKey() {
		return DataSourceRouteUtil.getContextDataSourceName();
	}

}
