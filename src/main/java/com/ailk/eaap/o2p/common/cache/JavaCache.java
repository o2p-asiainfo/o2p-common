package com.ailk.eaap.o2p.common.cache;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.asiainfo.foundation.exception.BusinessException;

public class JavaCache<K, V> implements ICache<K, V>{

	private ConcurrentHashMap<K,V> concurrentHashMap;//spring注入对象
	private static Log log = LogFactory.getLog(JavaCache.class);
	/**
	 * 添加一个键值对
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean put(K key, V value) {
		try{
			if(log.isDebugEnabled()){
				log.debug("the key be stored in javacache,key ="+key);
			}
			if(value==null){
				log.error("value could not be null! key="+key);
				return false;
			}
			K parseKey = (K) KeyParseUtil.parse(key);
			concurrentHashMap.put(parseKey, value);
//			if(null != obj){//obj should be null where there is no object for this key in cache befour
//				return true;
//			}
		}catch(Exception e){
			log.error("put object into javaCache fieled! key="+key, e.getCause());
			return false;
		}
		return true;
	}
	/**
	 * 添加一个键值对
	 */
	@Override
	public boolean put(K key, int exp, V value) {
		return this.put(key, value);
	}
    /**
     * 由KEY值得到VALUE
     */
	@SuppressWarnings("unchecked")
	@Override
	public V get(K key) {
		try{
			Object parseKey = (K) KeyParseUtil.parse(key);
			return concurrentHashMap.get(parseKey);
		}catch(Exception e){
			log.error("get object from javaCache failed! key="+key, e);
			return null;
		}
	}
   /**
    * 移除一个对象
    */
	@Override
	public boolean remove(K key) {
		try{
			Object parseKey = KeyParseUtil.parse(key);
			V obj = concurrentHashMap.remove(parseKey);
			if(null != obj){
				return true;
			}
		}catch(Exception e){
			log.error("remove object from javaCache failed! key="+key, e);
			return false;
		}
		return false;
	}
   /**
    * 清空所有键值对
    */
	@Override
	public void clear() {
		concurrentHashMap.clear();
	}
    /**
     * 得到键值对数量
     */
	@Override
	public long getSizeInMemory() {
		return concurrentHashMap.size();
	}
    /**
     * 是否包含传入键值对象
     */
	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(K key) {
		key = (K) KeyParseUtil.parse(key);
		return concurrentHashMap.containsKey(key);
	}
	@Override
	public void destroy() {
		this.clear();
		concurrentHashMap = null;
	}
	public ConcurrentHashMap<K, V> getConcurrentHashMap() {
		return concurrentHashMap;
	}
	public void setConcurrentHashMap(ConcurrentHashMap<K, V> concurrentHashMap) {
		this.concurrentHashMap = concurrentHashMap;
	}
	@Override
	public Set<K> getKeySet() {
		return this.concurrentHashMap.keySet();
	}
	@SuppressWarnings("unchecked")
	@Override
	public V getByTenantId(K key, Integer tenantId) throws BusinessException {
		if(tenantId == null) {
			tenantId = CacheKey.defaultTenantId;
		}
		if(key instanceof String) {
			key = (K) (tenantId + key.toString());
		}
		if(key == null) {
			key = (K) tenantId.toString();
		}
		return get(key);
	}
	

}
