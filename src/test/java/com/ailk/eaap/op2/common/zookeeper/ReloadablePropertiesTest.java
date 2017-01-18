package com.ailk.eaap.op2.common.zookeeper;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ailk.eaap.op2.common.test.KafkaConnectionBean;

public class ReloadablePropertiesTest {
	
	public static void  main(String[] args) throws InterruptedException{
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/o2p-prop-spring-test.xml");
		KafkaConnectionBean bean = (KafkaConnectionBean) ctx.getBean("kafka");
		while(true){
			System.out.println(bean.toString());
			Thread.sleep(1000);
		}
	}
}
