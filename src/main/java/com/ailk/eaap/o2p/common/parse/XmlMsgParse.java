/** 
 * Project Name:serviceAgent-core 
 * File Name:XmlMsgParse.java 
 * Package Name:com.ailk.eaap.integration.o2p.parsing 
 * Date:2014年11月13日上午9:52:34 
 * Copyright (c) 2014, www.asiainfo.com All Rights Reserved. 
 * 
 */

package com.ailk.eaap.o2p.common.parse;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.util.Assert;

import com.ailk.eaap.op2.common.O2pDocumentHelper;

/**
 * ClassName:XmlMsgParse 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON. 
 * Date: 2014年11月13日 上午9:52:34 
 * 
 * @author zhongming
 * @version
 * @since JDK 1.6
 * 
 */
public class XmlMsgParse implements IMsgParse {

	@SuppressWarnings("unchecked")
	@Override
	public Object parsing(Object obj, String path) {
		Assert.notNull(obj, "The XML Object is null ");
		Assert.notNull(path, "The XML path is null ");

		Document doc = null;
		if(obj instanceof Document) {
			
			doc = (Document) obj;
		} else  {
			
			try {
				doc = O2pDocumentHelper.parseText(obj.toString());
			} catch (DocumentException e) {

				e.printStackTrace();
			}
		}
		List<Element> list = doc.selectNodes(path);
		if (list.size() == 0) {
			return null;
		}
		if (list.size() == 1) {
			Element e = list.get(0);
			return e.getText();
		}

		return list;
	}

	@Override
	public Object parsingValToObject(Object obj, String path) {
		Document doc = (Document) obj;
		String[] listPath = path.split("/");
		for (String str : listPath) {
			Element e = doc.getRootElement();
			doc = (Document) e.element(str);
		}
		return doc;
	}
	
}
