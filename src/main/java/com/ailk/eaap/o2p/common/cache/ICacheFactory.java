package com.ailk.eaap.o2p.common.cache;



/**
 * 缓存工厂抽象接口，定义缓存的几种实现方式
 * @author MAWL
 *
 * @param <K>
 * @param <V>
 */
public interface ICacheFactory<K,V> {

	/**
	 * ConcurrentHashMap的缓存实现方式
	 * @return
	 */
	public JavaCache<K,V> getConcurrentHashMap();
	/**
	 * XMemcachedClient的缓存实现方式
	 * @return
	 */
	public MemCache getXMemcachedClient(); 
	/**
	 * EhCache的缓存实现方式
	 * @return
	 */
	public EhCache<K, V> getEhCache();
	/**
	 * EhCache的缓存实现方式
	 * @return
	 */
	public Redis<K, V> getRedis();
	/**
	 * 获取客户端缓存对象
	 * @return
	 */
	public ICache<K, V> getCacheClient();
	/**
	 * 获取本地客户端缓存对象
	 * @return
	 */
	public ICache<K, V> getLocalCacheClient();
}
