package com.ailk.eaap.o2p.common.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SecurityType implements Serializable {
	private int id;
	private String name;
	private String code;
	private SecurityType upSecurityType;
	private String upEncryAim;
	private String isSymmety;
	private String algorithmType;
	private String transformation;
	private String provider;
	private String loginPass;
	private String iv;
	Map<String, SecurityKey> keys = new HashMap<String, SecurityKey>();
	private SecurityCert cert;
	private SecurityKeyStore keyStore;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public SecurityType getUpSecurityType() {
		return upSecurityType;
	}

	public void setUpSecurityType(SecurityType upSecurityType) {
		this.upSecurityType = upSecurityType;
	}

	public String getUpEncryAim() {
		return upEncryAim;
	}

	public void setUpEncryAim(String upEncryAim) {
		this.upEncryAim = upEncryAim;
	}

	public String getIsSymmety() {
		return isSymmety;
	}

	public void setIsSymmety(String isSymmety) {
		this.isSymmety = isSymmety;
	}

	public String getAlgorithmType() {
		return algorithmType;
	}

	public void setAlgorithmType(String algorithmType) {
		this.algorithmType = algorithmType;
	}

	public String getTransformation() {
		return transformation;
	}

	public void setTransformation(String transformation) {
		this.transformation = transformation;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Map<String, SecurityKey> getKeys() {
		return keys;
	}

	public void setKeys(Map<String, SecurityKey> keys) {
		this.keys = keys;
	}

	public SecurityCert getCert() {
		return cert;
	}
	
	public SecurityKeyStore getKeyStore() {
		return keyStore;
	}

	public void setKeyStore(SecurityKeyStore keyStore) {
		this.keyStore = keyStore;
	}

	public void setCert(SecurityCert cert) {
		this.cert = cert;
	}
	
	public String getLoginPass() {
		return loginPass;
	}

	public void setLoginPass(String loginPass) {
		this.loginPass = loginPass;
	}

	public String getIv() {
		return iv;
	}

	public void setIv(String iv) {
		this.iv = iv;
	}

	public SecurityType doClone() throws Exception{
		SecurityType st = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
			ByteArrayInputStream bais = new ByteArrayInputStream(
					baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			st = (SecurityType) ois.readObject();
		} catch (IOException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw e;
		}
		return st;

	}

}
