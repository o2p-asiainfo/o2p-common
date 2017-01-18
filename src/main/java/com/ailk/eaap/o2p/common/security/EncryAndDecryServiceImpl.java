package com.ailk.eaap.o2p.common.security;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

import org.springframework.util.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public class EncryAndDecryServiceImpl implements IEncryAndDecryService{
	
	@Override
	public String encryptStr(String message, SecurityType securityType, Key secretkey) throws Exception {
		byte[] byteMi = null;
        byte[] byteMing = null;
        String strMi = "";
        BASE64Encoder base64en = new BASE64Encoder();
        try {  
            byteMing = message.getBytes("UTF8");
            if(securityType.getTransformation().contains(SecurityDomain.ECB_MODE) || !"Y".equals(securityType.getIsSymmety()))
            	byteMi = doEncDec(byteMing, Cipher.ENCRYPT_MODE, securityType, secretkey); 
            else  {
            	byte[] iv = getIv(securityType.getIv());
            	byteMi = doEncDec(byteMing, iv, Cipher.ENCRYPT_MODE, securityType, secretkey); 
            }
            strMi = base64en.encode(byteMi);
        } catch (Exception e) {
        	throw e;
        } finally {  
            base64en = null;  
            byteMing = null;  
            byteMi = null;  
        }  
        return strMi; 
	}
	
	private byte[] getIv(String iv) {
		if(!StringUtils.hasText(iv)) return new byte[0];
		String[] ivArray = iv.split(",");
		byte[] result = new byte[ivArray.length];
		for(int i=0; i<ivArray.length; i++) 
			result[i] = Byte.valueOf(ivArray[i].trim());
		return result;
	}

	@Override
	public String decodeStr(String message,SecurityType securityType, Key secretkey) throws Exception {
		BASE64Decoder base64De = new BASE64Decoder();  
        byte[] byteMing = null;  
        byte[] byteMi = null;  
        String strMing = "";  
        try {  
            byteMi = base64De.decodeBuffer(message);  
            if(securityType.getTransformation().contains(SecurityDomain.ECB_MODE) || !"Y".equals(securityType.getIsSymmety()))
            	byteMing = doEncDec(byteMi, Cipher.DECRYPT_MODE, securityType, secretkey);
            else  {
            	byte[] iv = getIv(securityType.getIv());
            	byteMing = doEncDec(byteMi, iv, Cipher.DECRYPT_MODE, securityType, secretkey); 
            }
            strMing = new String(byteMing, "UTF8");  
        } catch (Exception e) {  
        	throw e;
        } finally {  
            base64De = null;  
            byteMing = null;  
            byteMi = null;  
        }  
        return strMing;  
	}

	@Override
	public String encryptStr(String message, byte[] iv, SecurityType securityType, Key secretkey) throws Exception {
		byte[] byteMi = null;  
        byte[] byteMing = null;  
        String strMi = "";  
        BASE64Encoder base64en = new BASE64Encoder();  
        try {  
            byteMing = message.getBytes("UTF8");  
            byteMi = doEncDec(byteMing, iv, Cipher.ENCRYPT_MODE, securityType, secretkey);  
            strMi = base64en.encode(byteMi);  
        } catch (Exception e) {
        	throw e;
        } finally {  
            base64en = null;  
            byteMing = null;  
            byteMi = null;  
        }  
        return strMi; 
	}
	
	@Override
	public String decodeStr(String message, byte[] iv, SecurityType securityType, Key secretkey) throws Exception {
		BASE64Decoder base64De = new BASE64Decoder();  
        byte[] byteMing = null;  
        byte[] byteMi = null;  
        String strMing = "";  
        try {  
            byteMi = base64De.decodeBuffer(message);  
            byteMing = doEncDec(byteMi, iv, Cipher.DECRYPT_MODE,  securityType, secretkey);  
            strMing = new String(byteMing, "UTF8");  
        } catch (Exception e) {  
        	throw e;
        } finally {  
            base64De = null;  
            byteMing = null;  
            byteMi = null;  
        }  
        return strMing;  
	}
	
	private static byte[] doEncDec(byte[] byteS, int mode, SecurityType securityType, Key secretkey) throws Exception {
		byte[] byteFina = null;  
        Cipher cipher;  
        try {  
        	if(StringUtils.hasText(securityType.getProvider())) cipher = Cipher.getInstance(securityType.getTransformation(), securityType.getProvider());  
        	else cipher = Cipher.getInstance(securityType.getTransformation());
            cipher.init(mode, secretkey);
            byteFina = cipher.doFinal(byteS); 
        } catch (Exception e) {  
        	throw e;
        } finally {  
            cipher = null;  
        }  
        return byteFina;
	}
	
	private static byte[] doEncDec(byte[] byteS, byte[] iv, int mode, SecurityType securityType, Key secretkey) throws Exception {
		byte[] byteFina = null;  
        Cipher cipher;  
        try {  
        	if(StringUtils.hasText(securityType.getProvider())) cipher = Cipher.getInstance(securityType.getTransformation(), securityType.getProvider());  
        	else cipher = Cipher.getInstance(securityType.getTransformation());
        	IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(mode, secretkey, ivParameterSpec);  
            byteFina = cipher.doFinal(byteS);  
        } catch (Exception e) {  
        	throw e;
        } finally {  
            cipher = null;  
        }  
        return byteFina;
	}

}
