package com.ailk.eaap.op2.common.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.ailk.eaap.o2p.common.spring.config.ZooKeeperFactory;

public class ClientFactory {

	public static CuratorFramework newClient() {
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("ppd02:2181")  
		        .sessionTimeoutMs(30000)  
		        .connectionTimeoutMs(30000)  
		        .canBeReadOnly(false)  
		        .retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))  
		        .namespace("cfg")  
		        .defaultData(null)  
		        .build();  
		return client;
	}
	public static void main(String[] args) throws Exception{
		CuratorFramework zkClient = ZooKeeperFactory.get();
		zkClient.create().forPath("/properties/zkConnection", "ppd01:2181".getBytes());
		zkClient.create().forPath("/properties/brokerList", "ppd01:9092".getBytes());
		zkClient.create().forPath("/properties/groupId", "g1".getBytes());
//		zkClient.setData().forPath("/properties/zkConnection", "ppd021111:2181".getBytes());
		System.out.println("success");
	}

}
