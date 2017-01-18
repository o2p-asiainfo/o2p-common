package com.ailk.eaap.o2p.common.util.zookeeperUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.zookeeper.CreateMode;

import com.ailk.eaap.o2p.common.spring.config.ServerBreakDownWatcherCallback;
import com.ailk.eaap.o2p.common.spring.config.ZooKeeperFactory;
import com.asiainfo.foundation.log.Logger;

public class DistributedConsistencyHelper{
	public CuratorFramework client;
	private static final Logger log = Logger.getLog(DistributedConsistencyHelper.class);
	public static final String BASE_SERVER_PATH = "/server";
	public static final String DEFAULT_AGENT_NAME = "serviceAgent";
	public static final String DEFAULT_SPLIT = ":";
	private PathChildrenCache cache = null;
	
	public DistributedConsistencyHelper() {
		client = ZooKeeperFactory.get();
		try {
			client.getZookeeperClient().blockUntilConnectedOrTimedOut();
		} catch (InterruptedException e) {
			log.error("zookeeper client connect block failed", e);
		}
	}
	
	public DistributedConsistencyHelper(CuratorFramework client) {
		this.client = client;
		if(client.getState() != CuratorFrameworkState.STARTED) {
			client.start();
			try {
				client.getZookeeperClient().blockUntilConnectedOrTimedOut();
			} catch (InterruptedException e) {
				log.error("zookeeper client connect block failed", e);
			}
		}
	}
	
	public String registerServerToZK(String ip, String port, String serverNodeName) {
		UUID uuid = UUID.randomUUID();
		if(client == null) {
			throw new IllegalArgumentException("no connect to zookeeper!");
		}
		try {
			if(client.checkExists().forPath(BASE_SERVER_PATH+"/" + serverNodeName) == null) {
				client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(BASE_SERVER_PATH+"/" + serverNodeName);
			}
			String regHost = ip + DEFAULT_SPLIT + port + DEFAULT_SPLIT;
			String value = regHost + uuid.toString();
			client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(BASE_SERVER_PATH+"/" + serverNodeName+"/"+regHost, value.getBytes());
		} catch (Exception e) {
			log.error("register server to zookeeper error", e);
		}
		return uuid.toString();
	}
	
	public void createWatherToServers(String parentPath, final ServerBreakDownWatcherCallback callBack) throws Exception {
		cache = new PathChildrenCache(client, DistributedConsistencyHelper.BASE_SERVER_PATH + "/" + parentPath, true);
		cache.start(StartMode.POST_INITIALIZED_EVENT);
		cache.getListenable().addListener(new PathChildrenCacheListener() {
			
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
					throws Exception {
				switch(event.getType()) {
				case CHILD_REMOVED:
					callBack.callBack(new String(event.getData().getData()));
					break;
				case CHILD_ADDED:
					break;
				default:
					break;
				}
			}
		});
	}
	
	public void addLostListenerToZK(String ip, String port, String serverNodeName) {
		client.getConnectionStateListenable().addListener(new ServerConnectionStateListener(ip, port, serverNodeName));
	}
	
	public void closeClient() {
		if(client != null && client.getState() == CuratorFrameworkState.STARTED) {
			client.close();
		}
	}
	
	public CuratorFrameworkState getClientState() {
		if(client != null) {
			return client.getState();
		}
		return null;
	}
	
	class ServerConnectionStateListener implements ConnectionStateListener{
		private String ip;
		private String port;
		private String serverNodeName;
		
		public ServerConnectionStateListener(String ip, String port, String serverNodeName) {
			this.ip = ip;
			this.port = port;
			this.serverNodeName = serverNodeName;
		}

		@Override
		public void stateChanged(CuratorFramework client,
				ConnectionState newState) {
			if(ConnectionState.LOST == newState) {
				String value = ip + DEFAULT_SPLIT + port;
				try {
					if(client.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
						client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(BASE_SERVER_PATH+"/" + serverNodeName+"/"+serverNodeName, value.getBytes());
					}
				} catch (Exception e) {
					log.error("create server node in state listener error", e);
				}
			}
		}
		
	}

	public List<String> getAllServerIdentify(String defaultAgentName, boolean isGetSeralize) {
		if(client == null) {
			throw new IllegalArgumentException("no connect to zookeeper!");
		}
		try {
			List<String> children = client.getChildren().forPath(BASE_SERVER_PATH + "/" + defaultAgentName);
			List<String> identify = new ArrayList<String>();
			if(children != null) {
				String iden = null;
				String value = null;
				for(String name : children) {
					value = new String(client.getData().forPath(BASE_SERVER_PATH + "/" + defaultAgentName + "/" + name));
					if(isGetSeralize) {
						iden = value.split(DEFAULT_SPLIT)[2];
					} else {
						iden = value;
					}
					identify.add(iden);
				}
			}
			return identify;
		} catch (Exception e) {
			log.error("get all server identify error", e);
		}
		return null;
	}
}
