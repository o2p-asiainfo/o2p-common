package com.ailk.eaap.op2.common.zookeeper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import com.google.common.collect.Lists;

public class LeaderSelectorExample {
	private static final int CLIENT_QTY = 10;
	private static final String PATH = "/examples/leader";

	public static void main(String[] args) throws Exception {

		List<TestSelector> clients = Lists.newArrayList();
		try {
			for (int i = 0; i < CLIENT_QTY; ++i) {
				CuratorFramework client = ClientFactory.newClient();
				LeaderLatch example = new LeaderLatch(client, PATH, "Client #"
						+ i);
				TestSelector ts = new TestSelector(example, client);
				Thread t = new Thread(ts);
				t.start();
				clients.add(ts);
			}
			Thread.sleep(2000);
			new BufferedReader(new InputStreamReader(System.in)).readLine();
			System.out.println("leader = " + clients.get(0).getLeader());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Shutting down...");
			for (TestSelector exampleClient : clients) {
				exampleClient.close();
			}
		}
	}
	
	static class TestSelector implements Runnable {
		private LeaderLatch example;
		private CuratorFramework client;
		
		public TestSelector(LeaderLatch example, CuratorFramework client) {
			this.example = example;
			this.client = client;
		}

		@Override
		public void run() {
			try {
				client.start();
				example.start();
				Thread.sleep(1000);
				if(isLeader()) {
					System.out.println(example.getId() + " becoming a leader");
				}
				Thread.sleep(3000);
				if(isLeader()) {
					System.out.println(example.getId() + " becoming a leader");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public boolean isLeader() {
			return this.example.hasLeadership();
		}
		
		public void close() {
			CloseableUtils.closeQuietly(example);
			CloseableUtils.closeQuietly(client);
		}
		
		public String getId() {
			return example.getId();
		}
		
		public String getLeader() throws Exception {
			return example.getLeader().getId();
		}
	}
}
