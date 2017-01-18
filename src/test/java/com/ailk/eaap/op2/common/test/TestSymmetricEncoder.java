package com.ailk.eaap.op2.common.test;

import com.ailk.eaap.o2p.common.codec.SymmetricEncoder;


public class TestSymmetricEncoder {

	public static void main(String[] args) {
		System.out.println(SymmetricEncoder.getEncString("jdbc:mysql://192.168.1.53:3306/op2_conf?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull"));
		System.out.println(SymmetricEncoder.getDesString("iWr/LJL0naY2arYc7xok7c5HMl1Lpd7aOwyrMfM7uZtra72ddYyrz0iqUZomSSu7EFr0D1p+RO54cATKi4pM7kJfsNw1snRadG28dP7OQ+wyOh2B9Gy9rVBasanx6uU/bIqsHQiA8zhN/RP/+TAHmG+qPPodQ/wX"));
	}

}
