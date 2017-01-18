package com.ailk.eaap.op2.common.zookeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.Assert;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.ailk.eaap.o2p.common.spring.config.ICfgCacheHolder;
import com.ailk.eaap.o2p.common.spring.config.ServerBreakDownWatcherCallback;
import com.ailk.eaap.o2p.common.spring.config.ZooKeeperFactory;
import com.ailk.eaap.o2p.common.util.zookeeperUtil.DistributedConsistencyHelper;
import com.ailk.eaap.op2.common.test.TestBase;


/*@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"classpath:o2p-test-spring.xml"})  */
public class DistributedConsistencyHelperTest extends TestBase{

	@Test
	public void testZKWatch() throws Exception {
		DistributedConsistencyHelper dch = new DistributedConsistencyHelper();
		dch.registerServerToZK("10.226.4.71", "18080", DistributedConsistencyHelper.DEFAULT_AGENT_NAME);
		CuratorFramework client = createClient();
		DistributedConsistencyHelper dch1 = new DistributedConsistencyHelper(client);
		final List<String> li = new ArrayList<String>();
		dch1.createWatherToServers(DistributedConsistencyHelper.DEFAULT_AGENT_NAME,  new ServerBreakDownWatcherCallback() {
			@Override
			public void callBack(String serverIdentifying) {
				li.add(serverIdentifying);
			}
		});
		Thread.sleep(2000);
		dch.closeClient();
		Thread.sleep(100000);
		Assert.assertNotSame(0, li.size());
	}
	
	private CuratorFramework createClient() throws IOException, InterruptedException {
		Properties evnCfgItem = null;
		String CONNECT_STRING = null;
		CuratorFramework client = null;
		evnCfgItem = PropertiesLoaderUtils.loadProperties(new ClassPathResource(ICfgCacheHolder.ENV_CFG_PROP_RESOURCE));
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(ZooKeeperFactory.BASE_SLEEP_TIMEMS, ZooKeeperFactory.MAX_RETRIES);
		if(evnCfgItem!=null&&evnCfgItem.containsKey("CONNECT_STRING")){
				CONNECT_STRING = evnCfgItem.getProperty("CONNECT_STRING");
		}
		client = CuratorFrameworkFactory.builder()
				.connectString(CONNECT_STRING)
				.retryPolicy(retryPolicy)
				.namespace("cfg")
				.build();
		client.start();	
		client.getZookeeperClient().blockUntilConnectedOrTimedOut();
		return client;
	}
}
