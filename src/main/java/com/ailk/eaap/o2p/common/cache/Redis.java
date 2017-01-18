package com.ailk.eaap.o2p.common.cache;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.asiainfo.foundation.exception.BusinessException;


public class Redis<K,V> implements ICache<String, V>{
	private static Log log = LogFactory.getLog(Redis.class);
	private StringRedisTemplate redisTemplate;
	
	public StringRedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public boolean put(String key, final V value) {
		if(value==null){
			log.error("value can not be null! key="+key);
			return false;
		}
		try{
			final String parseKey = (String) KeyParseUtil.parse(key);
			redisTemplate.execute(new RedisCallback<Object>() {  
		        @Override  
		        public Object doInRedis(RedisConnection connection) throws DataAccessException {  
		            connection.set(parseKey.getBytes(), SerializeUtil.serialize(value));  
		            return true;  
		        }  
		    });
			return true;
		}catch(Exception e){
			log.error("put object into Redis failed! key="+key, e);
			return false;
		}
	}

	@Override
	public boolean put(String key, int exp, V value) {
		return this.put(key, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(String key) {
		try{
			if(key==null){
				log.info("key is null!");
				return null;
			}
			final String parseKey = (String) KeyParseUtil.parse(key);
			return redisTemplate.execute(new RedisCallback<V>() {  
		        @Override  
		        public V doInRedis(RedisConnection connection) throws DataAccessException {  
		        	if (connection.exists(parseKey.getBytes())) {  
		                byte[] bt = connection.get(parseKey.getBytes());  
		                if(bt!=null){
		    				return (V) SerializeUtil.unserialize(bt);
		    			}
		            }  
		            return null;  
		        }  
		    });
		}catch(Exception e){
			log.error("get object from Redis failed! key="+key, e);
			return null;
		}
	}

	@Override
	public boolean remove(String key) {
		try{
			final String parseKey = (String) KeyParseUtil.parse(key);
			redisTemplate.execute(new RedisCallback<Object>() {  
		        public Object doInRedis(RedisConnection connection) {  
		            connection.del(parseKey.getBytes());  
		            return true;  
		        }  
		    });
		}catch(Exception e){
			log.error("remove object from Redis failed! key="+key, e);
			return false;
		}
		return true;
	}

	@Override
	public void clear() throws CacheException {
		try{
			redisTemplate.execute(new RedisCallback<Object>() {  
		        public Object doInRedis(RedisConnection connection) {  
		            connection.flushAll();
		            return null;
		        }  
		    });
		}catch(Exception e){
			log.error("Redis clear exception:"+e.getMessage()+",detail:"+ e);
		}
	}

	@Override
	public long getSizeInMemory() {
		//get size
		
		return 0;
	}

	@Override
	public boolean contains(String key) {
		return false;
	}

	@Override
	public void destroy() throws CacheException {
		//destroy

	}

	@Override
	public Set<String> getKeySet() throws CacheException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public V getByTenantId(String key, Integer tenantId)
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
