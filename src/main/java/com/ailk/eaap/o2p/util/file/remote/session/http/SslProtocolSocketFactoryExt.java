/** 
 * Project Name:o2p-serviceAgent-core 
 * File Name:SslProtocolSocketFactoryExt.java 
 * Package Name:com.ailk.eaap.op2.serviceagent.common 
 * Date:2015年5月2日下午1:42:38 
 * Copyright (c) 2015, www.asiainfo.com All Rights Reserved. 
 * 
*/  
  
package com.ailk.eaap.o2p.util.file.remote.session.http;  

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/** 
 * ClassName:SslProtocolSocketFactoryExt  
 * Function: TODO ADD FUNCTION.  
 * Reason:   TODO ADD REASON.  
 * Date:     2015年5月2日 下午1:42:38  
 * @author   颖勤 
 * @version   
 * @since    JDK 1.6 
 * @see       
 */
public class SslProtocolSocketFactoryExt implements ProtocolSocketFactory{
	private Resource keyStore;

	private Resource trustStore;

	private char[] keyStorePassword;

	private char[] trustStorePassword;

	private volatile String protocol = "TLS";
	
	public SslProtocolSocketFactoryExt(String trustStore, String trustStorePassword){
		Assert.notNull(trustStore, "trustStore cannot be null");
		Assert.notNull(trustStorePassword, "trustStorePassword cannot be null");
		Resource  resolver = new FileSystemResource(trustStore);
		this.trustStore = resolver;
		this.trustStorePassword = trustStorePassword.toCharArray();
	}
	public SSLContext getSSLContext() throws GeneralSecurityException,
	IOException {
		KeyStore ts = KeyStore.getInstance("JKS");
		ts.load(trustStore.getInputStream(), trustStorePassword);
		TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
		tmf.init(ts);
		SSLContext sslContext = SSLContext.getInstance(protocol);
		sslContext.init(null, tmf.getTrustManagers(), null);
		return sslContext;
	}
	public SSLContext getSSLContext(boolean isClient)throws GeneralSecurityException,
	IOException{
		if(isClient){
			KeyStore ts = KeyStore.getInstance("JKS");
			ts.load(trustStore.getInputStream(), trustStorePassword);
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(ts);
			SSLContext sslContext = SSLContext.getInstance(protocol);
			sslContext.init(null, tmf.getTrustManagers(), null);
			return sslContext;
		}else{
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(keyStore.getInputStream(), keyStorePassword);
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, keyStorePassword);
			SSLContext sslContext = SSLContext.getInstance(protocol);
			X509TrustManager X509 = new X509TrustManager()
			{
				public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException
				{
				}

				public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			};
			TrustManager[] X509_MANAGERS = { X509 };
			sslContext.init(kmf.getKeyManagers(), X509_MANAGERS, null);
			return sslContext;
		}
	}	
	  public Socket createSocket(Socket socket, String host, int port, boolean autoClose)   
	          throws IOException, UnknownHostException, GeneralSecurityException {   
	      return getSSLContext().getSocketFactory().createSocket(   
	              socket,   
	              host,   
	              port,   
	              autoClose   
	          );   
	  }   
	  
	  public Socket createSocket(String host, int port) throws IOException,   
	          UnknownHostException {   
	      try {
			return getSSLContext().getSocketFactory().createSocket(   
			          host,   
			          port   
			      );
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;   
	  }   
	   
	   
	  public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort)   
	          throws IOException, UnknownHostException {   
	      try {
			return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			throw new IOException("getSSLContext Exception",e.getCause());
		}
	  }
	@Override
	public Socket createSocket(String host, int port, InetAddress localAddress,
			int localPort, HttpConnectionParams params) throws IOException,
			UnknownHostException, ConnectTimeoutException {
	      if (params == null) {   
	          throw new IllegalArgumentException("Parameters may not be null");   
	      }   
	      int timeout = params.getConnectionTimeout();   
	      SocketFactory socketfactory = null;
	      try{
	    	  socketfactory = getSSLContext().getSocketFactory();   
	      }catch(Exception e){
	    	 throw new IOException("get SSLContext Exception",e.getCause());
	      }
	      
	      if (timeout == 0) {   
	          return socketfactory.createSocket(host, port, localAddress, localPort);   
	      } else {   
	          Socket socket = socketfactory.createSocket();   
	          SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);   
	          SocketAddress remoteaddr = new InetSocketAddress(host, port);   
	          socket.bind(localaddr);   
	          socket.connect(remoteaddr, timeout);   
	          return socket;   
	      }
	}   
	  
 

}
