package com.ailk.eaap.o2p.common.codec;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * properties文件加密转换处理
 * @author MAWL
 *
 */
public class BatchChangeProperties {

	private static final Log log = LogFactory.getLog(BatchChangeProperties.class);
	/**
	 * 通过输入的文件流,生成properties对象
	 * @param in 输入文件流
	 * @return
	 */
	public static Properties  getProperties(InputStream in){
		Properties proper = new Properties();
		try {
			proper.load(in);
		} catch (IOException e) {
			log.error(e.getStackTrace());
		}
		return  proper;
	}
	/**
	 * 对properties文档进行加密处理
	 * @param proper 明文properties文档
	 * @return
	 */
	public static Properties  getEncProperties(Properties proper){
		if(null != proper && proper.size()>0){
			for(Map.Entry<Object, Object> entry : proper.entrySet()){
				String key = entry.getKey().toString();
				String value = entry.getValue().toString();
				proper.setProperty(key, SymmetricEncoder.getEncString(value));//加密处理
			}
		}
		return proper;
	}
	/**
	 * 对properties文档进行解密处理
	 * @param proper 密文properties文档
	 * @return
	 */
	public static Properties getDesProperties(Properties proper){
		if(null != proper && proper.size()>0){
			for(Map.Entry<Object, Object> entry : proper.entrySet()){
				String key = entry.getKey().toString();
				String value = entry.getValue().toString();
				proper.setProperty(key, SymmetricEncoder.getDesString(value));//解密处理
			}
		}
		return proper;
	}
}
