package com.ailk.eaap.o2p.util.file.remote.session;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.ailk.eaap.o2p.util.file.remote.session.model.ExtAttr;

/**
 * creat by zhuangyq
 * @author 颖勤
 *
 * @param <T,F>
 */
public interface Session<T,F> {
	
	public static final String ERROR_CODE_9999 = "9999";
	public static final String ORDERBY_NO = "-1";
	public static final String ORDERBY_MODIFY_DATE_DESC = "0";
	public static final String ORDERBY_MODIFY_DATE_ASC = "1";
	public static final String ORDERBY_NAME_ASC = "2";
	public static final String ORDERBY_NAME_DESC = "3";
	public static final String ORDERBY_LENGTH_DESC = "4";
	public static final String ORDERBY_LENGTH_ASC = "5";
	public static final String ORDERBY_FILE_TYPE_DESC = "6";
	public static final String ORDERBY_FILE_TYPE_ASC = "7";	
	
	boolean remove(String path) throws IOException;

	T[] list(String path) throws IOException;
	
	T[] list(String path,String filterPattern)throws IOException;
	
	Object[] listFiles(String path,String filterExpression)throws IOException;

	void read(String source, OutputStream outputStream) throws IOException;

	void write(InputStream inputStream, String destination) throws IOException;

	boolean mkdir(String directory) throws IOException;
	
	boolean changeDir(String directory) throws IOException;

	void rename(String pathFrom, String pathTo) throws IOException;

	void close();

	boolean isOpen();

	boolean exists(String path) throws IOException;

	String[] listNames(String path) throws IOException;
	
	void downloadFromDir(Map<String,String> fileMap,List<File> fileList,String fileSort,List<ExtAttr> moveFileInfos, Integer num,String remoteDir, String localPath,String fileFormats,DownloadCallBack<F> callBack, String isScanSubDirStr)throws IOException;
	
	void downloadFromDir(Map<String,String> fileMap,List<File> fileList,String fileSort,List<ExtAttr> moveFileInfos, Integer num,String remoteDir, String localPath,String filterExpression, String fileFormats,DownloadCallBack<F> callBack, String isScanSubDirStr)throws IOException;
	
	public void upload(String directory, String uploadFile)throws IOException;
	/**
	 * Retrieve a remote file as a raw {@link InputStream}.
	 * @param source The path of the remote file
	 * @return The raw inputStream.
	 */
	InputStream readRaw(String source) throws IOException;

	/**
	 * Invoke after closing the InputStream from {@link #readRaw(String)}.
	 * Required by some session providers.
	 * @return true if successful.
	 * @throws IOException
	 */
	boolean finalizeRaw() throws IOException;
	
}
