package com.ailk.eaap.o2p.common.security;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.util.StringUtils;

import com.asiainfo.foundation.log.Logger;

public class KeyGetDataBaseServiceImpl implements IKeyGetService {
	private final static Logger logger = Logger.getLog(KeyGetDataBaseServiceImpl.class);
	public static final String DEFAULT_SECURITY_KEY = "YrDVux5NxGBDQC2bSNdwUY9qqgUQy6CTd+483b2CS/Oyt5sBrgUk6q7ZYTSdDSsu7d1IO79hptjk"+
			"0VX1dMKMggTgaDYTP8e8yvsK5KY/4YjO9/F3fFbU3dLrfoadWflSZrbVMoSVh75wCsRPBrfQ0Jj7"+
			"Yf3vuzbrm8A3InkmOi83/q4jucJFv0I1D+b0Tm3d1TUETFatLuRygwDu+pc/7no7QTI3NlYSTETB"+
			"ACpb4Z8d7skGtnbyf3jB4+DdG1O/imN8E3yL3evhEQdLZYFv4biwLkNaldZ6150lKiS4Se9H6a8e"+
			"KQoi1jh08bCd8h8I+XJ6c+MJAaRoLkXz62TMcQ==";

	@Override
	public SecretKey getSecretKeyBySecurityKey(SecurityKey key, SecurityType type) throws Exception {
		if(StringUtils.hasText(key.getKeyAlias())) {
			return (SecretKey)getKeyByAlias(key, type);
		} else {
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(key.getKeyValue().getBytes());  
			KeyGenerator _generator = null;
			SecretKey secretkey = null;
			if(StringUtils.hasText(type.getAlgorithmType()) && StringUtils.hasText(type.getProvider())) _generator = KeyGenerator.getInstance(type.getAlgorithmType(), type.getProvider());
			else if(StringUtils.hasText(type.getAlgorithmType())) _generator = KeyGenerator.getInstance(type.getAlgorithmType());
			if(_generator != null) {
				int length = key.getKeyValue().length();
				if(length <= 16) {
					length = 16*8;
				} else if(length > 16 && length <= 24) {
					length = 24*8;
				} else {
					length = 32*8;
				}
				_generator.init(length, secureRandom);
		        secretkey = _generator.generateKey();  
			}
	        return secretkey;
		}
	}

	@Override
	public PublicKey getPublicKeyBySecurityKey(SecurityType type) throws Exception {
		KeyStore ks = loadKeyStore(type.getKeyStore());
		Certificate cert =ks.getCertificate(type.getCert().getAlias());																																										
		return cert.getPublicKey();
	}

	@Override
	public PrivateKey getPrivateKeyBySecurityKey(SecurityKey key, SecurityType type) throws Exception{
		if("Y".equalsIgnoreCase(key.getIsQuote())) {
			return (PrivateKey)getKeyByAlias(key, type);
		} else {
			return null;
		}
	}
	
	private Key getKeyByAlias(SecurityKey key, SecurityType type) throws Exception{
		KeyStore ks = loadKeyStore(type.getKeyStore());
		char[] pass = StringUtils.hasText(key.getKeyValue())?key.getKeyValue().toCharArray():null;
		return ks.getKey(key.getKeyAlias(), pass);
	}
	
	private KeyStore loadKeyStore(SecurityKeyStore store) throws Exception {
		if(store == null) return null;
		logger.debug("type = "+store.getType());
		KeyStore ks= KeyStore.getInstance(store.getType().trim());
		if(SecurityDomain.SECURITY_ENC_LUNA_TYPE.equalsIgnoreCase(store.getType())) {
			if(StringUtils.hasText(store.getPassword()) && StringUtils.hasText(store.getPassword().trim())) 
				ks.load(null, store.getPassword().toCharArray());
			else ks.load(null, null);
		} else {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(store.getName());
			ks.load(in,store.getPassword().toCharArray());
			in.close();
			in = null;
		}
		return ks;
	}
	
}
