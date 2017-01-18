package com.ailk.eaap.o2p.common.cache;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.asiainfo.foundation.exception.BusinessException;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;


public class EhCache<K,V> implements ICache<Object, Object>{
	private Ehcache cache;
	private static Log log = LogFactory.getLog(EhCache.class);
	public void setCache(Ehcache cache) {
		this.cache = cache;
	}

	@Override
	public boolean put(Object key, Object value) {
		try{
			Object parseKey = KeyParseUtil.parse(key);
			Element element = new Element(parseKey, value);
			cache.put(element);
		}catch(Exception e){
			log.error("put object into ehCache fieled! key="+key, e.getCause());
			return false;
		}
		return true;
	}

	@Override
	public boolean put(Object key, int exp, Object value) {
		return put(key, value);
	}

	@Override
	public Object get(Object key) {
		try{
			Object parseKey = KeyParseUtil.parse(key);
			Element element = cache.get(parseKey);
			if (element != null)
				return element.getValue();
		}catch(Exception e){
			log.error("get object from ehCache failed! key="+key, e);
			return null;
		}
		return null;
	}

	@Override
	public boolean remove(Object key) {
		try{
			Object parseKey = KeyParseUtil.parse(key);
			cache.remove(parseKey);
			return true;
		}catch(Exception e){
			log.error("remove object from ehCache failed! key="+key, e);
			return false;
		}
	}

	@Override
	public void clear() {
		cache.removeAll();
	}

	@Override
	public long getSizeInMemory() {
		return cache.getSize();
	}

	@Override
	public boolean contains(Object key) {
		key = KeyParseUtil.parse(key);
		return this.cache.get(key) != null;
	}

	@Override
	public void destroy() {
		cache.removeAll();
		cache = null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Object> getKeySet() {
		return new HashSet<Object>(cache.getKeys());
	}

	@Override
	public Object getByTenantId(Object key, Integer tenantId)
			throws BusinessException {
		if(tenantId == null) {
			tenantId = CacheKey.defaultTenantId;
		}
		if(key != null) {
			key = tenantId + key.toString();
		} else {
			key = tenantId.toString();
		}
		return get(key);
	}

}
