package com.ailk.eaap.op2.common.test;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class InfonoTest {

	public static void main(String[] args) throws Exception {
		/*String xml = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
"<ContractRoot>"+
"	<TcpCont>"+
"		<TransactionID>1000010005201305220000028985</TransactionID>"+
"		<ActionCode>0</ActionCode>"+
"		<BusCode>BUS37016</BusCode>"+
"		<ServiceCode>SVC37074</ServiceCode>"+
"		<ServiceContractVer>SVC3707420130901</ServiceContractVer>"+
"		<ServiceLevel>1</ServiceLevel>"+
"		<SrcOrgID>100001</SrcOrgID>"+
"		<SrcSysID>1000010005</SrcSysID>"+
"		<SrcSysSign>123</SrcSysSign>"+
"		<DstOrgID>100000</DstOrgID>"+
"		<DstSysID>1000000038</DstSysID>"+
"		<ReqTime>20130522145140</ReqTime>"+
"	</TcpCont>"+
"	<SvcCont>"+
"        <PROD_OFFER_ID>" +
"<SIPSS>"+

"<SIPSSData>2F5149F1C58921C9C2E990B442ADC380</SIPSSData>"+

"</SIPSS>"+
"<SIPSS>"+

"<SIPSSData>2F5149F1C58921C9C2E990B442ADC380</SIPSSData>"+

"</SIPSS>"+
"</PROD_OFFER_ID>"+
"   </SvcCont>"+
"</ContractRoot>";*/
		
		String xml = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:impl=\"http://impl.service.out.upc.ailk.com\">"+
   "<soapenv:Header/>"+
   "<soapenv:Body>"+
      "<impl:importObjFromXml soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"+
       "  <arg0 xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">102</arg0>"+
       "  <arg1 xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">1</arg1>"+
       "  <arg2 xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">1</arg2>"+
       "  <arg3 xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\"></arg3>"+
       "  <arg4 xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">1</arg4>"+
       "  <arg5 xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">01</arg5>"+
      "</impl:importObjFromXml>"+
   "</soapenv:Body>"+
"</soapenv:Envelope>";
		
		
		Document srcdoc = DocumentHelper.parseText(xml);
	/*	Element prodOfferId = (Element)srcdoc.selectSingleNode("/ContractRoot/TcpCont");
		prodOfferId.setName("CC");
		System.out.println(prodOfferId.asXML());
	*/
		
		
		
		Element  nodes = (Element)srcdoc.selectSingleNode("/soapenv:Envelope/soapenv:Body/impl:importObjFromXml/arg3");
		if(nodes!=null){
			nodes.setText("aa");
			System.out.println(nodes.asXML());
		}
		
		/*String temp = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
			"<offerConfiguration xmlns=\"http://www.infonova.com/product/model\">"+
			
			"<baseOffers>"+
			"</baseOffers>"+
			
			"<featureGroups>"+
			"</featureGroups>"+
			
			"<tariffModels>"+
			"</tariffModels>"+
			
			"<chargeClusters>"+
			"</chargeClusters>"+
			
			
			"<billTypeGroups>"+
			"</billTypeGroups>"+
			
			"<billTypes>"+
			"</billTypes>"+
			
			"</offerConfiguration>";
		Document dstdoc = DocumentHelper.parseText(temp);*/
		
		
	}
}
