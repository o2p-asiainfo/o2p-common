package com.ailk.eaap.o2p.common.cache;

import java.util.Set;

import com.asiainfo.foundation.exception.BusinessException;

/**
 * 抽象cache接口,O2P系统内需要用到cache的地方都用此接口
 * @author zhuangyq
 *
 * @param <K>
 * @param <V>
 */
public interface ICache<K,V> {

	/**
	 * 添加缓存值
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean put(K key,V value)throws BusinessException;
	/**
	 * 添加缓存值,有过期时间
	 * @param key
	 * @param exp过期时间(秒)
	 * @param value
	 * @return
	 */
	public boolean put(K key,int exp,V value)throws BusinessException;
	/**
	 * 获得缓存对象
	 * @param key
	 * @return
	 */
	public V get(K key)throws BusinessException;
	
	/**
	 * 获得租户获取缓存对象
	 * @param key
	 * @return
	 */
	public V getByTenantId(K key, Integer tenantId)throws BusinessException;
	/**
	 * 清除缓存对象
	 * @param key
	 * @return
	 */
	public boolean remove(K key)throws BusinessException;
	
	/**
	 * 删除所有缓存内的数据

	 * @return
	 */
	public void clear()throws BusinessException;
	/**
	 * 获取内存区缓存的数量

	 * @return
	 */
	public long getSizeInMemory();
	/**
	 * 是否包含了指定key的数据

	 * @param key
	 * @return
	 */
	public boolean contains(K key);
	
	/**
	 * 释放Cache占用的资源

	 */
	public void destroy()throws BusinessException;
	
	/**
	 * 获取缓存中的key list
	 */
	public Set<K> getKeySet()throws BusinessException;
}
