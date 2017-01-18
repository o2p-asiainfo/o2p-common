package com.ailk.eaap.op2.common.zookeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

import com.ailk.eaap.o2p.common.util.zookeeperUtil.DistributedConsistencyHelper;
import com.ailk.eaap.o2p.common.util.zookeeperUtil.ZooKeeperReadWriteLock;
import com.ailk.eaap.o2p.common.util.zookeeperUtil.ZooKeeperReentrantLock;

public class TestZookeeperLock {
	@Test
	public void test1() throws InterruptedException {
		// TODO Auto-generated method stub

		CountDownLatch latch = new CountDownLatch(5);

		String zookeeperConnectionString = "localhost:2181";
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient(
				zookeeperConnectionString, retryPolicy);
		client.start();
		client.getZookeeperClient().blockUntilConnectedOrTimedOut();
		System.out.println("客户端启动。。。。");

		long start = System.currentTimeMillis();
		List<Thread> t = new ArrayList<Thread>();
		for (int i = 0; i < 5; i++) {
			MyRWLock ml = null;
			if(new java.util.Random().nextInt(2) == 0)
				ml = new MyRWLock("client" + i, client, latch, "r");
			else 
				ml = new MyRWLock("client" + i, client, latch, "w");
			Thread th = new Thread(ml);
			t.add(th);
			th.start();
		}

		for(Thread m:t) {
			m.join();
		}
		long end = System.currentTimeMillis();
		System.out.println("所有任务执行完毕, 话费时间:" + (end-start));

		client.close();

		System.out.println("客户端关闭。。。。");

	}
	
	@Test
	public void test22() throws Exception {
		ZooKeeperReentrantLock lock = new ZooKeeperReentrantLock(String.valueOf(649));
		try {
			if(lock.lock()) {
				System.out.println(11111111);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			lock.release();
		}
	}
	
	@Test
	public void test11() {
		DistributedConsistencyHelper dh = new DistributedConsistencyHelper();
		List<String> identifyList = dh.getAllServerIdentify(dh.DEFAULT_AGENT_NAME,true);
		for(String identify: identifyList) {
			System.out.println(identify);
		}
	}
	
	@Test
	public void test2() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(5);

		String zookeeperConnectionString = "localhost:2181";
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient(
				zookeeperConnectionString, retryPolicy);
		client.start();
		System.out.println("客户端启动。。。。");
		ExecutorService exec = Executors.newCachedThreadPool();

		for (int i = 0; i < 5; i++) {
			exec.submit(new MyLock("client" + i, client, latch));
		}

		exec.shutdown();
		latch.await();
		System.out.println("所有任务执行完毕");

		client.close();

		System.out.println("客户端关闭。。。。");
	}
	
	@Test
	public void test3() throws Exception {
		CountDownLatch latch = new CountDownLatch(5);

		String zookeeperConnectionString = "localhost:2181";
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(100, 30);
		CuratorFramework client = CuratorFrameworkFactory.newClient(
				zookeeperConnectionString, retryPolicy);
		client.start();
		System.out.println(client.getZookeeperClient().blockUntilConnectedOrTimedOut());
		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/root/server/binging");
		Thread.sleep(10*1000);
		client.close();
	}
}

class MyRWLock implements Runnable {

	private String name;
	
	private String lockType;

	private CuratorFramework client;

	private CountDownLatch latch;

	public MyRWLock(String name, CuratorFramework client, CountDownLatch latch, String lockType) {
		this.name = name;
		this.client = client;
		this.latch = latch;
		this.lockType = lockType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void run() {
//		InterProcessMutex lock = new InterProcessMutex(client, "/test_group");
		ZooKeeperReadWriteLock lock = new ZooKeeperReadWriteLock(client, "test_group");
		try {
			if(lockType.equals("r")) {
				lock = lock.readLock();
				System.out.println("is read lock");
			}
			else if(lockType.equals("w")) {
				lock = lock.writeLock();
				System.out.println("is write lock");
			}
			if (lock.lock()) {
				try {
					// do some work inside of the critical section here
					System.out.println("----------" + this.name
							+ "获得资源----------");
					System.out.println("----------" + this.name
							+ "正在处理资源----------");
					Thread.sleep(2000);
//					lock.readLock().lock();
//					System.out.println("----------" + this.name
//							+ "降级为读锁----------");
//					Thread.sleep(5000);
					System.out.println("----------" + this.name
							+ "资源使用完毕----------");
					latch.countDown();
				} finally {
					if(lockType.equals("r")) {
						lock.releaseReadLock();
					} else {
						lock.releaseWriteLock();
					}
					System.out.println("----------" + this.name
							+ "释放----------");
				}
			} else {
				System.out.println(Thread.currentThread().getName() + "can't get the lock");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("thread over....");

	}

}

class MyLock implements Runnable {

	private String name;
	
	private CuratorFramework client;

	private CountDownLatch latch;

	public MyLock(String name, CuratorFramework client, CountDownLatch latch) {
		this.name = name;
		this.client = client;
		this.latch = latch;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void run() {
//		InterProcessMutex lock = new InterProcessMutex(client, "/test_group");
		ZooKeeperReentrantLock lock = new ZooKeeperReentrantLock(client, "test_group");
		try {
			if (lock.lock()) {
				try {
					// do some work inside of the critical section here
					System.out.println("----------" + this.name
							+ "获得资源----------");
					System.out.println("----------" + this.name
							+ "正在处理资源----------");
					Thread.sleep(2000);
//					lock.readLock().lock();
//					System.out.println("----------" + this.name
//							+ "降级为读锁----------");
//					Thread.sleep(5000);
					System.out.println("----------" + this.name
							+ "资源使用完毕----------");
					latch.countDown();
				} finally {
					lock.release();
					System.out.println("----------" + this.name
							+ "释放----------");
				}
			} else {
				System.out.println(Thread.currentThread().getName() + "can't get the lock");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("thread over....");

	}
	
}
