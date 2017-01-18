package com.ailk.eaap.op2.common;

import com.ailk.eaap.o2p.common.spring.config.ZKCfgCacheHolder;

public class PropertyLoader {
  
    public static String getProperty(String property) {    
        String value = (String) ZKCfgCacheHolder.PROP_ITEMS.get(property);
        if(value != null) {
        	value = value.trim();
        }
        return value;
    }        

}
