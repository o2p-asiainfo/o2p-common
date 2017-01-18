/** 
 * Project Name:gson-test 
 * File Name:StaxonUtils.java 
 * Package Name:com.mycompany.mavenproject1 
 * Date:2014年10月10日下午4:21:48 
 * Copyright (c) 2014, www.asiainfo.com All Rights Reserved. 
 * 
*/  
  
package com.ailk.eaap.o2p.common.util;  

import java.io.IOException;  
import java.io.StringReader;  
import java.io.StringWriter;  
  





import javax.xml.stream.XMLEventReader;  
import javax.xml.stream.XMLEventWriter;  
import javax.xml.stream.XMLInputFactory;  
import javax.xml.stream.XMLOutputFactory;  

import com.asiainfo.foundation.log.Logger;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;
import de.odysseus.staxon.json.JsonXMLConfig;  
import de.odysseus.staxon.json.JsonXMLConfigBuilder;  
import de.odysseus.staxon.json.JsonXMLInputFactory;  
import de.odysseus.staxon.json.JsonXMLOutputFactory;  
import de.odysseus.staxon.xml.util.PrettyXMLEventWriter;  
/** 
 * ClassName:StaxonUtils  
 * Function: xml json 互转工具.  
 * Reason:   TODO ADD REASON.  
 * Date:     2014年10月10日 下午4:21:48  
 * @author   颖勤 
 * @version   
 * @since    JDK 1.6 
 *        
 */
public class StaxonUtils {
	
	private static final Logger LOG = Logger.getLog(StaxonUtils.class);
	
	/** 
     * json string convert to xml string 
     */  
    public static String json2xml(String json){  
        StringReader input = new StringReader(json);  
        StringWriter output = new StringWriter();  
        JsonXMLConfig config = new JsonXMLConfigBuilder().multiplePI(false).repairingNamespaces(false).build();  
        try {  
            XMLEventReader reader = new JsonXMLInputFactory(config).createXMLEventReader(input);  
            XMLEventWriter writer = XMLOutputFactory.newInstance().createXMLEventWriter(output);  
            writer = new PrettyXMLEventWriter(writer);  
            writer.add(reader);  
            reader.close();  
            writer.close();  
        } catch( Exception e){  
            LOG.error("something bad happend",e);  
        } finally {  
            try {  
                output.close();  
                input.close();  
            } catch (IOException e) {  
                LOG.error("something bad happend",e);  
            }  
        }  
        if(output.toString().length()>=38){//remove <?xml version="1.0" encoding="UTF-8"?>  
            return output.toString().substring(39);  
        }  
        return output.toString();  
    }  
      
    /** 
     * xml string convert to json string 
     */  
    public static String xml2json(String xml){  
        StringReader input = new StringReader(xml);  
        StringWriter output = new StringWriter();  
        JsonXMLConfig config = new JsonXMLConfigBuilder().autoArray(true).autoPrimitive(true).prettyPrint(true).build();  
        try {  
            XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(input);  
            XMLEventWriter writer = new JsonXMLOutputFactory(config).createXMLEventWriter(output);  
            writer.add(reader);  
            reader.close();  
            writer.close();  
        } catch( Exception e){  
            LOG.error("something bad happend",e);  
        } finally {  
            try {  
                output.close();  
                input.close();  
            } catch (IOException e) {  
                LOG.error("something bad happend",e);  
            }  
        }  
        return output.toString();  
    }  
    
    /**
     * 
     * jsontoXml:(json转xml自带一个根元素).  
     * TODO(这里描述这个方法适用条件 – 可选). 
     * TODO(这里描述这个方法的执行流程 – 可选). 
     * TODO(这里描述这个方法的使用方法 – 可选). 
     * TODO(这里描述这个方法的注意事项 – 可选). 
     * 
     * @author wuwz 
     * @param json
     * @return 
     * @since JDK 1.6
     */
    public static String jsontoXml(String json) {
      
        XMLSerializer serializer = new XMLSerializer();

        serializer.setTypeHintsEnabled(false);
        serializer.setTypeHintsCompatibility(false);
        JSON jsonObject = JSONSerializer.toJSON(json);
        return serializer.write(jsonObject);
    }
}
