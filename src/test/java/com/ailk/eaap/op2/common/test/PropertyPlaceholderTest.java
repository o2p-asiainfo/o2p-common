package com.ailk.eaap.op2.common.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.CollectionUtils;

import com.ailk.eaap.o2p.common.spring.config.CfgCacheHolder;
import com.ailk.eaap.o2p.common.spring.config.O2pPropertyPlaceholderConfigurer;

/*@RunWith(SpringJUnit4ClassRunner.class)  
 @ContextConfiguration({"classpath:o2p-test-spring.xml"})  */
public class PropertyPlaceholderTest extends TestBase
{
    /*
     * @InjectMocks
     * 
     * @Resource(name= "cfgCacheHolder") private CfgCacheHolder cfgCacheHolder;
     * 
     * @Resource(name= "common-dataSource") private MyDataSource myDataSource;
     */
    @Test
    public void test_1() throws IOException, NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException
    {
        CfgCacheHolder cfgCache = mock(CfgCacheHolder.class);
        // 私有变量mock的方式，下面的mock不适合，因为不够内聚
        /*
         * Field memcacheClient =
         * cfgCache.getClass().getDeclaredField("memcachedClient");
         * memcacheClient.setAccessible(true); MemcachedClient cacheMock =
         * mock(MemcachedClient.class); memcacheClient.set(cfgCache, cacheMock);
         */
        when(cfgCache.loadPropFromCache()).thenAnswer(new Answer<Properties>()
        {

            @Override
            public Properties answer(InvocationOnMock invocation)
                    throws Throwable
            {
                Properties p = new Properties();
                p.setProperty("test", "111111");
                return p;
            }
        });
        Properties prop = cfgCache.loadPropFromCache();
        Assert.assertNotNull(prop.entrySet());
        Properties localProp = new Properties();
        localProp.setProperty("niceday1111", "hllllllllllllllll");
        CollectionUtils.mergePropertiesIntoMap(localProp, prop);
        Assert.assertEquals("hllllllllllllllll",
                prop.getProperty("niceday1111"));
    }

    @Test
    public void test_2() throws IOException, Exception
    {
        O2pPropertyPlaceholderConfigurer propholder = new O2pPropertyPlaceholderConfigurer();
        propholder.setRemoteOverride(true);
        propholder.setLocalOverride(false);
        CfgCacheHolder cfgCache = mock(CfgCacheHolder.class);
        propholder.setCacheHolder(cfgCache);
        when(cfgCache.loadPropFromCache()).thenAnswer(new Answer<Properties>()
        {

            @Override
            public Properties answer(InvocationOnMock invocation)
                    throws Throwable
            {
                Properties p = new Properties();
                p.setProperty("cache.billingCycle.valid.count", "6");
                return p;
            }

        });
        propholder
                .setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);
        propholder.setSearchSystemEnvironment(true);
        propholder.setLocation(new ClassPathResource("log4j.properties"));
        Properties prop = propholder.mergeProperties();
        Assert.assertEquals("org.apache.log4j.ConsoleAppender",
                prop.getProperty("log4j.appender.stdout"));
        Assert.assertEquals("6",
                prop.getProperty("cache.billingCycle.valid.count"));
    }

    @Test(expected = Exception.class)
    public void test_3() throws IOException
    {
        O2pPropertyPlaceholderConfigurer propholder = new O2pPropertyPlaceholderConfigurer();
        propholder.setRemoteOverride(true);
        propholder.setLocalOverride(false);
        // propholder.setCacheServerUrl(cacheUrl );
        propholder.setLocation(new ClassPathResource("log4j.properties"));
        Properties prop = propholder.mergeProperties();
        prop.getProperty("log4j.appender.stdout");
    }
    /*
     * @Before public void setUp() throws IOException{
     * when(cfgCacheHolder.loadPropFromCache()).thenAnswer(new
     * Answer<Properties>(){
     * 
     * @Override public Properties answer(InvocationOnMock invocation) throws
     * Throwable { Properties p = new Properties();
     * p.setProperty("o2pCommon.jdbcDriver", "root");
     * p.setProperty("o2pCommon.database", "com.mysql");
     * p.setProperty("o2pCommon.user", "root");
     * p.setProperty("o2pCommon.password", "zhuangyq"); return p; }
     * 
     * }); }
     * 
     * @Test public void test_4() throws IOException{
     * Assert.assertEquals("root", myDataSource.getDriverClass()); }
     */

}
