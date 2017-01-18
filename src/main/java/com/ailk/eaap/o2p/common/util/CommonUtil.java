package com.ailk.eaap.o2p.common.util;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.ailk.eaap.o2p.common.spring.config.ZKCfgCacheHolder;
import com.ailk.eaap.op2.bo.Tenant;
import com.ailk.eaap.op2.common.EAAPConstants;
import com.asiainfo.foundation.common.ExceptionCommon;
import com.asiainfo.foundation.exception.BusinessException;
import com.asiainfo.foundation.log.LogModel;
import com.asiainfo.foundation.log.Logger;
import com.linkage.rainbow.util.StringUtil;

/**
 * @ClassName: CommonUtil
 * @Description: 
 * @author zhengpeng
 * @date 2015-3-26 下午6:05:01
 *
 */
public class CommonUtil {
	private static Logger log = Logger.getLog(CommonUtil.class);
	public static final String URL_TENANT_CODE = "/URL_TENANT_CODE";
	public static final String URL_TENANT_ID = "/URL_TENANT_ID";
	
	/**
	 * 从session里获取到时间差
	 * @param session
	 * @return
	 */
	public static int getTimeOffsetForSession(HttpSession session){
		int timeOffest = 0;
		if(session != null){
			if(session.getAttribute(EAAPConstants.TIME_OFFSET) != null){
				timeOffest = Integer.valueOf(session.getAttribute(EAAPConstants.TIME_OFFSET).toString());
			//将cookie里timeOffest放入session
			}else{
				Cookie[] cookies = ServletActionContext.getRequest().getCookies();
				if(cookies != null){
					for (Cookie cookie : cookies) {
					    if(EAAPConstants.TIME_OFFSET.equals(cookie.getName())){
					    	timeOffest = CommonUtil.getTimeOffsetForCookie(cookie);
					    	session.setAttribute(EAAPConstants.TIME_OFFSET, String.valueOf(timeOffest));
					    }
					}
				}
			}
		}
		return timeOffest;
	}
	
	
	/**
	 * 从Cookie获取到时区值
	 * @param cookie
	 * @return
	 */
	public static int getTimeOffsetForCookie(Cookie cookie){
		int timeOffset = 0;
		if(!StringUtil.isEmpty(cookie.getValue())){
			timeOffset = Integer.valueOf(cookie.getValue());
		}
		return timeOffset;
	}

	/**
	 * 从session里获租户对象（Tenant）
	 * @param session
	 * @return
	 */
	public static Tenant getTenant(HttpSession session){
		Tenant tenant = new Tenant();
		if(session != null){
			if(session.getAttribute("Tenant") != null){
				tenant = (Tenant) session.getAttribute("Tenant");
			}else{
				Cookie[] cookies = ServletActionContext.getRequest().getCookies();
				if(cookies != null){
					String userTenantId="";
					String tenantId="";
					String tenantName="";
					String tenantCode="";
					String tenantLogo="";
					String tenantCountry="";
					String tenantProvince="";
					String tenantLanguage="";
					String tenantZipCode="";
					String tenantContractNum="";
					String tenantCurrency="";
					String tenantStatus="";
					String tenantTimeZone="";
					String tenantTheme="";
					String tenantIdCookieSessionName = CommonUtil.getValueByProCode("user_tenant_id");		//从公共配置文件eaap_common.properties取
					for (Cookie cookie : cookies) {
					    if(tenantIdCookieSessionName.equals(cookie.getName())){
					    	userTenantId = CommonUtil.getCookieVal(cookie);
					    	session.setAttribute(tenantIdCookieSessionName, userTenantId);
					    }else if("Tenant.TenantId".equals(cookie.getName())){
					    	tenantId = CommonUtil.getCookieVal(cookie);
							tenant.setTenantId(Integer.valueOf(tenantId));
					    }else if("Tenant.Name".equals(cookie.getName())){
					    	tenantName = CommonUtil.getCookieVal(cookie);
							tenant.setName(tenantName);
					    }else if("Tenant.Code".equals(cookie.getName())){
					    	tenantCode = CommonUtil.getCookieVal(cookie);
							tenant.setCode(tenantCode);
					    }else if("Tenant.Logo".equals(cookie.getName())){
					    	tenantLogo = CommonUtil.getCookieVal(cookie);
							tenant.setLogo(tenantLogo);
					    }else if("Tenant.Country".equals(cookie.getName())){
					    	tenantCountry = CommonUtil.getCookieVal(cookie);
							tenant.setCountry(tenantCountry);
					    }else if("Tenant.Province".equals(cookie.getName())){
					    	tenantProvince = CommonUtil.getCookieVal(cookie);
							tenant.setProvince(tenantProvince);
					    }else if("Tenant.Language".equals(cookie.getName())){
					    	tenantLanguage = CommonUtil.getCookieVal(cookie);
							tenant.setLanguage(tenantLanguage);
					    }else if("Tenant.ZipCode".equals(cookie.getName())){
					    	tenantZipCode = CommonUtil.getCookieVal(cookie);
							tenant.setZipCode(tenantZipCode);
					    }else if("Tenant.ContractNum".equals(cookie.getName())){
					    	tenantContractNum = CommonUtil.getCookieVal(cookie);
							tenant.setContractNum(tenantContractNum);
					    }else if("Tenant.Currency".equals(cookie.getName())){
					    	tenantCurrency = CommonUtil.getCookieVal(cookie);
							tenant.setCurrency(tenantCurrency);
					    }else if("Tenant.Status".equals(cookie.getName())){
					    	tenantStatus = CommonUtil.getCookieVal(cookie);
							tenant.setStatus(tenantStatus);
					    }else if("Tenant.TimeZone".equals(cookie.getName())){
					    	tenantTimeZone = CommonUtil.getCookieVal(cookie);
							tenant.setTimeZone(tenantTimeZone);
					    }else if("Tenant.Theme".equals(cookie.getName())){
					    	tenantTheme = CommonUtil.getCookieVal(cookie);
							tenant.setTheme(tenantTheme);
					    }
					}
					if(tenantId!=null && tenantId!=""){
						session.setAttribute("Tenant",tenant);
					}else if(userTenantId!=null && userTenantId!=""){
						tenant.setTenantId(Integer.valueOf(userTenantId));
						//tenant = getTenantService().getTenant(tenant);		//从数据库中取
						session.setAttribute("Tenant",tenant);
					}
				}
			}
		}
		return tenant;
	}
	
	/**
	 * 从session里获租户ID（TenantId）
	 * @param session
	 * @return
	 */
	public static String getTenantId(HttpSession session){
		String tenantId = "";
		if(session != null){
			String tenantIdCookieSessionName = getPropertyValue("user_tenant_id");		//从公共配置文件eaap_common.properties取
			if(session.getAttribute(tenantIdCookieSessionName) != null){
				tenantId = session.getAttribute(tenantIdCookieSessionName).toString();
			}else{
				//将cookie里tenantId放入session
				Cookie[] cookies = ServletActionContext.getRequest().getCookies();
				if(cookies != null){
					for (Cookie cookie : cookies) {
					    if(tenantIdCookieSessionName.equals(cookie.getName())){
					    	tenantId = CommonUtil.getCookieVal(cookie);
					    	session.setAttribute(tenantIdCookieSessionName, tenantId);
					    }
					}
				}
			}
		}
		return tenantId;
	}

	public static String getCookieVal(Cookie cookie){
		String val = "";
		if(!StringUtil.isEmpty(cookie.getValue())){
			val = cookie.getValue();
			if(!StringUtil.isEmpty(val)){
				byte[] bytes =CustomBase64.decode(val);
				val = new String(bytes);
			}
		}
		return val;
	}	
	
	
	/**
	 * URL地址替换租户信息
	 * @param url  url地址
	 * @param tenantId  租户ID
	 * @return
	 */
	public static String changeUrlByTenantId(String url,String tenantId){
		if(tenantId == null){
			tenantId = String.valueOf(CommonUtil.getDefalutTenantId()); 
		}
		
		if(null != url && url.contains(CommonUtil.URL_TENANT_ID)){
			return url.replace(CommonUtil.URL_TENANT_ID, tenantId);
		}
		
		return url;
	}
	
	public static Integer getDefalutTenantId(){
		String defaultTenantId = getPropertyValue("default_tenant_id");
		if(StringUtil.isEmpty(defaultTenantId)){
			defaultTenantId = "1";
		}
		return Integer.valueOf(defaultTenantId);
	}
	
	public static String getPropertyValue(String propertyKey){
		Object propertyValueObj = ZKCfgCacheHolder.PROP_ITEMS.get(propertyKey);
		log.debug("log ############# Property key:" + propertyKey + "|| Property value:" + propertyValueObj);
		String propertyValue = "";
		if(propertyValueObj != null){
			propertyValue = String.valueOf(propertyValueObj).trim();
		}else{
			log.info("Property value are not in the zk; propertyKey name:" + propertyKey); 
			try{
				propertyValue = CommonUtil.getValueByProCode(propertyKey);
			}catch (Exception e) {
				log.error(LogModel.EVENT_APP_EXCPT, new BusinessException(
						ExceptionCommon.WebExceptionCode,"Get eaap_commom file fail ........",e));
			}
		}
		return propertyValue;
	}
	
	/**
	 * 从配置文件获取配置值
	 *        不推荐直接使用，以修改成从ZK获取属性值
	 * @param proCode
	 * @return
	 */
	private static String getValueByProCode(String proCode) {
		Properties p = new Properties();
		try {
			p.load(CommonUtil.class.getResourceAsStream("/eaap_common.properties"));
			return (String) p.get(proCode); 
		} catch (IOException e) {
			log.error(LogModel.EVENT_APP_EXCPT, new BusinessException(
					ExceptionCommon.WebExceptionCode,"Gets the configuration file in the Chinese value anomaly",
					e));
			return null;
		}
	}
	
	/**
	 * URL地址替换租户信息
	 * @param url  url地址
	 * @param tenantId  租户ID
	 * @return
	 */
	public static String changeUrlByTenantCode(String url,String tenantCode){
		if(tenantCode == null){
//			tenantCode = String.valueOf(EAAPConstants.TENANT_CODE_DEFAULT);
			tenantCode = "";
		}else{
			tenantCode += "/";
		}
		
		if(null != url && url.contains(CommonUtil.URL_TENANT_CODE)){
			return url.replace(CommonUtil.URL_TENANT_CODE, tenantCode);
		}
		
		return url;
	}
}
