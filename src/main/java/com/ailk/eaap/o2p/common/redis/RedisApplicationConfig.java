package com.ailk.eaap.o2p.common.redis;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.StringUtils;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import com.ailk.eaap.o2p.common.util.CommonUtil;
import com.linkage.rainbow.util.StringUtil;


/**
 * @ClassName: RedisApplicationConfig
 * @Description: 
 * @author zhengpeng
 * @date 2015-10-27 下午5:24:52
 *
 */
@Configuration
@Lazy(true)
public class RedisApplicationConfig {
	
//	@Bean(name="singleRedisConnectionFactory")  
//  public RedisConnectionFactory singleRedisConnectionFactory(){
//		String isSentinel = CommonUtil.getPropertyValue("redis.isSentinel");
//		JedisConnectionFactory jedisConnectionFactory = null;
//		if(!StringUtil.isEmpty(isSentinel) && !Boolean.valueOf(isSentinel)){
//			jedisConnectionFactory = this.getSingleRedisConnectionFactory();
//		}
//		return jedisConnectionFactory;
//  }
	
	
//	@Bean(name="sentinelRedisConnectionFactory")  
//	public RedisConnectionFactory sentinelRedisConnectionFactory() {
//		String isSentinel = CommonUtil.getPropertyValue("redis.isSentinel");
//		JedisConnectionFactory jedisConnectionFactory = null;
//		if(!StringUtil.isEmpty(isSentinel) && Boolean.valueOf(isSentinel)){
//			jedisConnectionFactory = this.getSentinelRedisConnectionFactory();
//		}
//		return jedisConnectionFactory;
//	}
	
	
	@Bean(name="redisConnectionFactory")
	public RedisConnectionFactory redisConnectionFactory() {
		JedisConnectionFactory jedisConnectionFactory = null;
		String isSentinel = CommonUtil.getPropertyValue("redis.isSentinel");
		//Sentinel方式
		if(!StringUtil.isEmpty(isSentinel) && Boolean.valueOf(isSentinel)){
			jedisConnectionFactory = this.getSentinelRedisConnectionFactory();
		//单机方式
		}else{
			jedisConnectionFactory = this.getSingleRedisConnectionFactory();
		}
		return jedisConnectionFactory;
	}
	
	private JedisConnectionFactory getSentinelRedisConnectionFactory(){
		String sentinelMaster = StringUtil.isEmpty(CommonUtil.getPropertyValue("redis.sentinel.master"))?"mymaster":CommonUtil.getPropertyValue("redis.sentinel.master");	
		String redisHostAndPorts = CommonUtil.getPropertyValue("redis.sentinel.hostnameAndPort");
		
		RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration().master(sentinelMaster);
		String[] redisHostAndPortArray = StringUtils.delimitedListToStringArray(redisHostAndPorts, ",");
		Set<RedisNode> redisNodeSet = new HashSet<RedisNode>();
		for (String redisHostAndPort : redisHostAndPortArray) {
			String[] redisInfo = redisHostAndPort.split(":");
			if (redisInfo != null && redisInfo.length == 2) {
				RedisNode redisNode = new RedisNode(redisInfo[0],Integer.valueOf(redisInfo[1]));
				redisNodeSet.add(redisNode);
			}
		}
		sentinelConfig.setSentinels(redisNodeSet);
		return new JedisConnectionFactory(sentinelConfig);
	}
	
	
	private JedisConnectionFactory getSingleRedisConnectionFactory(){
		String redisHost = StringUtil.isEmpty(CommonUtil.getPropertyValue("redis.hostname"))?"localhost":CommonUtil.getPropertyValue("redis.hostname");
   	 	Integer redisPort = StringUtil.isEmpty(CommonUtil.getPropertyValue("redis.port"))?Protocol.DEFAULT_PORT:Integer.valueOf(CommonUtil.getPropertyValue("redis.port"));
   	 	String redisPwd = CommonUtil.getPropertyValue("redis.password");
   	 	Integer dbIndex = StringUtil.isEmpty(CommonUtil.getPropertyValue("redis.dbIndex"))?0:Integer.valueOf(CommonUtil.getPropertyValue("redis.dbIndex"));
   	 	Integer timeout = StringUtil.isEmpty(CommonUtil.getPropertyValue("redis.timeout"))?Protocol.DEFAULT_TIMEOUT:Integer.valueOf(CommonUtil.getPropertyValue("redis.timeout"));
   	 	
   	    JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(this.getPoolConfig());
   	    jedisConnectionFactory.setHostName(redisHost.trim());
   	    jedisConnectionFactory.setPort(redisPort);
   	    jedisConnectionFactory.setPassword(redisPwd);
		jedisConnectionFactory.setDatabase(dbIndex);
		jedisConnectionFactory.setTimeout(timeout);
   	 	return jedisConnectionFactory;
	}
	
	private JedisPoolConfig getPoolConfig(){
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		Integer poolMaxTotal = StringUtil.isEmpty(CommonUtil.getPropertyValue("redis.maxTotal"))?8:Integer.valueOf(CommonUtil.getPropertyValue("redis.maxTotal"));
		Integer poolMaxIdle = StringUtil.isEmpty(CommonUtil.getPropertyValue("redis.maxIdle"))?8:Integer.valueOf(CommonUtil.getPropertyValue("redis.maxIdle"));
		Integer poolMaxWait = StringUtil.isEmpty(CommonUtil.getPropertyValue("redis.maxWait"))?-1:Integer.valueOf(CommonUtil.getPropertyValue("redis.maxWait"));
		Boolean testOnBorrow = StringUtil.isEmpty(CommonUtil.getPropertyValue("redis.testOnBorrow"))?false:Boolean.valueOf(CommonUtil.getPropertyValue("redis.testOnBorrow"));
		poolConfig.setMaxTotal(poolMaxTotal);
		poolConfig.setMaxIdle(poolMaxIdle);
		poolConfig.setMaxWaitMillis(poolMaxWait);
		poolConfig.setTestOnBorrow(testOnBorrow);
		return poolConfig;
	}

}
