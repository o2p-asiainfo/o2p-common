package com.ailk.eaap.op2.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.asiainfo.foundation.log.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultAttribute;
import org.dom4j.tree.DefaultElement;
import org.dom4j.tree.DefaultText;

/**
 * 
 * 锟斤拷锟斤拷锟�br>
 * 锟斤拷 XML锟叫碉拷锟斤拷莞锟斤拷XPATH锟揭筹拷锟斤拷锟斤拷锟斤拷锟揭癸拷锟剿碉拷锟斤拷
 * <p>
 * @version 1.0
 * @author litieyang Apr 16, 2013
 * <hr>
 * 锟睫改硷拷录
 * <hr>
 * 1锟斤拷锟睫革拷锟斤拷员:    锟睫革拷时锟斤拷:<br>       
 *    锟睫革拷锟斤拷锟斤拷:
 * <hr>
 */
public class XmlUtil {
	static Logger LOG = Logger.getLog(XmlUtil.class);
	/**
	 * 锟斤拷锟斤拷锟斤拷锟�
	 * @param str
	 * @return Map 锟斤拷锟斤拷锟斤拷锟�
	 */

	private Document document;

	/**
	 * 锟斤拷锟斤拷XML
	 * @param xmlStr  锟街凤拷
	 * @throws DocumentException  锟届常
	 */
	public static Document convToxml(String xmlStr){
		Document doc;
		try {
			doc = DocumentHelper.parseText(xmlStr);
		} catch (DocumentException e) {
			return null;
		}
		return doc;
	}
	
	public static String convToStr(Document doc){
		String result = doc.asXML();
		return result;
	}
	
	/**
	 * 锟斤拷取某锟斤拷锟节碉拷
	 */
	public static Element getElement(Document doc,String node){
		Element root = doc.getRootElement();
		Element ele = (Element)root.selectSingleNode(node);
		return ele;
	}
	
	/**
	 * 锟斤拷取某锟节碉拷锟侥筹拷锟斤拷锟斤拷锟�
	 */
	public static Attribute getAttribute(Document doc,String node,String attribute){
		Element root = doc.getRootElement();
		Element ele = (Element)root.selectSingleNode(node);
		return ele.attribute(attribute);
	}
	
	/**
	 * 锟斤拷取某锟节碉拷锟侥筹拷锟斤拷锟街�
	 */
	public static String getAttributeValue(Document doc,String node,String attribute){
		Element root = doc.getRootElement();
		Element ele = (Element)root.selectSingleNode(node);
		String value = ele.attributeValue(attribute);
		return value;
	}
	
	/**
	 * 锟斤拷锟节诧拷锟斤拷,锟接憋拷锟斤拷锟侥硷拷锟叫讹拷取
	 * @throws DocumentException
	 * @throws FileNotFoundException 
	 */
	private String loadXML(String xmlfile) throws DocumentException, FileNotFoundException{
		SAXReader saxReader = new SAXReader();
		FileInputStream in = new FileInputStream(xmlfile);
		document = saxReader.read(in);
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error("occur exception",e);
		}
		return document.asXML();
	}

	/**
	 * 锟斤拷锟�Element锟斤拷Xpath锟矫碉拷 锟斤拷锟叫碉拷 key 锟斤拷 value
	 * @param document    xml锟侥碉拷
	 * @param xpath       锟斤拷锟斤拷锟斤拷锟斤拷
	 * @return			  一锟斤拷PO锟侥讹拷锟斤拷值系锟叫的硷拷锟斤拷
	 * @throws DocumentException 锟侥碉拷锟斤拷锟斤拷失锟斤拷锟届常
	 */
	private Map getXML(Document document, String xpath) throws DocumentException{
		Map map = new HashMap();
		List list = document.selectNodes(xpath);
		Iterator iter = list.iterator();
		while(iter.hasNext()){
			Element elem = (Element) iter.next();
			if(!elem.getText().trim().equals(""))
//				System.out.println(elem.getPath()+"/"+elem.getName()+" "+elem.getText());
			getChildElement(elem);
		}
		return map;
	}
	
	
	/**
	 * 锟斤拷锟斤拷锟斤拷Element锟侥递癸拷
	 * @param elem elem
	 * @throws DocumentException  锟届常
	 */
	private void getChildElement(Element elem) throws DocumentException{
		Iterator it = elem.elements().iterator();
		while(it.hasNext()){
			Element el = (Element) it.next();
			if(!el.getText().trim().equals(""))
//				System.out.println(elem.getPath()+"/"+elem.getName()+ "/"+ el.getName()+" "+el.getText());
			getChildElement(el);
		}
	}
	
	/**
	 * 锟斤拷锟絏path锟矫碉拷锟矫诧拷锟斤拷锟斤拷锟斤拷值. 
	 * <b>注锟解：<b/><br>
	 * <i>1. 直锟斤拷通锟斤拷xpath 锟斤拷锟斤拷锟斤拷锟角凤拷玫锟斤拷锟斤拷锟斤拷锟斤拷锟截癸拷锟斤拷true or false  直锟斤拷 rule = 锟斤拷锟斤拷锟斤拷锟�
	 * <i>2. 锟斤拷锟斤拷通锟斤拷xpath 锟斤拷锟矫碉拷锟斤拷锟斤拷取值锟斤拷锟斤拷锟矫碉拷指锟斤拷锟斤拷锟斤拷锟侥革拷值锟斤拷     锟斤拷锟斤拷锟叫讹拷 rule = 锟斤拷锟斤拷.equals("锟斤拷锟斤拷")锟斤拷
	 * @param xmlstr  xml 锟街凤拷锟斤拷锟斤拷
	 * @param xpath   xpath
	 * @return value  通锟斤拷xpath锟矫碉拷锟斤拷xml锟侥憋拷锟叫的凤拷锟截斤拷锟�
	 * @throws RuleEingineException 锟斤拷锟斤拷锟届常
	 */
	public Object getValue(String xmlstr, String xpath) throws DocumentException{
		String returnvalue = null;
		convToxml(xmlstr);
		Object node = document.selectObject(xpath);
		if(node==null|| node.toString().equals("[]")){
			//锟斤拷锟絰path锟睫凤拷取锟斤拷值锟斤拷锟津返伙拷false
			return null;
		} else {
		  if (node instanceof List){
		  		returnvalue = node.toString();
		  } else if(node instanceof DefaultText){
				returnvalue = ((DefaultText)node).getText();
		  } else if(node instanceof DefaultElement){
		  		returnvalue = ((DefaultElement)node).toString();
		  } else if(node instanceof String){
		  		returnvalue = node.toString();
		  }
		  return returnvalue;
		}
	}
	
	/**
	 * 锟斤拷取doc锟叫的节碉拷(锟节点不锟斤拷执锟叫⌒�
	 */
	public static Element trace(Element root, String str) {
		Element result = null;
		if (root.getName().equalsIgnoreCase(str)) {
			result = root;
		} else {
			List l = root.elements();
			for (int i = 0; i < l.size(); i++) {
				Element e = (Element) l.get(i);
				result = trace(e, str);
				if (result != null)
					break;
			}

		}
		return result;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s= "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">  <soap:Body>   <qryCustInfoResponse xmlns=\"http://common.intf.bss\"> <out> xxx</out>   </qryCustInfoResponse>    </soap:Body> </soap:Envelope> ";           
		String ss = "<cust><custId>aabc</custId><custName>davidZhuang</custName></cust>";
		
		Document doc = XmlUtil.convToxml(s);
		String tmp = changeNodePath(doc,"/soap:Envelope/soap:Body");
		Element e = (Element) doc.selectSingleNode(tmp);
//		Element e = XmlUtil.getElement(doc, "/cust/custName");
		if(LOG.isDebugEnabled()){
			if(e!=null)
			LOG.debug("nodeValue:" + e.getText());
		}
	}
	
	
	
	
    public static Map<String, Object> Dom2Map(Document doc){  
        Map<String, Object> map = new HashMap<String, Object>();  
        if(doc == null)  
            return map;  
        Element root = doc.getRootElement();  
        for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {  
            Element e = (Element) iterator.next();  
            //System.out.println(e.getName());  
            List list = e.elements();  
            if(list.size() > 0){  
                map.put(e.getName(), Dom2Map(e));  
            }else  
                map.put(e.getName(), e.getText());  
        }  
        return map;  
    }  
      
  
    public static Map Dom2Map(Element e){  
        Map map = new HashMap();  
        List list = e.elements();  
        if(list.size() > 0){  
            for (int i = 0;i < list.size(); i++) {  
                Element iter = (Element) list.get(i);  
                List mapList = new ArrayList();  
                List<DefaultAttribute> ats = iter.attributes();
                
                if(iter.elements().size() > 0){
                	
                    Map m = Dom2Map(iter);  
                    if(ats!=null && ats.size()>0){
                		
                    	for (DefaultAttribute at : ats) {
                    		m.put("@"+at.getName(), at.getText());
    					}
                    	
                    }
                    
                    if(map.get(iter.getName()) != null){ 
                    	
                        Object obj = map.get(iter.getName());  
                        if(!obj.getClass().getName().equals("java.util.ArrayList")){  
                            mapList = new ArrayList();  
                            mapList.add(obj);  
                            mapList.add(m);
                            
                        }  
                        if(obj.getClass().getName().equals("java.util.ArrayList")){  
                            mapList = (List) obj;  
                            mapList.add(m);  
                        }  
                        map.put(iter.getName(), mapList);  
                    }else{
                    	
                    	map.put(iter.getName(), m);
                    	
                    	
                    }
                          
                }  
                else{  
                	
                	
                	
                    if(map.get(iter.getName()) != null){ 
                    	
                        Object obj = map.get(iter.getName());  
                        if(!obj.getClass().getName().equals("java.util.ArrayList")){  
                            mapList = new ArrayList();  
                            mapList.add(obj);  
                            mapList.add(iter.getText());  
                        }
                        if(obj.getClass().getName().equals("java.util.ArrayList")){  
                            mapList = (List) obj;  
                            mapList.add(iter.getText());  
                        }  
                        map.put(iter.getName(), mapList);
                        if(ats!=null && ats.size()>0){
                    		Map m = new HashMap();
                        	for (DefaultAttribute at : ats) {
                        		m.put("@"+at.getName(), at.getText());
        					}
                        	map.put(iter.getName(), m);
                        }
                    }else{
                    	map.put(iter.getName(), iter.getText()); 
                    	if(ats!=null && ats.size()>0){
                    		Map m = new HashMap();
                        	for (DefaultAttribute at : ats) {
                        		m.put("@"+at.getName(), at.getText());
        					}
                        	m.put("#text", iter.getText());
                        	map.put(iter.getName(), m);
                        }
                    }
                         
                }  
            }  
        }else{
        	map.put(e.getName(), e.getText());  
        }
            
        return map;  
    }  

    public static String changeNodePath(Document xmldoc, String nodePath) {
		
		Element root = xmldoc.getRootElement();
		String pre = root.getNamespacePrefix();
		if(nodePath.contains(pre) ||nodePath.contains("local-name()")) {
			return nodePath;
		}
		String[] xpaths = nodePath.split("/");
		if(xpaths.length<=1) {
			return nodePath;
		}
		xpathAddPrefix(root, xpaths, 1);
		String newXpath = "";
		for(String xpathStr : xpaths) {
			if(!xpathStr.equals(""))
				newXpath +="/"+xpathStr;
		}
		
		return newXpath;
	}

	public static void xpathAddPrefix(Element e, String[] xpaths, int i) {
		String prefix = e.getNamespacePrefix();
		if(prefix != null && !prefix.equals("")) {
			xpaths[i] = prefix + ":" + xpaths[i];
		}
		if(i+1==xpaths.length) {
			return;
		}
		Element elementChild = e.element(xpaths[i+1]);
		if(elementChild != null) {
			xpathAddPrefix(elementChild, xpaths, i+1);
		}
		
	}
}
