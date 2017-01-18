package com.ailk.eaap.o2p.common.cache;

import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.asiainfo.foundation.exception.BusinessException;

import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

public class MemCache implements ICache<String,Object> {
	private static Log log = LogFactory.getLog(MemCache.class);
	private XMemcachedClient runTimeMemcachedClient;
	private int DEFAULT_TTL = 3600*24*30;
	public void setRunTimeMemcachedClient(XMemcachedClient runTimeMemcachedClient) {
		this.runTimeMemcachedClient = runTimeMemcachedClient;
	}
	public XMemcachedClient getRunTimeMemcachedClient() {
		return runTimeMemcachedClient;
	}
	public void setTTL(int ttl){
		DEFAULT_TTL = ttl;
	}
	@Override
	public boolean put(String key, Object value) {
		return put(key,DEFAULT_TTL,value);
	}
	@Override
	public boolean put(String key, int exp, Object value) {
		try {
			if(log.isDebugEnabled()){
				log.debug("start to put object into memcache,key="+key);
			}
			if(value==null){
				throw new NullPointerException();
			}
			String parsedKey = (String) KeyParseUtil.parse(key);
			return runTimeMemcachedClient.set(parsedKey, exp, value);
		} catch (TimeoutException e) {
			log.error("memcache put oper timeout!key="+key, e);
			return false;
		} catch (InterruptedException e) {
			log.error("memcache put oper InterruptedException!key="+key, e);
			return false;
		} catch (MemcachedException e) {
			log.error("memcache put oper MemcachedException!key="+key, e);
			return false;
		}catch(NullPointerException e){
			log.error("put into memcache failed! value could not be null! key="+key, e);
			return false;
		}catch(Exception e){
			log.error("put into memcache failed! key="+key, e);
			return false;
		}
	}
	@Override
	public Object get(String key) {
		try {
			String parsedKey = (String) KeyParseUtil.parse(key);
			return runTimeMemcachedClient.get(parsedKey);
		} catch (TimeoutException e) {
			log.error("memcache get oper timeout!key="+key, e);
			return null;
		} catch (InterruptedException e) {
			log.error("memcache get oper InterruptedException!key="+key, e);
			return null;
		} catch (MemcachedException e) {
			log.error("memcache get oper MemcachedException!key="+key, e);
			return null;
		}catch(Exception e){
			log.error("get from memcache failed! key="+key, e);
			return null;
		}
	}
	@Override
	public boolean remove(String key) {
		try {
			String parsedKey = (String) KeyParseUtil.parse(key);
			return runTimeMemcachedClient.delete(parsedKey);
		} catch (TimeoutException e) {
			log.error("memcache delete oper timeout!key="+key, e);
			return false;
		} catch (InterruptedException e) {
			log.error("memcache delete oper InterruptedException!key="+key, e);
			return false;
		} catch (MemcachedException e) {
			log.error("memcache delete oper MemcachedException!key="+key, e);
			return false;
		}catch(Exception e){
			log.error("remove from memcache failed! key="+key, e);
			return false;
		}
	}
	@Override
	public void clear() {
		try {
			runTimeMemcachedClient.flushAll();
		} catch (TimeoutException e) {
			log.error("memcache clear oper timeout", e);
		} catch (InterruptedException e) {
			log.error("memcache clear oper InterruptedException", e);
		} catch (MemcachedException e) {
			log.error("memcache clear oper MemcachedException", e);
		}catch(Exception e){
			log.error("clear memcache failed!", e);
		}
	}
	@Override
	public long getSizeInMemory() {
//		runTimeMemcachedClient.getCounter(arg0)
		return 0;
	}
	@Override
	public boolean contains(String key) {
		key = (String) KeyParseUtil.parse(key);
		return get(key) != null;
	}
	@Override
	public void destroy() {
		clear();
		runTimeMemcachedClient = null;
	}
	@Override
	public Set<String> getKeySet() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object getByTenantId(String key, Integer tenantId)
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
