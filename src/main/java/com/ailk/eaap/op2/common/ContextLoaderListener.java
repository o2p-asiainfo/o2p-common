package com.ailk.eaap.op2.common;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ailk.eaap.o2p.common.util.CommonUtil;
import com.asiainfo.foundation.log.Logger;

public class ContextLoaderListener implements ServletContextListener {
	private static final Logger log = Logger.getLog(ContextLoaderListener.class);
	public static final String O2P_CONFIGLOCATION = "o2pConfigLocation";
	public static final String APPLICATION_STYLE_THEME = "contextStyleTheme";
	public static final String APPLICATION_STYLE_SPECIAL = "contextStyleSpecial";
	public static final String APPLICATION_MENU_BELONGTO = "contextMenuBelongto";
	
	public static final String FRONT_END_URL = "frontEnd_url";
	
	public static final String O2P_PORTAL_URL = "PORTAL_URL";
	public static final String APP_PATH = "APP_PATH";
	
	
	public void contextInitialized(ServletContextEvent event) {
		log.debug("Servlet Context init....");
		
		ServletContext servletContext = event.getServletContext();
//		//锟斤拷式锟斤拷锟解，取/resource/目录锟斤拷锟斤拷一锟斤拷锟斤拷锟斤拷锟绞�
//		String StyleTheme = servletContext.getInitParameter(APPLICATION_STYLE_THEME);
//		servletContext.setAttribute(APPLICATION_STYLE_THEME, StyleTheme);
//		//锟斤拷锟斤拷目专锟斤拷图片目录锟斤拷取/resource/styleTheme/images/目录锟斤拷锟斤拷一锟斤拷锟斤拷目专锟斤拷目录
//		String styleSpecial = servletContext.getInitParameter(APPLICATION_STYLE_SPECIAL);
//		servletContext.setAttribute(APPLICATION_STYLE_SPECIAL, styleSpecial);
//		
//		//锟斤拷锟截讹拷应系统锟侥菜碉拷
//		String menuBelongto = servletContext.getInitParameter(APPLICATION_MENU_BELONGTO);
//		servletContext.setAttribute(APPLICATION_MENU_BELONGTO, menuBelongto);
		
		
		//锟斤拷式锟斤拷锟解，取/resource/目录锟斤拷锟斤拷一锟斤拷锟斤拷锟斤拷锟绞�
		String contextStyleTheme = null;
		String contextStyleSpecial = null;
		String contextMenuBelongto = null;
		String frontEndUrl=null;
		String portalUrl = null;
		
		if(null != CommonUtil.getPropertyValue(APPLICATION_STYLE_THEME)){
			contextStyleTheme = CommonUtil.getPropertyValue(APPLICATION_STYLE_THEME);
		}
		if(null != CommonUtil.getPropertyValue(APPLICATION_STYLE_SPECIAL)){
			contextStyleSpecial = CommonUtil.getPropertyValue(APPLICATION_STYLE_SPECIAL);
		}
		if(null != CommonUtil.getPropertyValue(APPLICATION_MENU_BELONGTO)){
			contextMenuBelongto = CommonUtil.getPropertyValue(APPLICATION_MENU_BELONGTO);
		}
		if(null != CommonUtil.getPropertyValue(FRONT_END_URL)){
			frontEndUrl = CommonUtil.getPropertyValue(FRONT_END_URL);
		}
		if(null != CommonUtil.getPropertyValue(O2P_PORTAL_URL)){
			portalUrl = CommonUtil.getPropertyValue(O2P_PORTAL_URL);
		}
		servletContext.setAttribute(FRONT_END_URL, frontEndUrl); 
		servletContext.setAttribute(O2P_PORTAL_URL, portalUrl);
		servletContext.setAttribute(APP_PATH, servletContext.getContextPath()); 

		//锟斤拷式锟斤拷锟解，取/resource/目录锟斤拷锟斤拷一锟斤拷锟斤拷锟斤拷锟绞�
		servletContext.setAttribute(APPLICATION_STYLE_THEME, contextStyleTheme);
		//锟斤拷锟斤拷目专锟斤拷图片目录锟斤拷取/resource/styleTheme/images/目录锟斤拷锟斤拷一锟斤拷锟斤拷目专锟斤拷目录
		servletContext.setAttribute(APPLICATION_STYLE_SPECIAL, contextStyleSpecial);
		//锟斤拷锟截讹拷应系统锟侥菜碉拷
		servletContext.setAttribute(APPLICATION_MENU_BELONGTO, contextMenuBelongto);
	}

	public void contextDestroyed(ServletContextEvent event) {

	}
}
