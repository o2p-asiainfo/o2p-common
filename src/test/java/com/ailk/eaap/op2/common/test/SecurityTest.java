package com.ailk.eaap.op2.common.test;

import junit.framework.Assert;

import org.junit.Test;

import com.ailk.eaap.o2p.common.security.SecurityUtil;

public class SecurityTest {
	@Test
	public void testEncryAndDecry() {
		String cipher = SecurityUtil.getInstance().encryMsg("yang");
		System.out.println(cipher);
//		System.out.println(SecurityUtil.getInstance().decryMsg("QXgH6JYobZzsp30fkJY4/8zImxUyBaDNjjS8OSeEzXY="));   //
//		Assert.assertEquals("ailk", SecurityUtil.getInstance().decryMsg(cipher));
	}
	
	@Test
	public void testEncryAndDecry1() {
		String aa = SecurityUtil.getInstance().decryMsg("M5u8/896Yg8Hi+OLPXKu4Q==");
		System.out.println(aa);
		Assert.assertEquals("ailk", aa);
		
		aa = SecurityUtil.getInstance().decryMsg("aabbcc");
		Assert.assertEquals(null, aa);
	}
	
	@Test
	public void testIsEncry() {
		String aa = SecurityUtil.getInstance().decryMsg("C+77ogqzx/sHL3Yh/5XU9g==");
		
		
		Assert.assertEquals("ailk", aa);
	}
	
}
