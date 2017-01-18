package com.ailk.eaap.o2p.common.security;

import java.io.Serializable;

public class SecurityKey implements Serializable {
	private int id;
	private String pubPri;
	private String isQuote;
	private String keyAlias;
	private String keyValue;
	private int keyLength;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPubPri() {
		return pubPri;
	}

	public void setPubPri(String pubPri) {
		this.pubPri = pubPri;
	}

	public String getIsQuote() {
		return isQuote;
	}

	public void setIsQuote(String isQuote) {
		this.isQuote = isQuote;
	}

	public String getKeyAlias() {
		return keyAlias;
	}

	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public int getKeyLength() {
		return keyLength;
	}

	public void setKeyLength(int keyLength) {
		this.keyLength = keyLength;
	}
	
}
