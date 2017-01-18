package com.ailk.eaap.o2p.common.spring.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.ailk.eaap.o2p.common.security.SecurityUtil;

public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	private static final Log log = LogFactory.getLog(EncryptPropertyPlaceholderConfigurer.class);
	private static Map<String, String> ctxPropertiesMap = new HashMap<String, String>();
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactory, Properties props)
			throws BeansException {
		Map<String,String> map = new HashMap<String,String>();
		try {
			if(null != props && props.size() > 0){
				for(Map.Entry<Object, Object>  entry : props.entrySet()){
					String key = entry.getKey().toString();
					String value = entry.getValue().toString();
					if(key.contains("SEC.KEY.")){
						String changeKey = key.replace("SEC.KEY.", "");
						String proValue = SecurityUtil.getInstance().decryMsg(value);
						map.put(changeKey, proValue);//解密后重新设置props对象
					}
					ctxPropertiesMap.put(key, value);
				}
			}
			if(map.size() > 0){
				props.putAll(map);
			}
			super.processProperties(beanFactory, props);   //调用父方法  
		} catch (Exception e) {
			log.error(e.getStackTrace());
            throw new BeanInitializationException(e.getMessage()); 
		}
	}

	public static String getContextProperty(String name) {  
        return ctxPropertiesMap.get(name);  
    }
}
	