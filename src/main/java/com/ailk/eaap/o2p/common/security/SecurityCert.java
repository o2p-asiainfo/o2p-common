package com.ailk.eaap.o2p.common.security;

import java.io.Serializable;

public class SecurityCert implements Serializable{
	private String id;
	private String alias;
	private String algorithm;
	private String subject;
	private String serialNumber;
	private String passSaltValue;
	private long timeout;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getPassSaltValue() {
		return passSaltValue;
	}
	public void setPassSaltValue(String passSaltValue) {
		this.passSaltValue = passSaltValue;
	}
	public long getTimeout() {
		return timeout;
	}
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	
}
