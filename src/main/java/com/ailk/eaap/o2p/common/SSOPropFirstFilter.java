package com.ailk.eaap.o2p.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ailk.eaap.o2p.common.spring.config.EncryptPropertyPlaceholderConfigurer;
import com.asiainfo.foundation.util.StringUtils;
import com.asiainfo.portal.framework.external.HttpPost;
import com.asiainfo.portal.framework.external.HttpsPost;
import com.asiainfo.portal.framework.external.IPopedom;
import com.asiainfo.portal.framework.external.PortalDataFetch;
import com.asiainfo.portal.framework.external.SSOHelper;

public class SSOPropFirstFilter implements Filter {
	private ArrayList arrPathList = new ArrayList();
	private String strImplClassName = "";
	public static String isLog = "false";

	public static String isAdLogin = "false";

	public static String adLoginContextName = "ADSSOLOGINCONTEXT";
	protected static String resKey = "";

	private static String crmSSOServerName = "";

	private static String serverNameOf4A = "";

	private static Boolean _IS_INIT = Boolean.valueOf(false);

	private static String _ACTIVE_METHOD = "redirect";
	private static String redirectUrl = "";
	public static final String actionName = "activeSession";
	private static IPopedom ipopedom = null;

	private static String _COOKIE_DOMAIN = "telenor.com";
	private static ThreadLocal s_locale = new ThreadLocal();
	private static ThreadLocal s_user = new ThreadLocal();

	private static String _D_SSOSERVER = "sso.server";

	private static String _D_SSOCOOKIEDOMAIN = "sso.cookieDomain";

	private static String _D_REDIRECTURL = "sso.redirectUrl";

	private static String _D_PORTAL_SERVERNAME_REDIRECT = "sso.server.redirect";

	private static String _D_PORTAL_SERVERNAME_REQUEST = "sso.server.request";

	private static String _SSO_REDIRECT = "";

	private static String _SSO_HTTP_REQUEST = "";

	private static String clientName = null;

	private static String cookiePath = null;

	private static int _ACTIVE_INTERVAL_TIME = 5;

	private static final Log log = LogFactory.getLog(SSOPropFirstFilter.class);

	private static Boolean IS_SSO_FILTER_OPEN = true;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String isOpen = EncryptPropertyPlaceholderConfigurer
				.getContextProperty("isSSOFilterOpen");
		if (StringUtils.hasText(isOpen)) {
			IS_SSO_FILTER_OPEN = Boolean.valueOf(isOpen);
		}
		if (IS_SSO_FILTER_OPEN) {
			if (!_IS_INIT.booleanValue()) {
				synchronized (_IS_INIT) {
					crmSSOServerName = EncryptPropertyPlaceholderConfigurer
							.getContextProperty("portal-servername");

					if ((crmSSOServerName == null)
							|| ("".equals(crmSSOServerName))) {
						crmSSOServerName = System.getProperty(_D_SSOSERVER);
					}

					_SSO_REDIRECT = EncryptPropertyPlaceholderConfigurer
							.getContextProperty("portal-servername-redirect");
					if ((_SSO_REDIRECT == null) || ("".equals(_SSO_REDIRECT))) {
						_SSO_REDIRECT = System
								.getProperty(_D_PORTAL_SERVERNAME_REDIRECT);
					}

					_SSO_HTTP_REQUEST = EncryptPropertyPlaceholderConfigurer
							.getContextProperty("portal-servername-getstatus");
					if ((_SSO_HTTP_REQUEST == null)
							|| ("".equals(_SSO_HTTP_REQUEST))) {
						_SSO_HTTP_REQUEST = System
								.getProperty(_D_PORTAL_SERVERNAME_REQUEST);
					}

					isLog = EncryptPropertyPlaceholderConfigurer
							.getContextProperty("is-log");
					if (isLog == null) {
						isLog = EncryptPropertyPlaceholderConfigurer
								.getContextProperty("ISLOG");
					}
					serverNameOf4A = EncryptPropertyPlaceholderConfigurer
							.getContextProperty("4a-servername");
					redirectUrl = EncryptPropertyPlaceholderConfigurer
							.getContextProperty("crm-redirectUrl");
					if ((redirectUrl == null) || ("".equals(redirectUrl))) {
						redirectUrl = System.getProperty(_D_REDIRECTURL);
					}
					clientName = EncryptPropertyPlaceholderConfigurer
							.getContextProperty("client-name");

					cookiePath = EncryptPropertyPlaceholderConfigurer
							.getContextProperty("cookie-path");

					if (isLog == null) {
						isLog = "false";
					}
					isAdLogin = EncryptPropertyPlaceholderConfigurer
							.getContextProperty("is-activedirectory");
					if (isAdLogin == null) {
						isAdLogin = "false";
					}

					String allPath = EncryptPropertyPlaceholderConfigurer
							.getContextProperty("allow-path");
					if (allPath == null) {
						allPath = EncryptPropertyPlaceholderConfigurer
								.getContextProperty("ALLOWPATH");
					}
					if (allPath == null) {
						allPath = "";
					}
					String[] tempArr = split(allPath, ";");
					for (int i = 0; i < tempArr.length; i++)
						this.arrPathList.add(tempArr[i]);
					try {
						String strImplClassName = EncryptPropertyPlaceholderConfigurer
								.getContextProperty("impl-classname");
						if (ipopedom == null) {
							Class cls = Class.forName(strImplClassName);
							ipopedom = (IPopedom) cls.newInstance();
						}
					} catch (Exception e) {
						log.error("SSOPropFirstFilter init error", e);
						throw new ServletException(
								"sso-client initial exception.");
					}
					String activeTime = EncryptPropertyPlaceholderConfigurer
							.getContextProperty("active-interval-minute-time");
					_ACTIVE_METHOD = EncryptPropertyPlaceholderConfigurer
							.getContextProperty("active-method");
					if ((activeTime != null) && (!"".equals(activeTime))) {
						_ACTIVE_INTERVAL_TIME = Integer.parseInt(activeTime);
					}
					String cookieDomain = EncryptPropertyPlaceholderConfigurer
							.getContextProperty("cookie-domain");
					if ((cookieDomain == null) || ("".equals(cookieDomain))) {
						cookieDomain = System.getProperty(_D_SSOCOOKIEDOMAIN);
					}
					if ((cookieDomain != null) && (!"".equals(cookieDomain))) {
						_COOKIE_DOMAIN = cookieDomain;
					}
					_IS_INIT = Boolean.valueOf(true);
				}
			}
		}
	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httprequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpresponse = (HttpServletResponse) servletResponse;
		try {
			if (IS_SSO_FILTER_OPEN) {
				boolean isPass = ipopedom.setFirstPopedom(httprequest,
						httpresponse, this.arrPathList, crmSSOServerName);

				if (isPass) {
					HttpSession session = httprequest.getSession(false);
					String ssoSessionId = PortalDataFetch
							.getSessionId(httprequest);
					String ssoHome = getSSOHttpRequestAddr();
					if ((session != null)
							&& (PortalDataFetch.getSessionId(httprequest) != null)
							&& (PortalDataFetch.getSessionId(httprequest)
									.length() > 0)
							&& ((httprequest.getMethod()
									.equalsIgnoreCase("GET")) || (httprequest
									.getMethod().equalsIgnoreCase("POST")))) {
						long lastAccessedTime = PortalDataFetch
								.getLastAccessedTime(httprequest);
						if ((lastAccessedTime > 0L)
								&& (System.currentTimeMillis()
										- lastAccessedTime > 300000L)) {
							StringBuffer activeUrl = new StringBuffer();
							if ((ssoHome != null) && (!"".equals(ssoHome))) {
								if (ssoHome.endsWith("/"))
									activeUrl.append(ssoHome)
											.append("portalcheckout?action=")
											.append("activeSession")
											.append("&clientName=")
											.append(clientName);
								else {
									activeUrl.append(ssoHome)
											.append("/portalcheckout?action=")
											.append("activeSession")
											.append("&clientName=")
											.append(clientName);
								}
							}

							if ("post".equalsIgnoreCase(getActiveMethod())) {
								activeUrl.append("&activeMethod=post");
								String result = "";
								if (ssoHome.startsWith("https")) {
									result = HttpsPost.doPost(
											activeUrl.toString(), ssoSessionId,
											"UTF-8", 1000, 1000);
								}
								result = HttpPost.doHttpPost(
										activeUrl.toString(), ssoSessionId);

								if ((result != null)
										&& ("success".equalsIgnoreCase(result))) {
									long curTime = System.currentTimeMillis();
									SSOHelper.setLastAccessedTime(httpresponse,
											curTime);
								} else {
									SSOHelper.setLastAccessedTime(httpresponse,
											0L);
								}
							} else {
								activeUrl.append("&activeMethod=redirect");
								activeUrl.append("&ssoSessionId=").append(
										ssoSessionId);
								HttpPost.sendRedirectUrl(httprequest,
										httpresponse, activeUrl.toString());
							}
						}
					}
					filterChain.doFilter(servletRequest, servletResponse);
				}
			} else {
				filterChain.doFilter(servletRequest, servletResponse);
			}
		} catch (Exception e) {
			log.error("SSOPropFirstFilter filter error", e);
		}
	}

	public static boolean portalCheckOut(String strCheckOutUrl,
			String strSessionId) throws Exception {
		boolean flag = false;
		String returnVal = "";
		if (strCheckOutUrl.startsWith("https"))
			returnVal = HttpsPost.doPost(strCheckOutUrl, strSessionId, "utf-8",
					1000, 1000);
		else {
			returnVal = HttpPost.doHttpPost(strCheckOutUrl, strSessionId);
		}
		if (returnVal.equalsIgnoreCase("success")) {
			flag = true;
		}
		return flag;
	}

	public static String[] split(String str, String x) {
		if (str == null) {
			return null;
		}
		if (x == null) {
			return null;
		}
		Vector v = new Vector();
		StringTokenizer stToken = new StringTokenizer(str, x);
		int iIndex = 0;
		while (stToken.hasMoreTokens()) {
			String strToken = stToken.nextToken();
			v.add(iIndex++, strToken);
		}
		String[] seqResult = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			String strTemp = (String) v.get(i);
			seqResult[i] = strTemp;
		}
		return seqResult;
	}

	public static String getActiveMethod() {
		return _ACTIVE_METHOD;
	}

	@Override
	public void destroy() {
		this.arrPathList = new ArrayList();
		ipopedom = null;
	}

	public static String getCRMSSOServerName() {
		return crmSSOServerName;
	}

	public static String get4ASSOServerName() {
		return serverNameOf4A;
	}

	public static String getRedirectUrl() {
		return redirectUrl;
	}

	public static String getCookieDomain() {
		return _COOKIE_DOMAIN;
	}

	public static IPopedom getPopedomImpl() {
		return ipopedom;
	}

	public static boolean getIsAdLogin() {
		return !"false".equals(isAdLogin);
	}

	public static String getAdLoginContextName() {
		return adLoginContextName;
	}

	public static String getClientname() {
		return clientName;
	}

	public static String getSSOHttpRequestAddr() {
		if ((crmSSOServerName == null) || ("".equals(crmSSOServerName))) {
			return _SSO_HTTP_REQUEST;
		}
		return crmSSOServerName;
	}

	public static String getSSORedirectAddr() {
		if ((crmSSOServerName == null) || ("".equals(crmSSOServerName))) {
			return _SSO_REDIRECT;
		}
		return crmSSOServerName;
	}

	public static String getCookiePath() {
		if (cookiePath != null) {
			return cookiePath;
		}
		return "/";
	}
}
