package com.ailk.eaap.o2p.util.file.remote.session.http;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.wltea.expression.ExpressionEvaluator;
import org.wltea.expression.datameta.Variable;

import com.ailk.eaap.o2p.common.security.SecurityUtil;
import com.ailk.eaap.o2p.util.file.remote.session.DownloadCallBack;
import com.ailk.eaap.o2p.util.file.remote.session.Session;
import com.ailk.eaap.o2p.util.file.remote.session.model.ExtAttr;
import com.ailk.eaap.op2.bo.EOPDomain;
import com.ailk.eaap.op2.bo.EndpointAttr;
import com.ailk.eaap.op2.bo.TechImpl;
import com.ailk.eaap.op2.common.EAAPException;
import com.ailk.eaap.op2.common.EAAPTags;
import com.ailk.eaap.op2.common.StringUtil;
import com.asiainfo.foundation.exception.BusinessException;
import com.asiainfo.foundation.log.LogModel;
import com.asiainfo.foundation.log.Logger;

public class HttpSession<F, T> implements Session<T, F> {
	
	private static final  Logger LOG = Logger.getLog(HttpSession.class);
	
	public boolean remove(String path) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	public T[] list(String path) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public T[] list(String path, String filterPattern) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void read(String source, OutputStream outputStream)
			throws IOException {

	}
	
	public void write(InputStream inputStream, String destination)
			throws IOException {
		// TODO Auto-generated method stub

	}

	public boolean mkdir(String directory) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean changeDir(String directory) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	public void rename(String pathFrom, String pathTo) throws IOException {
		// TODO Auto-generated method stub

	}

	public void close() {
		// TODO Auto-generated method stub

	}

	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean exists(String path) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	public String[] listNames(String path) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean downloadFromDir(String remoteDir, String localPath,
			DownloadCallBack<F> callBack) throws EAAPException {
		// TODO Auto-generated method stub
		return false;
	}

//	public boolean downloadFromUrl(String url,String downloadDir) throws EAAPException{
//		HttpClient client = new DefaultHttpClient();
//		String fileFormat = "filename_21";
//		httpDownload(client,url,downloadDir,fileFormat);
//		return true;
//	}
	public boolean downloadFromUrl(String url,String downloadDir,PreDownLoadHandler handler, List<ExtAttr> moveFileInfos){
//		HttpClient client = new DefaultHttpClient();
//		url = handler.downloadRuleExecute();
//		httpDownload(client,url,downloadDir,moveFileInfos);
		return true;
	}	
	
	public void httpDownload(HttpClient httpClient,String url,String downloadDir, List<ExtAttr> moveFileInfos, TechImpl tech){
		String paramUrl = getParamUrl(moveFileInfos);
		String srcFilePathName = "";
		String fileName = "";
		HttpMethod httpGet = null;
		try {
			if(!"".equals(paramUrl)) {
				
				url = paramUrl;
				fileName = paramUrl.substring(paramUrl.lastIndexOf("/")+1, paramUrl.length());
			}
			
			if(LOG.isDebugEnabled()) {
				
				LOG.debug("==========> url="+paramUrl+", fileName="+fileName);
			}
			 
			httpGet = getHttpGet(url, null, null);
			
			addProxy(httpClient, tech);
			
			if(url.startsWith("https")) {
				
				int port = 443;
				if(tech.getAttrMap().get(EndpointAttr.PORT)!=null){
					
					port = Integer.parseInt(tech.getAttrMap().get(EndpointAttr.PORT).toString());
				}

				if(LOG.isDebugEnabled()){
					LOG.debug("call https....");
				}
				
				String keyStorePassword=null;
				String keyStore =null;
				
				String trustStorePassword=null;
				String trustStore =null;
				
				if(tech.getAttrMap().get(EndpointAttr.KEY_STORE_PASSWORD)!=null&&!"".equals(tech.getAttrMap().get(EndpointAttr.KEY_STORE_PASSWORD))){
					keyStorePassword = tech.getAttrMap().get(EndpointAttr.KEY_STORE_PASSWORD).toString();
					keyStorePassword = SecurityUtil.getInstance().decryMsg(keyStorePassword);
				}
				
				if(tech.getAttrMap().get(EndpointAttr.KEY_STORE)!=null&&!"".equals(tech.getAttrMap().get(EndpointAttr.KEY_STORE))){
					keyStore = tech.getAttrMap().get(EndpointAttr.KEY_STORE).toString();
				}
				
				if(tech.getAttrMap().get(EndpointAttr.TRUST_STORE_PASSWORD)!=null&&!"".equals(tech.getAttrMap().get(EndpointAttr.TRUST_STORE_PASSWORD))){
					trustStorePassword = tech.getAttrMap().get(EndpointAttr.TRUST_STORE_PASSWORD).toString();
					trustStorePassword = SecurityUtil.getInstance().decryMsg(trustStorePassword);
				}
				
				if(tech.getAttrMap().get(EndpointAttr.TRUST_STORE)!=null&&!"".equals(tech.getAttrMap().get(EndpointAttr.TRUST_STORE))){
					trustStore = tech.getAttrMap().get(EndpointAttr.TRUST_STORE).toString();
				}
				Protocol myhttps = new Protocol("https", new AuthSSLProtocolSocketFactory((keyStore==null?null:new URL(keyStore)),keyStorePassword,(trustStore==null?null:new URL(trustStore)),trustStorePassword), port);  
				Protocol.registerProtocol("https", myhttps);
				
				if(LOG.isDebugEnabled()) {
					LOG.debug("keyStore="+keyStore+" keyStorePd="+keyStorePassword+" trustStore="+trustStore+" trustStorePd="+trustStorePassword);
				}

			}
			
			int statusCode = httpClient.executeMethod(httpGet);
			 
			if (statusCode == HttpStatus.SC_OK) {

				byte[] result = httpGet.getResponseBody();
				BufferedOutputStream bw = null;
				try {
					if("".equals(fileName)) {
						
						Header[] attachmentheader = httpGet.getResponseHeaders();
						if(attachmentheader!=null&&attachmentheader.length>0){
							String attachment = attachmentheader[0].getValue();
							String dowloadprefix = "filename=";
							int flag = attachment.indexOf(dowloadprefix);
							if(flag!=-1){
								fileName = attachment.substring(flag+dowloadprefix.length(), attachment.length());
							}else{
								long temp = RandomUtils.nextLong();
								//									String response.getLastHeader("Content-Type");
								fileName = String.valueOf(temp);
							}
						}else{
							int flag = url.lastIndexOf("/");
							fileName = url.substring(flag+1,url.length());
						}
					}
					
					 
					srcFilePathName = downloadDir+"/"+fileName;
					if(LOG.isDebugEnabled()) {
						
						LOG.debug("==========> downloadDir="+srcFilePathName);
					}
					FileOutputStream outputStream = new FileOutputStream(srcFilePathName+".ing");
					bw = new BufferedOutputStream(outputStream);
					bw.write(result);
//					FileUtils.moveFile(new File(srcFilePathName+".ing"), new File(srcFilePathName));
				} catch (Exception e) {
					LOG.error("http download fail,the path="+url);
					throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN, "9214", "http download fail,the path="+url, e);
				} finally {
					try {
						if (bw != null)
							bw.close();
						File tempFile = new File(srcFilePathName+".ing");
						tempFile.renameTo(new File(srcFilePathName));
					} catch (Exception e) {
						throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN, "9213", "finally BufferedOutputStream shutdown close", e);
					}
				}
			}
			 
			else {
				StringBuffer errorMsg = new StringBuffer();
				errorMsg.append("httpStatus:");
				errorMsg.append(statusCode+",");
				errorMsg.append("responseBody:"+StringUtil.getString(httpGet.getResponseBodyAsStream(), EOPDomain.CHARSET_UTF8));
				errorMsg.append(", Header: ");
				Header[] headers = httpGet.getResponseHeaders();
				for (Header header : headers) {
					errorMsg.append(header.getName());
					errorMsg.append(":");
					errorMsg.append(header.getValue());
				}
				LOG.error("HttpResonse Error:" + errorMsg);
				throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN, "9214", "HttpResonse Error:"+errorMsg);
			}
		} catch (ConnectException e) {
			LOG.error("HttpResonse Error:,path=" + url + ",url=" + url, e);
			String responseInfo="HttpResponse Error:,path=" + url + ",url=" + url;
			throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN, "9214", responseInfo, e);
		} catch (Exception e) {
			LOG.error("HttpResonse Error:,path=" + url + ",url=" + url, e);
			String responseInfo="HttpResponse Error:,path=" + url + ",url=" + url;
			throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN, "9214", responseInfo, e);
		} finally {
			try {
				httpGet.releaseConnection();
			} catch (Exception e) {
				LOG.error("finally HttpClient shutdown error", e);
				throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN, "9214", "finally HttpClient shutdown error", e);
			}
		}
	}
	
	/**
	 * 获得HttpGet对象
	 * 
	 * @param url
	 *             
	 * @param params
	 *             
	 * @param encode
	 *             
	 * @return HttpGet对象
	 */
	private static HttpMethod getHttpGet(String url, Map<String, String> params,
			String encode) {
		StringBuffer buf = new StringBuffer(url);
		if (params != null) {
			 
			String flag = (url.indexOf('?') == -1) ? "?" : "&";
			 
			for (String name : params.keySet()) {
				buf.append(flag);
				buf.append(name);
				buf.append("=");
				try {
					String param = params.get(name);
					if (param == null) {
						param = "";
					}
					buf.append(URLEncoder.encode(param, encode));
				} catch (UnsupportedEncodingException e) {
					LOG.error("URLEncoder Error,encode=" + encode + ",param="
							+ params.get(name), e);
				}
				flag = "&";
			}
		}
		HttpMethod httpMethod = new GetMethod(buf.toString());
		return httpMethod;
	}
	
	private String getParamUrl(List<ExtAttr> moveFileInfos) {

		if(moveFileInfos != null && moveFileInfos.size() > 0){
			
			for (ExtAttr extAttr : moveFileInfos) {
				if(!StringUtils.isEmpty(extAttr.getFileName())){

					return extAttr.getFileName();
				}
			}
		}
		return "";
	}
	
	private void addHttps(TechImpl tech) throws MalformedURLException {
		int port = 443;
		if(tech.getAttrMap().get(EndpointAttr.PORT)!=null){
			
			port = Integer.parseInt(tech.getAttrMap().get(EndpointAttr.PORT).toString());
		}

		if(LOG.isDebugEnabled()){
			LOG.debug("call https....");
		}
		
		String keyStorePassword=null;
		String keyStore =null;
		
		String trustStorePassword=null;
		String trustStore =null;
		
		if(tech.getAttrMap().get(EndpointAttr.KEY_STORE_PASSWORD)!=null&&!"".equals(tech.getAttrMap().get(EndpointAttr.KEY_STORE_PASSWORD))){
			keyStorePassword = tech.getAttrMap().get(EndpointAttr.KEY_STORE_PASSWORD).toString();
		}
		
		if(tech.getAttrMap().get(EndpointAttr.KEY_STORE)!=null&&!"".equals(tech.getAttrMap().get(EndpointAttr.KEY_STORE))){
			keyStore = tech.getAttrMap().get(EndpointAttr.KEY_STORE).toString();
		}
		
		if(tech.getAttrMap().get(EndpointAttr.TRUST_STORE_PASSWORD)!=null&&!"".equals(tech.getAttrMap().get(EndpointAttr.TRUST_STORE_PASSWORD))){
			trustStorePassword = tech.getAttrMap().get(EndpointAttr.TRUST_STORE_PASSWORD).toString();
		}
		
		if(tech.getAttrMap().get(EndpointAttr.TRUST_STORE)!=null&&!"".equals(tech.getAttrMap().get(EndpointAttr.TRUST_STORE))){
			trustStore = tech.getAttrMap().get(EndpointAttr.TRUST_STORE).toString();
		}
		Protocol myhttps = new Protocol("https", new AuthSSLProtocolSocketFactory((keyStore==null?null:new URL(keyStore)),keyStorePassword,(trustStore==null?null:new URL(trustStore)),trustStorePassword), port);   
		Protocol.registerProtocol("https", myhttps);
		
		if(LOG.isDebugEnabled()) {
			LOG.debug("keyStore="+keyStore+" keyStorePassword="+keyStorePassword+" trustStore="+trustStore+" trustStorePassword="+trustStorePassword);
		}
	}

	private void addProxy(HttpClient httpClient, TechImpl tech) {
		String proxyIp = null;
		String proxyPort = null;
		
		if(tech.getAttrMap().get(EndpointAttr.PROXY_IP)!=null){
			proxyIp = tech.getAttrMap().get(EndpointAttr.PROXY_IP).toString();				
		}
		if(tech.getAttrMap().get(EndpointAttr.PROXY_PORT)!=null){
			proxyPort = tech.getAttrMap().get(EndpointAttr.PROXY_PORT).toString();
								
		}
		//代理
		if(!StringUtils.isEmpty(proxyIp) && !StringUtils.isEmpty(proxyPort)){
			if(LOG.isDebugEnabled()) {
				LOG.debug(" proxyIP:"+proxyIp+", proxyPort:"+proxyPort+" ");
			}
			httpClient.getHostConfiguration().setProxy(proxyIp, Integer.parseInt(proxyPort));  
			httpClient.getParams().setAuthenticationPreemptive(true); 
			
		}
	}
	
	private String getFileName(List<ExtAttr> moveFileInfos) {

		//指定要下载的文件名
		if(moveFileInfos != null && moveFileInfos.size() > 0){
			
			for (ExtAttr extAttr : moveFileInfos) {
				if(!StringUtils.isEmpty(extAttr.getFileName())){

					return extAttr.getFileName();
				}
			}
		}
		return "";
	}

	/**
	 * 重写上面的downloadFromUrl方法
	 * @param url
	 * @param downloadDir
	 * @param handler
	 * @param dirBean
	 * @return
	 */
	public boolean downloadFromUrl(String url,String downloadDir,PreDownLoadHandler handler,String fileFormat) throws EAAPException{
//		HttpClient client = new DefaultHttpClient();
//		url = handler.downloadRuleExecute();
//		httpDownload(client,url,downloadDir,fileFormat);
		return true;
	}	
	
	/**
	 * 重写上面的 httpDownload方法
	 * @param client
	 * @param url
	 * @param downloadDir
	 * @param dirBean
	 */
	private void httpDownload(HttpClient client,String url,String downloadDir,String fileFormat) throws EAAPException{
//		String fileName = "";
//		String srcFilePathName = "";
//		try {
//			// 获得HttpGet对象
//			HttpGet httpGet = getHttpGet(url, null, null);
//			// 发送请求获得返回结果
//			HttpResponse response = client.execute(httpGet);
//			// 如果成功
//			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				byte[] result = EntityUtils.toByteArray(response.getEntity());
//				BufferedOutputStream bw = null;
//				try {
//					Header[] attachmentheader = response.getHeaders("content-disposition");
//					if(attachmentheader!=null&&attachmentheader.length>0){
//						String attachment = attachmentheader[0].getValue();
//						String dowloadprefix = "filename=";
//						int flag = attachment.indexOf(dowloadprefix);
//						if(flag!=-1){
//							fileName = attachment.substring(flag+dowloadprefix.length(), attachment.length());
//						}else{
//							long temp = RandomUtils.nextLong();
//							//									String response.getLastHeader("Content-Type");
//							fileName = String.valueOf(temp);
//						}
//					}else{
//						int flag = url.lastIndexOf("/");
//						fileName = url.substring(flag+1,url.length());
//					}
//				
//					fileName = getFileName(fileName, fileFormat);	//对文件名称进行格式化
//					srcFilePathName = downloadDir+"/"+fileName;
//					FileOutputStream outputStream = new FileOutputStream(srcFilePathName+".ing");
//					bw = new BufferedOutputStream(outputStream);
//					bw.write(result);
////					FileUtils.moveFile(new File(srcFilePathName+".ing"), new File(srcFilePathName));
//				} catch (Exception e) {
//					throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN, "9213", "http download fail,the path "+url, e);
//				} finally {
//					try {
//						if (bw != null)
//							bw.close();
//						File tempFile = new File(srcFilePathName+".ing");
//						tempFile.renameTo(new File(srcFilePathName));
//					} catch (Exception e) {
//						throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN, "9213", "finally BufferedOutputStream shutdown close", e);
//					}
//				}
//			}
//			// 如果失败
//			else {
//				StringBuffer errorMsg = new StringBuffer();
//				errorMsg.append("httpStatus:");
//				errorMsg.append(response.getStatusLine().getStatusCode());
//				errorMsg.append(response.getStatusLine().getReasonPhrase());
//				errorMsg.append(", Header: ");
//				Header[] headers = response.getAllHeaders();
//				for (Header header : headers) {
//					errorMsg.append(header.getName());
//					errorMsg.append(":");
//					errorMsg.append(header.getValue());
//				}
//				 LOG.error("HttpResonse Error:{0}" , errorMsg);
//			}
//		} catch (Exception e) {
//			//LOG.error("HttpResonse Error:,path=" + url + ",url=" + url, e);
//			throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN, "9215", "HttpResonse Error:,path=" + url + ",url=" + url, e);
//		} finally {
//			try {
//				client.getConnectionManager().shutdown();
//			} catch (Exception e) {
//				throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN, "9215", "finally HttpClient shutdown error", e);
//			}
//		}
	}
	
	/**
	 * 格式化文件名称
	 * @param fileName
	 * @param dirBean
	 * @return
	 */
	private String getFileName(String fileName,String fileFormat) throws Exception{
		String filename = null;										//格式化后文件名称
		String name =null;
		String suffix = "";
		int flag = fileName.lastIndexOf(".");
		if(flag != -1) {
			filename = fileName.substring(0,flag);
			suffix = fileName.substring(flag+1,fileName.length());	//获取后缀名
		}
		
		if(suffix.length() == 0){
			filename = fileName;
		}
		try {
			if(fileFormat==null || "".equals(fileFormat)){
				name = filename+new SimpleDateFormat("_yyyyMMdd").format(new Date());
				if(suffix.length() > 0) name = name + "."+suffix;
			}else{
				name =fileExpr(filename, suffix, fileFormat);
			}
			return name.toString();
		} catch (Exception e) {
			//LOG.error("Expression does not regulate "+fileMoveSerInst.getFileFormats(), e);
			String errorInfo="Expression does not regulate "+fileFormat;
			throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN, "9211", errorInfo, e);
		}
	}
	
	/**
	 * 注册IKexpression中用于表示提取函数 
	 * @param date
	 * @return
	 */
	public  String getCurrTime(String date){
		SimpleDateFormat dateFormat = new SimpleDateFormat(date);
		date = dateFormat.format(new Date());
		return "_"+date;
	}
	
	/**
	 * 注册IKexpression中用于表示返回suffix 
	 * @param date
	 * @return
	 */
	public  String changeSuffix(String suffix){
		return "."+suffix;
	}
	
	/**
	 * IKexpression 解析表达式
	 * @param filename
	 * @param expr
	 * @return
	 */
	public String fileExpr(String filename,String suffix, String expression){
	    List<Variable> variables = new ArrayList<Variable>();  
	    variables.add(Variable.createVariable("filename", filename));
	    if(!expression.contains("changeSuffix") && suffix.length() > 0) {
	    	expression += "+$changeSuffix(suffix)";
	    	variables.add(Variable.createVariable("suffix", suffix));
	    }
	    //expression = "filename+$getCurrTime(\"yyyy-MM-dd\")+$changeSuffix(\"cvs\")";  
	    //执行表达式  
	    Object result = ExpressionEvaluator.evaluate(expression,variables);  
	    return result == null ? "" : result.toString();
	}
	public void upload(String directory, String uploadFile) throws IOException {
		// TODO Auto-generated method stub

	}

	public InputStream readRaw(String source) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean finalizeRaw() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	public T[] listFiles(String path, String filterExpression)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean downloadFromDir(String remoteDir, String localPath, String filterExpression
			,DownloadCallBack<F> callBack) throws EAAPException {
		// TODO Auto-generated method stub
		return false;
	}

	public String validateExpression(String filterExpression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void downloadFromDir(Map<String, String> fileMap,
			List<File> fileList, String fileSort, List<ExtAttr> moveFileInfos,
			Integer num, String remoteDir, String localPath,
			String fileFormats, DownloadCallBack<F> callBack,
			String isScanSubDirStr) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void downloadFromDir(Map<String, String> fileMap,
			List<File> fileList, String fileSort, List<ExtAttr> moveFileInfos,
			Integer num, String remoteDir, String localPath,
			String filterExpression, String fileFormats,
			DownloadCallBack<F> callBack, String isScanSubDirStr)
			throws IOException {
		// TODO Auto-generated method stub
		
	}



}
