package com.ailk.eaap.op2.common;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.asiainfo.foundation.log.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultAttribute;




public class JsonUtil {

	private static Logger log = Logger.getLog(JsonUtil.class);

	
	public static String ObjToJsonStr(Object obj){
		
		
		if(obj.getClass().isArray() || obj instanceof Collection){
			JSONArray object = JSONArray.fromObject(obj);
			return object.toString();
		}else{
			JSONObject object = JSONObject.fromObject(obj);
			return object.toString();
		}
	}

	
	public static String json2xml(String jsonStr) {  
		JsonToXmlUtil xmlSerializer = new JsonToXmlUtil();  
	    JSON json = JSONObject.fromObject(jsonStr);
	    
	    String xml = xmlSerializer.write(json);
	    return xml;  
	}  


	public static boolean isJson(String jsonStr){
		if(jsonStr.startsWith("{")){
			return true;
		}else{
			return false;
		}
	}
	
	public static  String xml2JSON(String xml) {  
		JSONObject obj = new JSONObject();  
		try {  
			Document doc = DocumentHelper.parseText(xml);
			Element root = doc.getRootElement();  

			Map map = XmlUtil.Dom2Map(doc);  

			//obj.put(root.getName(), iterateElement(root));  
			obj.put(root.getName(), map);
			
			//obj.putAll(map);
			return obj.toString();  
		} catch (Exception e) {  
			log.error(e.toString());
			return null;  
		} 

		/*XMLSerializer xmlSerializer = new XMLSerializer();

		JSON json = xmlSerializer.read(xml);
		return json.toString();
		*/
		
	}
	
	
	
	public static void getElementList(Element element) { 
        List elements = element.elements(); 
        
        if (elements.size() == 0) { 

            
           
        } else { 
            //鏈夊瓙鍏冪礌 
            for (Iterator it = elements.iterator(); it.hasNext();) { 
                Element elem = (Element) it.next(); 
                //閫掑綊閬嶅巻 
                getElementList(elem); 
            } 
        } 
    } 

	
	private static Map  iterateElement(Element element) {  
        List jiedian = element.elements();
        
        Element et = null;
        Map obj = new HashMap();  
        List list = null;  
        for (int i = 0; i < jiedian.size(); i++) {  
            list = new LinkedList();  
            et = (Element) jiedian.get(i); 
            
            List ats = et.attributes();
            
            if(ats!=null && ats.size()>0){
            	Map atMap = new HashMap();
            	for (int j = 0; j < ats.size(); j++) {
            		DefaultAttribute e = (DefaultAttribute)ats.get(j);
            		 
            		 atMap.put(e.getName(), e.getText());
            		 
				}
            	obj.put(et.getName(), atMap);
            }
            if (et.getTextTrim().equals("")) {  
            	
                if (et.elements().size() == 0)  
                    continue;  
                if (obj.containsKey(et.getName())) {  
                    list = (List) obj.get(et.getName());  
                }
                list.add(iterateElement(et));  
                obj.put(et.getName(), list);  
                
            } else {  
                if (obj.containsKey(et.getName())) {  
                    list = (List) obj.get(et.getName());  
                }  
                list.add(et.getTextTrim());  
                obj.put(et.getName(), list);  
                
            }  
            
        }  
        return obj;  
    }
}
