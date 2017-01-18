package com.ailk.eaap.o2p.common.redis.connection;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Protocol;

import com.ailk.eaap.o2p.common.spring.config.ZKCfgCacheHolder;

/**
 * 扩展JedisConnectionFactory
 * @author 颖勤
 *
 */
public class JedisConnectionFactoryExt implements FactoryBean<JedisConnectionFactory>,InitializingBean {
	
	private final static Log LOG = LogFactory.getLog(JedisConnectionFactoryExt.class);
	
	private JedisConnectionFactory jedisConnectionFactory;
	
	private static final String REDIS_SENTINEL_MASTER_CONFIG_PROPERTY = "spring.redis.sentinel.master";
	
	private static final String REDIS_SENTINEL_NODES_CONFIG_PROPERTY = "spring.redis.sentinel.nodes";
	
	private static final String REDIS_HOSTNAME_CONFIG_PROPERTY = "spring.redis.hostName";
	private static final String REDIS_PORT_CONFIG_PROPERTY = "spring.redis.port";
	private static final String REDIS_TIMEOUT_CONFIG_PROPERTY = "spring.redis.timeout";
	private static final String REDIS_PASSWORD_CONFIG_PROPERTY = "spring.redis.password";
	// default pool model
//	private static final String REDIS_USEPOOL_CONFIG_PROPERTY = "spring.redis.usePool";
	private static final String REDIS_DBINDEX_CONFIG_PROPERTY = "spring.redis.dbIndex";
	
	private static final String REDIS_POOL_MAX_TOTAL_CONFIG_PROPERTY = "spring.redis.maxTotal";
	private static final String REDIS_POOL_MAX_IDLE_CONFIG_PROPERTY = "spring.redis.maxIdle";
	private static final String REDIS_POOL_MAX_WAIT_CONFIG_PROPERTY = "spring.redis.maxWait";
	private static final String REDIS_POOL_TESTONBORROW_CONFIG_PROPERTY = "spring.redis.testOnBorrow";
	
	private String hostName = "localhost";
	private int port = Protocol.DEFAULT_PORT;
	private int timeout = Protocol.DEFAULT_TIMEOUT;
	private String password;
	private boolean usePool = true;
	private int dbIndex = 0;
	private int poolMaxTotal = 8;
	private int poolMaxIdle = 8;
	private long poolMaxWait = -1;
	private boolean testOnBorrow = false;
	private String sentinelMaster;
	private String sentinelNodes;
	private boolean convertPipelineAndTxResults = true;
	private JedisPoolConfig poolConfig = new JedisPoolConfig();
	private JedisShardInfo shardInfo;
	
	// init propItem from global 
	public JedisConnectionFactoryExt(){
		sentinelMaster = ZKCfgCacheHolder.PROP_ITEMS.getProperty(REDIS_SENTINEL_MASTER_CONFIG_PROPERTY);
		sentinelNodes = ZKCfgCacheHolder.PROP_ITEMS.getProperty(REDIS_SENTINEL_NODES_CONFIG_PROPERTY);
		hostName = StringUtils.hasText(ZKCfgCacheHolder.PROP_ITEMS.getProperty(REDIS_HOSTNAME_CONFIG_PROPERTY))?
				ZKCfgCacheHolder.PROP_ITEMS.getProperty(REDIS_HOSTNAME_CONFIG_PROPERTY):hostName;
				
		port = StringUtils.hasText(ZKCfgCacheHolder.PROP_ITEMS.getProperty(REDIS_PORT_CONFIG_PROPERTY))?
						Integer.valueOf(ZKCfgCacheHolder.PROP_ITEMS.getProperty(REDIS_PORT_CONFIG_PROPERTY)):port;
						
		timeout = StringUtils.hasText(ZKCfgCacheHolder.PROP_ITEMS.getProperty(REDIS_TIMEOUT_CONFIG_PROPERTY))?
						Integer.valueOf(ZKCfgCacheHolder.PROP_ITEMS.getProperty(REDIS_TIMEOUT_CONFIG_PROPERTY)):timeout;	
						
		password = ZKCfgCacheHolder.PROP_ITEMS.getProperty(REDIS_PASSWORD_CONFIG_PROPERTY);
		
		dbIndex = StringUtils.hasText(ZKCfgCacheHolder.PROP_ITEMS.getProperty(REDIS_DBINDEX_CONFIG_PROPERTY))?
						Integer.valueOf(ZKCfgCacheHolder.PROP_ITEMS.getProperty(REDIS_DBINDEX_CONFIG_PROPERTY)):dbIndex;
						
		poolMaxTotal = StringUtils.hasText(ZKCfgCacheHolder.PROP_ITEMS.getProperty(REDIS_POOL_MAX_TOTAL_CONFIG_PROPERTY))?
						Integer.valueOf(ZKCfgCacheHolder.PROP_ITEMS.getProperty(REDIS_POOL_MAX_TOTAL_CONFIG_PROPERTY)):poolMaxTotal;
						
		poolMaxIdle = StringUtils.hasText(ZKCfgCacheHolder.PROP_ITEMS.getProperty(REDIS_POOL_MAX_IDLE_CONFIG_PROPERTY))?
						Integer.valueOf(ZKCfgCacheHolder.PROP_ITEMS.getProperty(REDIS_POOL_MAX_IDLE_CONFIG_PROPERTY)):poolMaxIdle;	
						
		poolMaxWait = StringUtils.hasText(ZKCfgCacheHolder.PROP_ITEMS.getProperty(REDIS_POOL_MAX_WAIT_CONFIG_PROPERTY))?
						Integer.valueOf(ZKCfgCacheHolder.PROP_ITEMS.getProperty(REDIS_POOL_MAX_WAIT_CONFIG_PROPERTY)):poolMaxWait;	
						
		testOnBorrow = StringUtils.hasText(ZKCfgCacheHolder.PROP_ITEMS.getProperty(REDIS_POOL_TESTONBORROW_CONFIG_PROPERTY))?
						Boolean.valueOf(ZKCfgCacheHolder.PROP_ITEMS.getProperty(REDIS_POOL_TESTONBORROW_CONFIG_PROPERTY)):testOnBorrow;			
		shardInfo = new JedisShardInfo(hostName,port,timeout);
		shardInfo.setPassword(password);
	} 
	
	@Override
	public JedisConnectionFactory getObject() throws Exception {
		if(jedisConnectionFactory==null){
			synchronized (this.poolConfig){
				if(StringUtils.hasText(ZKCfgCacheHolder.PROP_ITEMS.getProperty(REDIS_SENTINEL_MASTER_CONFIG_PROPERTY))){
					jedisConnectionFactory = new JedisConnectionFactory(getSentinelConfig(),getPoolConfig());
					LOG.info("instance JedisConnectionFactory for sentinel model success!");
				}else{
					jedisConnectionFactory = new JedisConnectionFactory(getPoolConfig());
					jedisConnectionFactory.setHostName(hostName);
					jedisConnectionFactory.setPort(port);
					jedisConnectionFactory.setPassword(password);
					jedisConnectionFactory.setConvertPipelineAndTxResults(convertPipelineAndTxResults);
					LOG.info("instance JedisConnectionFactory normal model success!");
				}
				jedisConnectionFactory.setDatabase(dbIndex);
				jedisConnectionFactory.setTimeout(timeout);
			}
			jedisConnectionFactory.afterPropertiesSet();
			
		}

		return jedisConnectionFactory;
	}

	private RedisSentinelConfiguration getSentinelConfig() {
		Assert.hasText(sentinelMaster, "for redis sentinel HA cluster the property sentinelMaster must not be empty");
		RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration().master(sentinelMaster);
		String[] redisHostAndPortArray = StringUtils.delimitedListToStringArray(sentinelNodes, ",");
		Set<RedisNode> redisNodeSet = new HashSet<RedisNode>();
		for (String redisHostAndPort : redisHostAndPortArray) {
			String[] redisInfo = redisHostAndPort.split(":");
			if (redisInfo != null && redisInfo.length == 2) {
				RedisNode redisNode = new RedisNode(redisInfo[0],Integer.valueOf(redisInfo[1]));
				redisNodeSet.add(redisNode);
			}
		}
		sentinelConfig.setSentinels(redisNodeSet);
		return sentinelConfig;
	}

	private JedisPoolConfig getPoolConfig() {
		poolConfig.setMaxTotal(poolMaxTotal);
		poolConfig.setMaxIdle(poolMaxIdle);
		poolConfig.setMaxWaitMillis(poolMaxWait);
		poolConfig.setTestOnBorrow(testOnBorrow);
		return poolConfig;
	}

	@Override
	public Class<?> getObjectType() {
		return JedisConnectionFactory.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}
}
