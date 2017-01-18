package com.ailk.eaap.op2.common.test;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.ailk.eaap.o2p.common.codec.DES3Util;

public class DES3UtilTest {

	@Test
	public void test() throws UnsupportedEncodingException{
		byte[] b = DES3Util.encryptMode("odc".getBytes());
		System.out.println(new String(b,"utf8"));
		byte[] ret = DES3Util.decryptMode(b);
		System.out.println(new String(ret));
	}
}
