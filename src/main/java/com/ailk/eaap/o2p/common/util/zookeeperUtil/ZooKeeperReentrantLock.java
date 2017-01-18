package com.ailk.eaap.o2p.common.util.zookeeperUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import com.ailk.eaap.o2p.common.spring.config.ZooKeeperFactory;
import com.asiainfo.foundation.log.Logger;

public class ZooKeeperReentrantLock{
	public static final String LOCK_BASE_PATH = "/_lock_";
	public static final long DEFAULT_LOCK_TIME = 1;
	public static final TimeUnit DEFAULT_LOCK_TIME_UNIT = TimeUnit.MINUTES;
	InterProcessLock lock = null;
	CuratorFramework client = null;
	private static final Logger log = Logger.getLog(ZooKeeperReentrantLock.class);
	
	protected ZooKeeperReentrantLock(){}
	
	public ZooKeeperReentrantLock(String lockPointName) {
		this.client = initClient();
		this.lock = new InterProcessMutex(this.client, LOCK_BASE_PATH+"/" + lockPointName);
	}

	public ZooKeeperReentrantLock(CuratorFramework client, String lockPointName) {
		this.client = client;
		this.lock = new InterProcessMutex(this.client, LOCK_BASE_PATH+"/" + lockPointName);
	}
	
	public ZooKeeperReentrantLock(List<String> lockPointNameList) {
		this.client = initClient();
		lock = new InterProcessMultiLock(client, lockPointNameList);
	}
	
	public ZooKeeperReentrantLock(CuratorFramework client, List<String> lockPointNameList) {
		this.client = client;
		lock = new InterProcessMultiLock(client, lockPointNameList);
	}
	
	CuratorFramework initClient() {
		client = ZooKeeperFactory.get();
		if(client != null) {
			try {
				client.getZookeeperClient().blockUntilConnectedOrTimedOut();
			} catch (InterruptedException e) {
				log.error("zookeeper client connect block failed", e);
			}
		}
		return client;
	}

	public boolean lock() throws Exception {
		return lock.acquire(DEFAULT_LOCK_TIME, DEFAULT_LOCK_TIME_UNIT);
	}
	
	public boolean lock(long time, TimeUnit timeUnit) {
		if(lock == null) {
			log.error("not find lock, do you use Read Lock like this: ZooKeeperLockFactory.getReadWriteLock().readLock().lock()");
			return true;
		}
		try {
			return lock.acquire(time, timeUnit);
		} catch (Exception e) {
			log.error("zookeeper lock error", e);
			return true;
		}
	}
	
	public boolean lockCode(long time, TimeUnit timeUnit, ZooKeeperCodeCallBack callBack) {
		try {
			if(this.lock(time, timeUnit)) {
				return callBack.callBack();
			}
		} finally {
			try {
				this.release();
			} catch (Exception e) {
				log.error("zookeeper release lock error", e);
			}
		}
		return false;
	}
	
	public void release() throws Exception {
		if(lock != null && lock.isAcquiredInThisProcess()) {
			lock.release();
		} else {
			log.debug("lock is null or not acquire the lock, no be released");
		}
	}
	
	public boolean isLock() {
		if(lock != null) {
			return lock.isAcquiredInThisProcess();
		} 
		return false;
	}
}
