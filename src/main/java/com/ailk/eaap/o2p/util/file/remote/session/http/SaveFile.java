package com.ailk.eaap.o2p.util.file.remote.session.http;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import com.asiainfo.foundation.log.LogModel;
import com.asiainfo.foundation.log.Logger;


public class SaveFile {

	private static Logger logger  = Logger.getLog(SaveFile.class);
	public final static boolean DEBUG = true; // 调试用
	private static int BUFFER_SIZE = 8096; // 缓冲区大小
	private Vector vDownLoad = new Vector(); // URL列表
	private Vector vFileList = new Vector(); // 下载后的保存文件名列表

	public void resetList() {
		vDownLoad.clear();
		vFileList.clear();
	}

	public void addItem(String url, String filename) {
		vDownLoad.add(url);
		vFileList.add(filename);
	}

	/**
	 * 　* 根据列表下载资源 　
	 */
	public void downLoadByList() {

		String url = null;
		String filename = null;

		// 按列表顺序保存资源
		for (int i = 0; i < vDownLoad.size(); i++) {
			url = (String) vDownLoad.get(i);
			filename = (String) vFileList.get(i);

			try {
				saveToFile(url, filename);
			} catch (IOException err) {
				if(logger.isDebugEnabled()){
					logger.debug("source [ {0} ] download fail !!!", url);
				}
			}
		}
		if(logger.isDebugEnabled()){
			logger.debug("download finish!");
		}
	}

	/**
	 * 将HTTP资源另存为文件
	 * 
	 * @param destUrl
	 *            String
	 * @param fileName
	 *            String
	 * @throws Exception
	 */
	public void saveToFile(String destUrl, String fileName) throws IOException {
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		URL url = null;
		byte[] buf = new byte[BUFFER_SIZE];
		int size = 0;
		try{
		// 建立链接
		url = new URL(destUrl);
		httpUrl = (HttpURLConnection) url.openConnection();
		// 连接指定的资源
		httpUrl.connect();
		// 获取网络输入流
		bis = new BufferedInputStream(httpUrl.getInputStream());
		// 建立文件
		fos = new FileOutputStream(fileName);
		if(logger.isDebugEnabled()){
			logger.debug("Getting the link[{0}]... save to file [{1}]", destUrl,fileName);
		}

		// 保存文件
		while ((size = bis.read(buf)) != -1)
			fos.write(buf, 0, size);
		}finally{
			if(fos!=null){
				fos.close();
			}
			if(bis!=null){
				bis.close();
			}
			if(httpUrl!=null){
				httpUrl.disconnect();
			}
		}
	}

	/**
	 * 主方法(用于测试)
	 * 
	 * @param argv
	 *            String[]
	 */
	public static void main(String argv[]) {
		SaveFile oInstance = new SaveFile();
		try {
			oInstance.saveToFile("http://localhost:9080/zooKeeper.ppt",
					"D:/test.ppt");
		} catch (Exception err) {
			logger.error(LogModel.EVENT_APP_EXCPT, err);
		}
	}

}
