package com.ailk.eaap.o2p.common.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.ehcache.Ehcache;
import net.rubyeye.xmemcached.XMemcachedClient;


/**
 * 缓存工厂实现类,创建出实际的缓存实例
 * @author MAWL
 *
 * @param <K>
 * @param <V>
 */
public class CacheFactory<K,V> implements ICacheFactory<K, V>, Serializable {

	private static final long serialVersionUID = 1L;
	private static final String CACHE_MODEL_MEMCACHE = "MEMCACHE";
	private static final String CACHE_MODEL_JAVA = "JAVA";
	private static final String CACHE_MODEL_EHCACHE = "EHCACHE";
	private ConcurrentHashMap<K,V> javaCache;//spring注入对象
	private Ehcache ehcache;
	private Redis<K,V> redis;
	public void setRedis(Redis<K,V> redis) {
		this.redis = redis;
	}

	private XMemcachedClient runTimeMemcachedClient;
	private Map<String,Object> map = new HashMap<String,Object>();
	private String cacheMode = "";
	private String localCacheMode = "";
	
	public String getLocalCacheMode() {
		return localCacheMode;
	}

	public void setLocalCacheMode(String localCacheMode) {
		this.localCacheMode = localCacheMode;
	}

	public String getCacheMode() {
		return cacheMode;
	}

	public void setCacheMode(String cacheMode) {
		this.cacheMode = cacheMode;
	}

	@SuppressWarnings("unchecked")
	@Override
	public  JavaCache<K, V> getConcurrentHashMap() {
		synchronized(CacheFactory.class){
			if(!map.containsKey(CACHE_MODEL_JAVA)){
				JavaCache<K,V> cache = new JavaCache<K,V>();
				cache.setConcurrentHashMap(javaCache);
				map.put(CACHE_MODEL_JAVA, cache);
			}
			return (JavaCache<K,V>)map.get(CACHE_MODEL_JAVA);
		}
	}

	@Override
	public MemCache getXMemcachedClient() {
		synchronized(CacheFactory.class){
			if(!map.containsKey(CACHE_MODEL_MEMCACHE)){
				MemCache cache = new MemCache();
				cache.setRunTimeMemcachedClient(runTimeMemcachedClient);
				map.put(CACHE_MODEL_MEMCACHE, cache);
			}
			return (MemCache)map.get(CACHE_MODEL_MEMCACHE);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public EhCache<K,V> getEhCache() {
		synchronized(CacheFactory.class){
			if(!map.containsKey(CACHE_MODEL_EHCACHE)){
				EhCache<K,V> cache = new EhCache<K,V>();
				cache.setCache(this.getEhcache());
				map.put(CACHE_MODEL_EHCACHE, cache);
			}
			return (EhCache<K,V>)map.get(CACHE_MODEL_EHCACHE);
		}
	}

	public ConcurrentHashMap<K, V> getJavaCache() {
		return javaCache;
	}

	public void setJavaCache(ConcurrentHashMap<K, V> javaCache) {
		this.javaCache = javaCache;
	}

	public Ehcache getEhcache() {
		return ehcache;
	}

	public void setEhcache(Ehcache ehcache) {
		this.ehcache = ehcache;
	}

	public XMemcachedClient getRunTimeMemcachedClient() {
		return runTimeMemcachedClient;
	}

	public void setRunTimeMemcachedClient(XMemcachedClient runTimeMemcachedClient) {
		this.runTimeMemcachedClient = runTimeMemcachedClient;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ICache<K, V> getCacheClient() {
		if(cacheMode.equals("memcache"))
			return (ICache<K, V>) this.getXMemcachedClient();
		if(cacheMode.equals("java"))
			return (ICache<K, V>) this.getConcurrentHashMap();
		if(cacheMode.equals("ehcache"))
			return (ICache<K, V>) this.getEhCache();
		return (ICache<K, V>) this.getRedis();
	}

	@Override
	public Redis<K, V> getRedis() {
		return redis;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ICache<K, V> getLocalCacheClient() {
		if(localCacheMode.equals("memcache"))
			return (ICache<K, V>) this.getXMemcachedClient();
		if(localCacheMode.equals("java"))
			return (ICache<K, V>) this.getConcurrentHashMap();
		if(localCacheMode.equals("ehcache"))
			return (ICache<K, V>) this.getEhCache();
		return (ICache<K, V>) this.getRedis();
	}
}
