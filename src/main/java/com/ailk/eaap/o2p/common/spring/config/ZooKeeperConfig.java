package com.ailk.eaap.o2p.common.spring.config;

import java.util.List;
import java.util.Properties;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;

import com.ailk.eaap.o2p.common.util.PropertiesParseUtil;
import com.asiainfo.foundation.log.Logger;

/**
 * 
 * @author 颖勤
 *
 */
public class ZooKeeperConfig implements Config {
	private final static Logger LOG = Logger.getLog(ZooKeeperConfig.class);
	@Override
    public byte[] getConfig(String path) throws Exception {
        CuratorFramework client = ZooKeeperFactory.get();
        if (!exists(client, path)) {
            throw new RuntimeException("Path " + path + " does not exists.");
        }
        return client.getData().forPath(path);
    }
	@Override
	public Properties getConfigItems(String path,boolean isIgnoreUnresolvableItem) throws Exception{
		CuratorFramework client = ZooKeeperFactory.get();
		List<String> children = client.getChildren().forPath(path);
		Properties props = new Properties();
		for(String s:children){
			String childPath = path + "/" + s;
			if(exists(client, childPath)){
				byte[] val = client.getData().forPath(childPath);
				String strVal = new String(val,"UTF-8");
				//LOG.info("get properties key {0} value json {1}", childPath, strVal);
				String value = PropertiesParseUtil.getPropertiesValue(strVal);
				if(value == null) {
					LOG.warn("path {0} value is null", childPath);
					value = "";
				}
				props.put(s, value);
			}else if(isIgnoreUnresolvableItem){
				LOG.warn("the path {0} cannot be found in znode", childPath);
			}else{
				throw new RuntimeException("Path " + childPath + " does not exists from zookeeper znodes.");
			}

		}
		return props;
	}
     
    private boolean exists(CuratorFramework client, String path) throws Exception {
        Stat stat = client.checkExists().forPath(path);
        return !(stat == null);
    }

}
