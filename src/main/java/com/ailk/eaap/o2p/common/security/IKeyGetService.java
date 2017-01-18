package com.ailk.eaap.o2p.common.security;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

public interface IKeyGetService {
	SecretKey getSecretKeyBySecurityKey(SecurityKey key, SecurityType type) throws Exception;
	PublicKey getPublicKeyBySecurityKey(SecurityType type) throws Exception;
	PrivateKey getPrivateKeyBySecurityKey(SecurityKey key, SecurityType type) throws Exception;
}
