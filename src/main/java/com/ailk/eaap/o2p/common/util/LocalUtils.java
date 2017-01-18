/** 
 * Project Name:o2p-common 
 * File Name:LocalUtils.java 
 * Package Name:com.ailk.eaap.o2p.common.util 
 * Date:2015年2月2日上午11:46:53 
 * Copyright (c) 2015, www.asiainfo.com All Rights Reserved. 
 * 
 */

package com.ailk.eaap.o2p.common.util;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.util.StringUtils;

import com.asiainfo.foundation.log.Logger;

/**
 * ClassName:LocalUtils 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON. 
 * Date: 2015年2月2日 上午11:46:53 
 * 
 * @author zhongming
 * @version
 * @since JDK 1.6
 * 
 */
public class LocalUtils {
	private static final Logger log = Logger.getLog(CacheUtil.class);
	/**
	 * 获取本地IP getLocalIp:(这里用一句话描述这个方法的作用). 
	 * TODO(这里描述这个方法适用条件 – 可选).
	 * TODO(这里描述这个方法的执行流程 – 可选).
	 * TODO(这里描述这个方法的使用方法 – 可选).
	 * TODO(这里描述这个方法的注意事项 – 可选).
	 * 
	 * @author zhongming
	 * @return
	 * @throws UnknownHostException
	 * @since JDK 1.6
	 */
	public static String getLocalIp() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}
	
	/**
	 * 获取本地IPV4 IP， 在linux和windows下适用
	 * @return
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
	public static String getLocalRealIp() {
		try {
			Enumeration<NetworkInterface> allNetInterfaces;
			allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			String networkCardName = System.getProperty("networkCardName");
			if(StringUtils.hasText(networkCardName)) {
				NetworkInterface netInterface = NetworkInterface.getByName(networkCardName);
				if(netInterface == null) {
					log.warn("no find networkcard of name {0}, use the default one", networkCardName);
				} else {
					Enumeration<InetAddress> addresses = netInterface
							.getInetAddresses();
					while (addresses.hasMoreElements()) {
						ip = addresses.nextElement();
						if (ip != null && (ip instanceof Inet4Address)
								&& (!"127.0.0.1".equals(ip.getHostAddress()))) {
							return ip.getHostAddress();
						}
					}
					log.warn("no find right ip address of the networkcard {0}, use the default one", networkCardName);
				}
			} else {
				log.warn("no find networkcard name in the system variable, use the default one");
			}
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = allNetInterfaces.nextElement();
				Enumeration<InetAddress> addresses = netInterface
						.getInetAddresses();
				while (addresses.hasMoreElements()) {
					ip = addresses.nextElement();
					if (ip != null && (ip instanceof Inet4Address)
							&& (!"127.0.0.1".equals(ip.getHostAddress()))) {
						return ip.getHostAddress();
					}
				}
			}
			return getLocalIp();
		} catch (SocketException e) {
			return "127.0.0.1";
		} catch (UnknownHostException e) {
			return "127.0.0.1";
		}
	}

	// 获取服务器端口
	public static String getLocalByWebPort(String type, String url) {

		return "";
	}

	// 获取Tomcat端口
	@SuppressWarnings("unchecked")
	public static String getLocalTomcatPort(String url)
			throws DocumentException, IOException {
		File file = new File(url);
		Document doc = DocumentHelper.parseText(FileUtils
				.readFileToString(file));
		List<Element> list = doc
				.selectNodes("//Connector[@protocol='HTTP/1.1']");
		if (list.size() == 0) {
			list = doc.selectNodes("//Connector[@protocol='org.apache.coyote.http11.Http11NioProtocol']");
		}
		if (list.size() == 0) {
			return "";
		}
		Element e = list.get(0);
		return e.attributeValue("port");
	}

	// 获取WebLogic端口
	@SuppressWarnings("unchecked")
	public static String getLocalWebLogicPort(String url)
			throws DocumentException, IOException {
		File file = new File(url);
		Document doc = DocumentHelper.parseText(FileUtils
				.readFileToString(file));
		Iterator<Element> itRoot = doc.getRootElement().elementIterator();
		while (itRoot.hasNext()) {
			Element element = itRoot.next();
			if (element.getName().equals("server")) {
				return element.element("ssl").element("listen-port").getText();
			}
		}
		return "";
	}
	
	public static String getSystemVariable(String key) {
		try {
			String value = System.getProperty(key);
			if (value == null) {
				value = System.getenv(key);
			}
			return value;
		}
		catch (Exception ex) {
			if (log.isDebugEnabled()) {
				log.debug("Could not access system property '" + key + "': " + ex);
			}
			return null;
		}
	}

	// 获取Jboss端口
	public static String getLocalJbossPort(String url) {

		return "";
	}

	// 获取WebSphere
	public static String getLocalWebSpherePort(String url) {

		return "";
	}

}
