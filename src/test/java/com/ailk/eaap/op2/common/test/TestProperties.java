package com.ailk.eaap.op2.common.test;

import java.io.*;
import java.util.Map;
import java.util.Properties;

import com.ailk.eaap.o2p.common.codec.BatchChangeProperties;

public class TestProperties {

	public static void main(String[] args) {
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream("E:/eaap_common.properties"));
			Properties proper = BatchChangeProperties.getEncProperties(BatchChangeProperties.getProperties(in));
			for(Map.Entry<Object, Object> entry : proper.entrySet()){
				String key = entry.getKey().toString();
				String value = entry.getValue().toString();
				System.out.println(key+"="+value);
				System.out.println();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
