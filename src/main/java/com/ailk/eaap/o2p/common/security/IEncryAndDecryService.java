package com.ailk.eaap.o2p.common.security;

import java.security.Key;

public interface IEncryAndDecryService {
	String encryptStr(String message,SecurityType securityType, Key secretkey) throws Exception ;
	String decodeStr(String message,SecurityType securityType, Key secretkey) throws Exception;
	
	String encryptStr(String message, byte[] IV, SecurityType securityType, Key secretkey) throws Exception ;
	String decodeStr(String message, byte[] IV, SecurityType securityType, Key secretkey) throws Exception;
}
