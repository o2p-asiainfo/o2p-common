package com.ailk.eaap.op2.common.test;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;


public class Test {

	public static void main(String[] args) throws Exception {
		
		String rsp = Test.test();
		System.out.println(rsp);
	
		/*ConcurrentHashMap attributes = new ConcurrentHashMap();
		attributes.put("a", null);*/

	}
	public static String readFile(String fileName){
		return null;
	}
	
	/*public static httTest(){
		String address = "https://isystem7.infonova.com/r6-api/rest/offerconfigurations?offerKey=DEMO_RT_INTERNET_OFFER";
		Map reqHead = null;
		String msg = null;
		String contentType = "text/xml;charset=UTF-8";
		String method = "";
		return HttpClientUtil.sendRequest(address, reqHead, msg, contentType, method, timeout, servicename, userName, password, proxyIP, proxyPort)
	}*/
	
	public static String test(){
		
		
		HttpMethod httpMethod = null;
		HttpClient httpClient = new HttpClient();
		String returnStr = "";
		try{
			
			
            
			/*if("POST".equalsIgnoreCase("POST")){				
				httpMethod = new PostMethod(address);
				((PostMethod)httpMethod).setRequestEntity(new StringRequestEntity(msg != null ? msg : "", "text/xml;charset=UTF-8", "utf-8"));
			}*/
			//String url = "https://isystem7.infonova.com/r6-api/rest/customers/6486/offers/availableoffers?channelId=243";
			String url = "https://isystem7.infonova.com/r6-api/rest/offerconfigurations?offerKey=RT_BUNDLE_TALKTIME_EVOLUTION";
			URL urla = new URL(url);
			System.out.println(urla.getHost());
			httpMethod = new GetMethod(url);



			httpClient.getParams().setAuthenticationPreemptive(true); 

			//CredentialsProvider credsProvider = new BasicCredentialsProvider();
			Credentials  usernamePassword = new UsernamePasswordCredentials(
                    "api_acc_ai@vso01", "api.cat!2013#8");
            //credsProvider.setCredentials(AuthScope.ANY, usernamePassword);
            //httpClient.setCredentialsProvider(credsProvider);
			httpClient.getState().setCredentials(

		            new AuthScope("isystem7.infonova.com", 443, "realm"),

		            usernamePassword

		        );
			/*if(reqHead != null) {
				List<Header>  headList = createReqHead(reqHead);
				headers.addAll(headList);
			}*/
		
			httpMethod.setDoAuthentication( true );
			httpMethod.addRequestHeader("Content-type", "text/xml;charset=UTF-8");
			try {          

	            // execute the GET

				httpClient.getParams().setConnectionManagerTimeout(80 * 1000);
				httpClient.getParams().setSoTimeout(80 * 1000);
				httpClient.setConnectionTimeout(80*1000);
				httpClient.setTimeout(80*1000);
				httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler(0, false));
	            int status = httpClient.executeMethod( httpMethod );

	 

	            // print the status and response

	            //System.out.println(status + "\n" + httpMethod.getResponseBodyAsString());
	            String str = httpMethod.getResponseBodyAsString();
	            Document doc = DocumentHelper.parseText(str);
	            System.out.println(doc.asXML());
	            List<Node> ns = doc.selectNodes("/availableOffers/availableOffer/offerKey");
	            System.out.println(ns.size());

	        } finally {

	            // release any connection resources used by the method

	        	httpMethod.releaseConnection();

	        }
				
			
		}catch(ConnectException e){
			e.printStackTrace();
		}
		catch(SocketTimeoutException e){
			e.printStackTrace();
		}		
		catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			if(httpMethod != null)
				httpMethod.releaseConnection();
			if (httpClient != null) {
				((SimpleHttpConnectionManager) (httpClient.getHttpConnectionManager())).shutdown();
				
			}
		}
		
		return returnStr;
	}
}
