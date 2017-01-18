package com.ailk.eaap.o2p.common.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.Key;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;
import com.ailk.eaap.o2p.common.spring.config.CfgCacheHolder;
import com.ailk.eaap.o2p.common.util.LocalUtils;
import com.asiainfo.foundation.exception.BusinessException;
import com.asiainfo.foundation.log.LogModel;
import com.asiainfo.foundation.log.Logger;

public class SecurityUtil {
	private IEncryAndDecryService encryAndDecryService = new EncryAndDecryServiceImpl();
	private IKeyGetService keyGetService = new KeyGetDataBaseServiceImpl();
	private boolean supportHSM = false;
	private boolean isEncrypt = true;
	private SecurityType st = null;
	private boolean highSecurityLevel = true;
	private static SecurityUtil su = null;
	private final static Logger logger = Logger.getLog(EncryAndDecryServiceImpl.class);
	
	public void init() throws IOException {
		storeSerializeSt();
		setHighSecurityLevel();
		if(highSecurityLevel) {
			setKey();
		}
		//优化设置
		String isEncryptObj = getIsEncryptObj();
		if(isEncryptObj != null) {
			isEncrypt = Boolean.valueOf((String)isEncryptObj);
		}
		if(supportHSM) {
			if(st.getProvider().contains(SecurityDomain.SECURITY_ENC_LUNA_TYPE)) {
				ExecutorService es = Executors.newFixedThreadPool(1);
				ProviderList.listProviders();
				try {
					HSM_Manager.hsmLogin(st.getLoginPass());
				} catch (Exception e) {
					logger.error("login HSM error and will reconnect again after 60s..." + e.getMessage());
				}
				es.submit(new HSMLoginCheckThread(st.getLoginPass()));
			}
		}
	}
	
	private String getIsEncryptObj() throws IOException {
		if(LocalUtils.getSystemVariable("isEncrypt") != null) {
			return LocalUtils.getSystemVariable("isEncrypt");
		} else if(new ClassPathResource(CfgCacheHolder.ENV_CFG_PROP_RESOURCE).exists()) {
			Properties cacheLocationProp = PropertiesLoaderUtils.loadProperties(new ClassPathResource(CfgCacheHolder.ENV_CFG_PROP_RESOURCE));
			return cacheLocationProp.getProperty("isEncrypt");
		}
		return null;
	}

	private void setHighSecurityLevel() throws IOException {
		if(LocalUtils.getSystemVariable("HIGH_SECURITY_LEVEL") != null) {
			highSecurityLevel = Boolean.valueOf(LocalUtils.getSystemVariable("HIGH_SECURITY_LEVEL"));
		} else {
			if(new ClassPathResource(CfgCacheHolder.ENV_CFG_PROP_RESOURCE).exists()) {
				Properties cacheLocationProp = PropertiesLoaderUtils.loadProperties(new ClassPathResource(CfgCacheHolder.ENV_CFG_PROP_RESOURCE));
				if(StringUtils.hasText(cacheLocationProp.getProperty("HIGH_SECURITY_LEVEL"))) {
					highSecurityLevel = Boolean.valueOf(cacheLocationProp.getProperty("HIGH_SECURITY_LEVEL"));
				}
			}
		}
	}

	public void setKey() {
		if(st == null) {
			return;
		}
		SecurityType stClone = st;
		while(stClone != null) {
			if(stClone.getUpSecurityType() != null) {
				if("N".equalsIgnoreCase(stClone.getIsSymmety()) && "Y".equalsIgnoreCase(stClone.getUpSecurityType().getIsSymmety())) {
					if(stClone.getUpSecurityType().getKeys() != null && stClone.getUpSecurityType().getKeys().get(SecurityVariable.secretKey) != null) {
						stClone.getUpSecurityType().getKeys().get(SecurityVariable.secretKey).setKeyValue(KeyGetDataBaseServiceImpl.DEFAULT_SECURITY_KEY);
					}
				}
			}
			stClone = stClone.getUpSecurityType();
		}
	}

	public void destoryHSM(){
		logger.info(LogModel.EVENT_APP_STATUS, "security bean destory and HSM loginout...");
		HSM_Manager.hsmLogout();		
	}
	public void storeSerializeSt() {
		ObjectInputStream ois = null;
		FileInputStream fis = null;
		try {
			ois = new ObjectInputStream(this.getClass().getClassLoader().getResourceAsStream(SecurityDomain.SECURITY_TYPE_SECURITY_FILE));
			st = (SecurityType) ois.readObject();
		} catch(Exception e) {
			logger.error("antitone sequence failure	cause ", e);
		} finally {
			try {
				if(ois != null) {
					ois.close();
					ois = null;
				}
				if(fis != null) {
					fis.close();
					fis = null;
				}
			} catch(Exception e) {
				logger.error("antitone sequence failure	cause ", e);
			}
		}
	}
	
	private SecurityUtil() {}
	
	public static SecurityUtil getInstance() {
		if (su == null) {  
	        synchronized (SecurityUtil.class) {  
		        if (su == null) {  
		            su = new SecurityUtil();
		            try {
		            	su.init();
		            } catch (IOException e) {
		            	logger.error("get is encry properpies fail, cause", e);
		            }
		        }
	        }  
	    }
		return su;
	}
	
	public String encryMsg(String clearText, SecurityType st) {
		try {
			if(clearText == null || !isEncrypt) {
				logger.warn("unsecurity mode");
				return clearText;
			}
			if(st == null) {
				logger.error("security type is null, and encry failure, the same return");
				return clearText;
			}
			return doEncryMsg(clearText, st);
		} catch(Exception e) {
			logger.error("encry error, clear text is " + clearText, e);
			return null;
		}
	}
	
	public String decryMsg(String ciperText, SecurityType st) {
		try {
			if(ciperText == null || !isEncrypt) {
				logger.warn("unsecurity mode");
				return ciperText;
			}
			if(st == null) {
				logger.error("security type is null, and decry failure, the same return");
				return ciperText;
			}
			logger.debug("ciperText={0}", ciperText,st.getName());
			return doDecryMsg(ciperText, st);
		} catch(Exception e) {
			logger.error("decry error, ciper text is " + ciperText, e);
			return null;
		}
	}
	
	public String encryMsg(String clearText) {
		return encryMsg(clearText, st);
	}
	
	public String decryMsg(String ciperText) {
		return decryMsg(ciperText, st);
	}

	/**
	 * 加密
	 * 
	 * @param clearText
	 * @param st
	 * @return
	 * @throws Exception
	 */
	private String doEncryMsg(String clearText,
			SecurityType st) throws Exception {
		try {
			SecurityType ust = st.getUpSecurityType();
			// 如果本级加密的是明文，则先进行加密
			if (ust == null || !SecurityVariable.aimKey.equalsIgnoreCase(st
					.getUpEncryAim())) {
				Key key = (Key) getKey(st, SecurityVariable.ENC);
				clearText = encryAndDecryService.encryptStr(clearText, st,
						key);
				return clearText;
			} else {
				Key decKey = getKey(st, SecurityVariable.DEC);
				String key = encryAndDecryService.decodeStr(
						ust.getKeys().get(SecurityVariable.secretKey)
								.getKeyValue(), st, decKey);
				ust.getKeys().get(SecurityVariable.secretKey).setKeyValue(key);
				degrade(st, ust);
				return doEncryMsg(clearText, st);
			}
		} catch(Exception e) {
			throw e;
		} finally {
			st = null;
		}
	}
	
	private void degrade(SecurityType st, SecurityType ust) {
		st.setAlgorithmType(ust.getAlgorithmType());
		st.setCert(ust.getCert());
		st.setIsSymmety(ust.getIsSymmety());
		st.setIv(ust.getIv());
		st.setKeys(ust.getKeys());
		st.setKeyStore(ust.getKeyStore());
		st.setLoginPass(ust.getLoginPass());
		st.setName(ust.getName());
		st.setProvider(ust.getProvider());
		st.setTransformation(ust.getTransformation());
		st.setUpEncryAim(ust.getUpEncryAim());
		st.setUpSecurityType(ust.getUpSecurityType());
		st.setCode(ust.getCode());
	}
	
	/**
	 * 获取密钥
	 * @param st
	 * @param encOrDnc
	 * @return
	 * @throws BusinessException
	 */
	private Key getKey(SecurityType st, String encOrDnc)  throws Exception{
		if("Y".equalsIgnoreCase(st.getIsSymmety())) {
			SecurityKey securityKey = st.getKeys().get(SecurityVariable.secretKey);
			return keyGetService.getSecretKeyBySecurityKey(securityKey,st);
		} else {
			if(SecurityVariable.ENC.equals(encOrDnc)) {
				return keyGetService.getPublicKeyBySecurityKey(st);
			}
			else  {
				SecurityKey securityKey = st.getKeys().get(SecurityVariable.priKey);
				return keyGetService.getPrivateKeyBySecurityKey(securityKey, st);
			}
		}
	}

	/**
	 * 解密
	 * 
	 * @param ciperText
	 * @param st
	 * @return
	 * @throws Exception
	 */
	private String doDecryMsg(String ciperText,
			SecurityType st) throws Exception{
		
		logger.debug("ciperText={0}", ciperText,st.getName());
		SecurityType ust = st.getUpSecurityType();
		Key decKey = getKey(st, SecurityVariable.DEC);
		try {
			if (ust != null) {
				// 如果本级加密的是键，先解密下级的key，然后再调用下级解密
				if (SecurityVariable.aimKey.equalsIgnoreCase(st
						.getUpEncryAim())) {
					String key = encryAndDecryService.decodeStr(
							ust.getKeys().get(SecurityVariable.secretKey)
									.getKeyValue(), st, decKey);
					ust.getKeys().get(SecurityVariable.secretKey).setKeyValue(key);
					degrade(st, ust);
					ciperText = doDecryMsg(ciperText, ust);
				} else {
					// 如果本级加密的是明文，则先调用下级解密密文，然后再本级解密
					ciperText = doDecryMsg(ciperText, ust);
					ciperText = encryAndDecryService.decodeStr(ciperText, st,decKey);
				}
			} else {
				ciperText = encryAndDecryService.decodeStr(ciperText, st,
						decKey);
			}
		} catch(Exception e) {
			throw e;
		} finally {
			st = null;
		}
		return ciperText;
	}

	public void setSupportHSM(boolean supportHSM) {
		this.supportHSM = supportHSM;
	}

	public void setEncrypt(boolean isEncrypt) {
		this.isEncrypt = isEncrypt;
	}

	public boolean isHighSecurityLevel() {
		return highSecurityLevel;
	}

	public void setHighSecurityLevel(boolean highSecurityLevel) {
		this.highSecurityLevel = highSecurityLevel;
	}
}
