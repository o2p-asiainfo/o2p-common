package com.ailk.eaap.o2p.util.file.remote.session.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.OverlappingFileLockException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;
import org.aspectj.util.FileUtil;
import org.springframework.util.Assert;

import com.ailk.eaap.o2p.util.file.filters.FileFilterExpression;
import com.ailk.eaap.o2p.util.file.remote.session.DownloadCallBack;
import com.ailk.eaap.o2p.util.file.remote.session.Session;
import com.ailk.eaap.o2p.util.file.remote.session.model.ExtAttr;
import com.ailk.eaap.op2.common.EAAPException;
import com.ailk.eaap.op2.common.EAAPTags;
import com.asiainfo.foundation.exception.BusinessException;
import com.asiainfo.foundation.log.LogModel;

public class FtpSession implements Session<FTPFile,FTPClient>{
	private final Log logger = LogFactory.getLog(this.getClass());
	
	private final FTPClient client;

	private final AtomicBoolean readingRaw = new AtomicBoolean();

	public FtpSession(FTPClient client) {
		Assert.notNull(client, "client must not be null");
		this.client = client;
	}


	
	public boolean remove(String path) throws IOException {
		Assert.hasText(path, "path must not be null");
		boolean completed = this.client.deleteFile(path);
	 	if (!completed) {
			throw new IOException("Failed to delete '" + path + "'. Server replied with: " + client.getReplyString());
		}
		return completed;
	}

	
	public FTPFile[] list(String path) throws IOException {
		Assert.hasText(path, "path must not be null");
		return this.client.listFiles(path);
	}

	
	public String[] listNames(String path) throws IOException {
		Assert.hasText(path, "path must not be null");
		return this.client.listNames(path);
	}

	
	public void read(String path, OutputStream fos) throws IOException {
		Assert.hasText(path, "path must not be null");
		Assert.notNull(fos, "outputStream must not be null");
		boolean completed = this.client.retrieveFile(path, fos);
		if (!completed) {
			throw new IOException("Failed to copy '" + path +
					"'. Server replied with: " + this.client.getReplyString());
		}
		logger.info("File has been successfully transfered from: " + path);
	}

	
	public InputStream readRaw(String source) throws IOException {
		if (!this.readingRaw.compareAndSet(false, true)) {
			throw new IOException("Previous raw read was not finalized");
		}
		InputStream inputStream = this.client.retrieveFileStream(source);
		if (inputStream == null) {
			throw new IOException("Failed to obtain InputStream for remote file " + source + ": " + this.client.getReplyCode());
		}
		return inputStream;
	}

	
	public boolean finalizeRaw() throws IOException {
		if (!this.readingRaw.compareAndSet(true, false)) {
			throw new IOException("Raw read is not in process");
		}
		if (this.client.completePendingCommand()) {
			int replyCode = this.client.getReplyCode();
			if (logger.isDebugEnabled()) {
				logger.debug(this + " finalizeRaw - reply code: " + replyCode);
			}
			return FTPReply.isPositiveCompletion(replyCode);
		}
		throw new IOException("completePendingCommandFailed");
	}

	
	public void write(InputStream inputStream, String path) throws IOException {
		Assert.notNull(inputStream, "inputStream must not be null");
		Assert.hasText(path, "path must not be null");
		boolean completed = this.client.storeFile(path, inputStream);
		if (!completed) {
			throw new IOException("Failed to write to '" + path
					+ "'. Server replied with: " + this.client.getReplyString());
		}
		if (logger.isInfoEnabled()) {
			logger.info("File has been successfully transfered to: " + path);
		}
	}

	
	public void close() {
		try {
			this.client.disconnect();
		}
		catch (Exception e) {
			if (logger.isWarnEnabled()) {
				logger.warn("failed to disconnect FTPClient", e);
			}
		}
	}

	
	public boolean isOpen() {
		try {
			this.client.noop();
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}

	
	public void rename(String pathFrom, String pathTo) throws IOException{
		this.client.deleteFile(pathTo);
		boolean completed = this.client.rename(pathFrom, pathTo);
		if (!completed) {
			throw new IOException("Failed to rename '" + pathFrom +
					"' to " + pathTo + "'. Server replied with: " + this.client.getReplyString());
		}
		if (logger.isInfoEnabled()) {
			logger.info("File has been successfully renamed from: " + pathFrom + " to " + pathTo);
		}
	}

	
	public boolean mkdir(String remoteDirectory) throws IOException {
		return this.client.makeDirectory(remoteDirectory);
	}

	
	public boolean exists(String path) throws IOException{
		Assert.hasText(path, "'path' must not be empty");

		String currentWorkingPath = this.client.printWorkingDirectory();
		Assert.state(currentWorkingPath != null, "working directory cannot be determined, therefore exists check can not be completed");
		boolean exists = false;

		try {
			if (this.client.changeWorkingDirectory(path)) {
				exists = true;
			}
		}
		finally {
			this.client.changeWorkingDirectory(currentWorkingPath);
		}

		return exists;
	}



	public boolean changeDir(String directory) throws IOException {
		return client.changeWorkingDirectory(directory);
	}



	public void downloadFromDir(Map<String,String> fileMap,List<File> fileList,String fileSort,List<ExtAttr> moveFileInfos,Integer num,String remoteDir, String localPath,
			String fileFormats,DownloadCallBack<FTPClient> callBack, String isScanSubDirStr) {
		
		try  
		{  
			client.changeWorkingDirectory(remoteDir);//转移到FTP服务器目录  
			FTPFile[] ftpList = client.listFiles();  
			FTPFile[] fs = fileSortByType(ftpList,fileSort);
			for (FTPFile ff : fs)  
			{  
				if(num==fileList.size()+fileMap.size()){
					break;
				}
				for(String key:fileMap.keySet()){
					String lostIngFile = fileMap.get(key);
					
					String saveFilePath = localPath+"/"+key;
					File localFile = new File(saveFilePath+".ing");
					OutputStream is = new FileOutputStream(localFile);  
					client.retrieveFile(lostIngFile, is);  
					is.close(); 
					localFile.renameTo(new File(saveFilePath));
					if(callBack!=null)
						callBack.doFile(client,lostIngFile, saveFilePath);
					fileList.add(new File(saveFilePath));
					fileMap.remove(key);
				}
				
				
				String fileName = ff.getName();
				
				//挑选出指定的文件，如果moveFileInfos为空，则搬迁所有文件
				if(moveFileInfos!=null&&moveFileInfos.size()>0){
					Boolean flag=true;															//默认当前文件不搬迁，true表示不搬迁当前文件，继续下一个文件，false表示搬迁
					for (ExtAttr temp : moveFileInfos) {
						if(fileName.equals(temp.getFileName())){			
							flag=false;														
						}
					}
					if(flag){
						continue;
					}
				}
				File localFile = null;
				try{
					if(".".equals(fileName)||"..".equals(fileName) || fileName.endsWith(".ing")){
						continue;
					}
					int fileType = ff.getType();
					if(FTPFile.DIRECTORY_TYPE==fileType && !"N".equals(isScanSubDirStr)){
						//创建本地目录
						File ffLocalDir = new File(localPath+"/"+fileName);
						ffLocalDir.mkdir();
						downloadFromDir(fileMap,fileList,fileSort,moveFileInfos,num,remoteDir+"/"+fileName,ffLocalDir.getAbsolutePath(),fileFormats,callBack, isScanSubDirStr);
						client.changeToParentDirectory();
					}else{
						String reDownloadFile = fileName+".ing";
						client.rename(fileName, reDownloadFile);
						
						fileMap.put(fileName, reDownloadFile);
						if(fileFormats!=null && !fileFormats.equals("")){
							fileName = FileFilterExpression.getFileName(fileName, fileFormats);	//对文件名称进行格式化
						}
						String saveFilePath = localPath+"/"+fileName;
						localFile = new File(saveFilePath+".ing");
						OutputStream is = new FileOutputStream(localFile);  
						client.retrieveFile(reDownloadFile, is);  
						is.close(); 
						localFile.renameTo(new File(saveFilePath));
						if(callBack!=null)
							callBack.doFile(client,reDownloadFile, saveFilePath);
						fileList.add(new File(saveFilePath));
						fileMap.remove(fileName);
						} 
				}catch (FileNotFoundException e) {  
					 if(logger.isInfoEnabled()){
						 logger.info("File name "+fileName+" is not found!");
					 }
					continue;
				}catch (OverlappingFileLockException  sException) {  
					 if(logger.isInfoEnabled()){
						 logger.info(fileName + "is Locked"); 
					 }
					 continue;
				}catch(Exception e){
					if(localFile!=null){
						FileUtil.deleteContents(localFile);
						throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN, ERROR_CODE_9999, fileName+" download exception!", e);
					}else{
						throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN, ERROR_CODE_9999, " download exception!", e);
					}
				}
			}
		}  
		catch (Exception e)  
		{  
			logger.error(LogModel.EVENT_APP_EXCPT, new BusinessException(9210, remoteDir+" dowload exception", e));
			throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN, ERROR_CODE_9999, remoteDir +"dowload exception!", e);

		}    
	}
	
	//文件排序算法
	private FTPFile[] fileSortByType(FTPFile[] ftpList, String sortType) {
		List<FTPFile> files = Arrays.asList(ftpList);
		// 按照文件从大到小排序
		if(sortType.equals(ORDERBY_LENGTH_DESC)){
			Collections.sort(files, new Comparator<FTPFile>() {
				public int compare(FTPFile f1, FTPFile f2) {
					long diff = f2.getSize() - f1.getSize();
					if (diff > 0)
						return 1;
					else if (diff == 0)
						return 0;
					else
						return -1;
				}
			});
		// 按照文件从小到大排序	
		}else if(sortType.equals(ORDERBY_LENGTH_ASC)){
			Collections.sort(files, new Comparator<FTPFile>() {
				public int compare(FTPFile f1, FTPFile f2) {
					long diff = f1.getSize() - f2.getSize();
					if (diff > 0)
						return 1;
					else if (diff == 0)
						return 0;
					else
						return -1;
				}
			});
		// 按照文件名称排序(0-9A-Za-z)	
		}else if(sortType.equals(ORDERBY_NAME_ASC)){
			Collections.sort(files, new Comparator<FTPFile>() {
				public int compare(FTPFile f1, FTPFile f2) {
					if (f1.isDirectory() && f2.isFile())
						return -1;
					if (f1.isFile() && f2.isDirectory())
						return 1;
					return f1.getName().compareTo(f2.getName());
				}
			});
			// 按照文件名称排序(z-aZ-A9-0)
		}else if(sortType.equals(ORDERBY_NAME_DESC)){
			Collections.sort(files, new Comparator<FTPFile>() {
				public int compare(FTPFile f1, FTPFile f2) {
					if (f1.isDirectory() && f2.isFile())
						return -1;
					if (f1.isFile() && f2.isDirectory())
						return 1;
					return f2.getName().compareTo(f1.getName());
				}
			});
		// 按修改日期降序排序	
		}else if(sortType.equals(ORDERBY_MODIFY_DATE_DESC)){
			Collections.sort(files, new Comparator<FTPFile>() {
				public int compare(FTPFile f1, FTPFile f2) {
					long diff = f2.getTimestamp().getTimeInMillis() - f1.getTimestamp().getTimeInMillis() ;
					if (diff > 0)
						return 1;
					else if (diff == 0)
						return 0;
					else
						return -1;
				}
			});
		//按修改时间升序排序
		}else if(sortType.equals(ORDERBY_MODIFY_DATE_ASC)){
			Collections.sort(files, new Comparator<FTPFile>() {
				public int compare(FTPFile f1, FTPFile f2) {
					long diff = f1.getTimestamp().getTimeInMillis() - f2.getTimestamp().getTimeInMillis() ;
					if (diff > 0)
						return 1;
					else if (diff == 0)
						return 0;
					else
						return -1;
				}
			});
		// 按文件类型升序排序		
		}else if(sortType.equals(ORDERBY_FILE_TYPE_ASC)){

			Collections.sort(files, new Comparator<FTPFile>() {
				public int compare(FTPFile f1, FTPFile f2) {
					if (f1.isDirectory() && f2.isFile())
						return -1;
					if (f1.isFile() && f2.isDirectory())
						return 1;
					String suffix1 = "";
					String suffix2 = "";
					int flag = f1.getName().lastIndexOf(".");
					if(flag != -1) {
						suffix1 = f1.getName().substring(flag+1,f1.getName().length());	//获取后缀名
					}
					flag = f2.getName().lastIndexOf(".");
					if(flag != -1) {
						suffix2 = f2.getName().substring(flag+1,f2.getName().length());	//获取后缀名
					}
					return suffix1.compareTo(suffix2);
				}
			});
		// 按文件类型降序排序	
		}else if(sortType.equals(ORDERBY_FILE_TYPE_DESC)){

			Collections.sort(files, new Comparator<FTPFile>() {
				public int compare(FTPFile f1, FTPFile f2) {
					if (f1.isDirectory() && f2.isFile())
						return -1;
					if (f1.isFile() && f2.isDirectory())
						return 1;
					String suffix1 = "";
					String suffix2 = "";
					int flag = f1.getName().lastIndexOf(".");
					if(flag != -1) {
						suffix1 = f1.getName().substring(flag+1,f1.getName().length());	//获取后缀名
					}
					flag = f2.getName().lastIndexOf(".");
					if(flag != -1) {
						suffix2 = f2.getName().substring(flag+1,f2.getName().length());	//获取后缀名
					}
					return suffix2.compareTo(suffix1);
				}
			});
		}
		
		FTPFile[] fileArray = (FTPFile[])files.toArray(new FTPFile[files.size()]);
		return fileArray;
	}



	public void downloadFromDir(Map<String,String> fileMap,List<File> fileList,String fileSort,List<ExtAttr> moveFileInfos,Integer num,String remoteDir, String localPath,
			 String filterExpression,String fileFormats, DownloadCallBack<FTPClient> callBack, String isScanSubDirStr)
			throws IOException {
		try  
		{  
			client.changeWorkingDirectory(remoteDir);//转移到FTP服务器目录  
			filterExpression = FileFilterExpression.validateExpression(filterExpression);
			FTPFile[] ftpList = this.listFiles(remoteDir, filterExpression);
			FTPFile[] fs = fileSortByType(ftpList,"");
			for (FTPFile ff : fs)  
			{  
				if(num==fileList.size()+fileMap.size()){
					break;
				}
				
				for(String key:fileMap.keySet()){
					String lostIngFile = fileMap.get(key);
					
					String saveFilePath = localPath+"/"+key;
					File localFile = new File(saveFilePath+".ing");
					OutputStream is = new FileOutputStream(localFile);  
					client.retrieveFile(lostIngFile, is);  
					is.close(); 
					localFile.renameTo(new File(saveFilePath));
					if(callBack!=null)
						callBack.doFile(client,lostIngFile, saveFilePath);
					fileList.add(new File(saveFilePath));
					fileMap.remove(key);
				}
				
				String fileName = ff.getName();
				
				//挑选出指定的文件，如果moveFileInfos为空，则搬迁所有文件
				if(moveFileInfos!=null&&moveFileInfos.size()>0){
					Boolean flag=true;															//默认当前文件不搬迁，true表示不搬迁当前文件，继续下一个文件，false表示搬迁
					for (ExtAttr temp : moveFileInfos) {
						if(fileName.equals(temp.getFileName())){			
							flag=false;														
						}
					}
					if(flag){
						continue;
					}
				}
				
				File localFile = null;
				try{
					if(".".equals(fileName)||"..".equals(fileName) || fileName.endsWith(".ing")){
						continue;
					}
					int fileType = ff.getType();
					if(FTPFile.DIRECTORY_TYPE==fileType){
						//创建本地目录
						File ffLocalDir = new File(localPath+"/"+fileName);
						ffLocalDir.mkdir();
						downloadFromDir(fileMap,fileList,fileSort,moveFileInfos,num,remoteDir+"/"+fileName,ffLocalDir.getAbsolutePath(),filterExpression,fileFormats,callBack, isScanSubDirStr);
						client.changeToParentDirectory();
					}else{
						String reDownloadFile = fileName+".ing";
						client.rename(fileName, reDownloadFile);
						fileMap.put(fileName, reDownloadFile);
						if(fileFormats!=null && !fileFormats.equals("")){
							fileName = FileFilterExpression.getFileName(fileName, fileFormats);	//对文件名称进行格式化
						}
						String saveFilePath = localPath+"/"+fileName;
						localFile = new File(saveFilePath+".ing");
						OutputStream is = new FileOutputStream(localFile);  
						client.retrieveFile(reDownloadFile, is);  
						is.close(); 
						localFile.renameTo(new File(saveFilePath));
						if(callBack!=null)
							callBack.doFile(client,reDownloadFile, saveFilePath);
						fileList.add(new File(saveFilePath));
						fileMap.remove(fileName);
					}
				}catch (FileNotFoundException e) {  
					 if(logger.isInfoEnabled()){
						 logger.info("File name "+fileName+" is not found!");
					 }
					continue;
				}catch (OverlappingFileLockException  sException) {  
					 if(logger.isInfoEnabled()){
						 logger.info(fileName + "is Locked"); 
					 }
					 continue;
				}catch(Exception e){
					if(localFile!=null){
						FileUtil.deleteContents(localFile);
						throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN, ERROR_CODE_9999, fileName+" download exception!", e);
					}else{
						throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN, ERROR_CODE_9999, " download exception!", e);
					}
				}
			}  
		} catch (IOException e)  
		{  
			logger.error(LogModel.EVENT_APP_EXCPT, new BusinessException(9210, remoteDir+" dowload exception", e));
			throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN, ERROR_CODE_9999, remoteDir+" dowload exception", e);

		}    
	}
	
	public void upload(String directory, String uploadFile) throws IOException {
		client.changeWorkingDirectory(directory);
		File file = new File(uploadFile);
		if(file.isDirectory()){           
			client.makeDirectory(file.getAbsolutePath());                
			String[] files = file.list();             
			for (int i = 0; i < files.length; i++) {
				File file1 = new File(file.getPath()+"/"+files[i] );    
				if(!file1.exists()||".".equals(file1.getName())||"..".equals(file1.getName()) || ".ing".endsWith(file1.getName()))
					continue;
				if(file1.isDirectory()){      
					upload(directory+"/"+file.getName(),file1.getAbsolutePath());         
					client.changeToParentDirectory();      
				}else{
					FileInputStream input =null;
					try{
						File file2 = new File(file.getPath()+"/"+files[i]);      
						input = new FileInputStream(file2);      
						client.storeFile(file2.getName()+".ing", input);      
						rename(file2.getName()+".ing", file2.getName());
						input.close();     						
					}finally{
						if(input!=null){
							input.close();  
						}						
					}
                       
				}                 
			}      
		}else{    
			String[] dir = directory.split("/");
			StringBuffer temp = new StringBuffer("");
			for(String d:dir){
				temp.append(d).append("/");
				if(client.makeDirectory(temp.toString()));
			}
			FileInputStream input=null;
			try{
				File file2 = new File(file.getPath());      
				input = new FileInputStream(file2);      
				temp.append("/").append(file2.getName());
				client.storeFile(temp.toString(), input);      
			}finally{
				if(input!=null){
					input.close();  
				}
			}

		}	
	}
	
	public FTPFile[] listFiles(String path, final String filterExpression) throws IOException {
		Assert.hasText(path, "path must not be null");
		return this.client.listFiles(path,new FTPFileFilter() {
			
			public boolean accept(FTPFile file) {
				return FileFilterExpression.validateFileName(file.getName(), filterExpression);
			}
		});
	}

	public FTPFile[] list(String path, final String filterPattern) throws IOException {
		Assert.hasText(path, "path must not be null");
		return this.client.listFiles(path,new FTPFileFilter() {
			
			public boolean accept(FTPFile file) {
				String name = file.getName();
				Pattern p = Pattern.compile(filterPattern);
				Matcher m = p.matcher(name);
				if(m.find()){
					return true;
				}else 
					return false;
			}
		});
	}
}
