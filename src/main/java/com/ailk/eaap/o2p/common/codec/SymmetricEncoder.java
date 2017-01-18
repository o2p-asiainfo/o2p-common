package com.ailk.eaap.o2p.common.codec;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;  
import sun.misc.BASE64Encoder;
/**
 * 对称加密处理，采用JAVA API的处理方式
 * 提供加解密的方法
 * @author MAWL
 *
 */
@SuppressWarnings("restriction")
public class SymmetricEncoder {
	private static final Log log = LogFactory.getLog(SymmetricEncoder.class);
    private static Key key;
    private final static String GENERATE_KEY_STR = "com.ailk.eaap.o2p";//密钥
    private static final String encryptAlgorithm="DES";//加密算法
    
    /**
     * 根据参数生成KEY
     * 密钥字符串
     * @param strKey
     */
    static {
    	try {  
    		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");  
    		secureRandom.setSeed(GENERATE_KEY_STR.getBytes());  
    		KeyGenerator _generator = KeyGenerator.getInstance(encryptAlgorithm);  
            _generator.init(secureRandom);  
            key = _generator.generateKey();  
            _generator = null;     
        } catch (Exception e) {  
        	log.error(e.getStackTrace());
        }  
    }
    /**
     * 加密String明文输入,String密文输出
     * @param strMing
     * @return
     */
	public static String getEncString(String strMing) {  
        byte[] byteMi = null;  
        byte[] byteMing = null;  
        String strMi = "";  
        BASE64Encoder base64en = new BASE64Encoder();  
        try {  
            byteMing = strMing.getBytes("UTF8");  
            byteMi = getEncCode(byteMing);  
            strMi = base64en.encode(byteMi);  
        } catch (Exception e) {  
        	log.error(e.getStackTrace());
        } finally {  
            base64en = null;  
            byteMing = null;  
            byteMi = null;  
        }  
        return strMi;  
    }
    
    /**
     * 解密  以String密文输入,String明文输出
     * @param strMi
     * @return
     */
	public static String getDesString(String strMi) {  
        BASE64Decoder base64De = new BASE64Decoder();  
        byte[] byteMing = null;  
        byte[] byteMi = null;  
        String strMing = "";  
        try {  
            byteMi = base64De.decodeBuffer(strMi);  
            byteMing = getDesCode(byteMi);  
            strMing = new String(byteMing, "UTF8");  
        } catch (Exception e) {  
        	log.error(e.getStackTrace());
        } finally {  
            base64De = null;  
            byteMing = null;  
            byteMi = null;  
        }  
        return strMing;  
    }
    
    private static byte[] getEncCode(byte[] byteS) {  
        byte[] byteFina = null;  
        Cipher cipher;  
        try {  
            cipher = Cipher.getInstance(encryptAlgorithm);  
            cipher.init(Cipher.ENCRYPT_MODE, key);  
            byteFina = cipher.doFinal(byteS);  
        } catch (Exception e) {  
        	log.error(e.getStackTrace());
        } finally {  
            cipher = null;  
        }  
        return byteFina;  
    }
    
    private static byte[] getDesCode(byte[] byteD) {  
        Cipher cipher;  
        byte[] byteFina = null;  
        try {  
            cipher = Cipher.getInstance(encryptAlgorithm);  
            cipher.init(Cipher.DECRYPT_MODE, key);  
            byteFina = cipher.doFinal(byteD);  
        } catch (Exception e) {  
        	log.error(e.getStackTrace());
        } finally {  
            cipher = null;  
        }  
        return byteFina;  
    }
    
    public static void main(String[] args){
    }
}
