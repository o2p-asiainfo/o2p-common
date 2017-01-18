package com.ailk.eaap.o2p.common.util.zookeeperUtil;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

public class ZooKeeperReadWriteLock extends ZooKeeperReentrantLock{
	private InterProcessReadWriteLock rwLock = null;

	public ZooKeeperReadWriteLock(String lockPointName) {
		this.client = initClient();
		this.rwLock = new InterProcessReadWriteLock(client, LOCK_BASE_PATH);
	}
	
	public ZooKeeperReadWriteLock(CuratorFramework client, String lockPointName) {
		this.client = client;
		this.rwLock = new InterProcessReadWriteLock(client, LOCK_BASE_PATH);
	}
	
	public ZooKeeperReadWriteLock readLock() {
		lock = rwLock.readLock();
		return this;
	}
	
	public ZooKeeperReadWriteLock writeLock() {
		lock = rwLock.writeLock();
		return this;
	}
	
	public void releaseReadLock() throws Exception {
		if(rwLock != null) {
			if(rwLock.readLock().isAcquiredInThisProcess()) {
				rwLock.readLock().release();
			}
		} 
	}
	
	public void releaseWriteLock() throws Exception {
		if(rwLock != null) {
			if(rwLock.writeLock().isAcquiredInThisProcess()) {
				rwLock.writeLock().release();
			}
		}
	}

}
