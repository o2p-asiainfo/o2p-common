package com.ailk.eaap.o2p.common.spring.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.PropertyPlaceholderHelper;

/** 
 * @ClassName: CustomPropertyConfigurer 
 * @Description: 读取系统eaap-common.properties信息
 * @author zhengpeng
 * @date 2014-11-12 下午2:30:04 
 * @version V1.0  
 */
public class CustomPropertyConfigurer extends PropertyPlaceholderConfigurer{
	
	private static Map<String,String> properties = new HashMap<String,String>();  
    
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,
			Properties props) throws BeansException {
		
        PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(DEFAULT_PLACEHOLDER_PREFIX,DEFAULT_PLACEHOLDER_SUFFIX);  
        for(Entry<Object,Object> entry : props.entrySet()){  
            String stringKey = String.valueOf(entry.getKey());  
            String stringValue = String.valueOf(entry.getValue());  
            stringValue = helper.replacePlaceholders(stringValue, props);
            properties.put(stringKey, stringValue);  
        }  
        super.processProperties(beanFactoryToProcess, props);  
    }  
      
    public static Map<String, String> getProperties() {  
        return properties;  
    }  
      
    public static String getProperty(String key){  
        return properties.get(key);  
    }  

}
