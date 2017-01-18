package com.ailk.eaap.o2p.util.file.remote.session.sftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.OverlappingFileLockException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.util.FileUtil;
import org.springframework.core.NestedIOException;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;

import com.ailk.eaap.o2p.common.util.Constant;
import com.ailk.eaap.o2p.util.file.filters.FileFilterExpression;
import com.ailk.eaap.o2p.util.file.remote.session.DownloadCallBack;
import com.ailk.eaap.o2p.util.file.remote.session.Session;
import com.ailk.eaap.o2p.util.file.remote.session.model.ExtAttr;
import com.ailk.eaap.op2.common.EAAPException;
import com.ailk.eaap.op2.common.EAAPTags;
import com.asiainfo.foundation.exception.BusinessException;
import com.asiainfo.foundation.log.LogModel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.ChannelSftp.LsEntrySelector;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;


public class SftpSession implements Session<LsEntry,ChannelSftp>{
	private static final Log LOG = LogFactory.getLog(SftpSession.class);

	private final com.jcraft.jsch.Session jschSession;

	private final JSchSessionWrapper wrapper;

	private  volatile ChannelSftp channel;

	private volatile boolean closed;
	
	public SftpSession(com.jcraft.jsch.Session jschSession) {
		Assert.notNull(jschSession, "jschSession must not be null");
		this.jschSession = jschSession;
		this.wrapper = null;
	}

	public SftpSession(JSchSessionWrapper wrapper) {
		Assert.notNull(wrapper, "wrapper must not be null");
		this.jschSession = wrapper.getSession();
		this.wrapper = wrapper;
	}

	
	public boolean remove(String path) throws IOException {
		Assert.state(this.channel != null, "session is not connected");
		try {
			this.channel.rm(path);
			return true;
		}
		catch (SftpException e) {
			LOG.error("Failed to remove file: ", e);
			throw new NestedIOException("Failed to remove file: "+ e);
		}
	}

	
	public LsEntry[] list(String path) throws IOException {
		Assert.state(this.channel != null, "session is not connected");
		try {
			Vector<?> lsEntries = this.channel.ls(path);
			if (lsEntries != null) {
				LsEntry[] entries = new LsEntry[lsEntries.size()];
				for (int i = 0; i < lsEntries.size(); i++) {
					Object next = lsEntries.get(i);
					Assert.state(next instanceof LsEntry, "expected only LsEntry instances from channel.ls()");
					entries[i] = (LsEntry) next;
				}
				return entries;
			}
		}
		catch (SftpException e) {
			LOG.error("Failed to list files: ", e);
			throw new NestedIOException("Failed to list files", e);
		}
		return new LsEntry[0];
	}

	
	public String[] listNames(String path) throws IOException {
		LsEntry[] entries = this.list(path);
		List<String> names = new ArrayList<String>();
		for (int i = 0; i < entries.length; i++) {
			String fileName = entries[i].getFilename();
			SftpATTRS attrs = entries[i].getAttrs();
			if (!attrs.isDir() && !attrs.isLink()) {
				names.add(fileName);
			}
		}
		String[] fileNames = new String[names.size()];
		return names.toArray(fileNames);
	}


	
	public void read(String source, OutputStream os) throws IOException {
		Assert.state(this.channel != null, "session is not connected");
		try {
			InputStream is = this.channel.get(source);
			FileCopyUtils.copy(is, os);
		}
		catch (SftpException e) {
			LOG.error("failed to read file: ", e);
			throw new NestedIOException("failed to read file " + source, e);
		}
	}

	
	public InputStream readRaw(String source) throws IOException {
		try {
			return this.channel.get(source);
		}
		catch (SftpException e) {
			LOG.error("failed to read file: ", e);
			throw new NestedIOException("failed to read file " + source, e);
		}
	}

	
	public boolean finalizeRaw() throws IOException {
		return true;
	}

	
	public void write(InputStream inputStream, String destination) throws IOException {
		Assert.state(this.channel != null, "session is not connected");
		try {
			this.channel.put(inputStream, destination);
		}
		catch (SftpException e) {
			LOG.error("failed to write file: ", e);
			throw new NestedIOException("failed to write file", e);
		}
	}

	
	public void close() {
		this.closed = true;
		if (this.wrapper != null) {
			if (this.channel != null) {
				this.channel.disconnect();
			}
			this.wrapper.close();
		}
		else {
			if (this.jschSession.isConnected()) {
				this.jschSession.disconnect();
			}
		}
	}

	
	public boolean isOpen() {
		return !this.closed && this.jschSession.isConnected();
	}

	
	public void rename(String pathFrom, String pathTo) throws IOException {
		try {
			this.channel.rename(pathFrom, pathTo);
		}
		catch (SftpException sftpex) {
			if (LOG.isDebugEnabled()){
				LOG.debug("Initial File rename failed, possibly because file already exists. Will attempt to delete file: "
						+ pathTo + " and execute rename again.");
			}
			try {
				this.remove(pathTo);
				if (LOG.isDebugEnabled()) {
					LOG.debug("Delete file: " + pathTo + " succeeded. Will attempt rename again");
				}
			}
			catch (IOException ioex) {
				LOG.error("Failed to delete file " + pathTo, ioex);
				throw new NestedIOException("Failed to delete file " + pathTo, ioex);
			}
			try {
				// attempt to rename again
				this.channel.rename(pathFrom, pathTo);
			}
			catch (SftpException sftpex2) {
				LOG.error("failed to rename from " + pathFrom + " to " + pathTo, sftpex2);
				throw new NestedIOException("failed to rename from " + pathFrom + " to " + pathTo, sftpex2);
			}
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("File: " + pathFrom + " was successfully renamed to " + pathTo);
		}
	}

	
	public boolean mkdir(String remoteDirectory) throws IOException {
		try {
			this.channel.mkdir(remoteDirectory);
		}
		catch (SftpException e) {
			LOG.error("failed to create remote directory '" + remoteDirectory + "'.", e);
			throw new NestedIOException("failed to create remote directory '" + remoteDirectory + "'.", e);
		}
		return true;
	}

	
	public boolean exists(String path) {
		try {
			this.channel.lstat(path);
			return true;
		}
		catch (SftpException e) {
			LOG.debug("the file path: " + path + ", not exists, will create");
		}
		return false;
	}

	public void connect(Integer timeout) {
		
		if(LOG.isDebugEnabled()) {
			
			LOG.debug("====> timeout:"+timeout/1000+"s");
		}
		try {
			if (!this.jschSession.isConnected()) {
				this.jschSession.connect(timeout);
			}
			this.channel = (ChannelSftp) this.jschSession.openChannel("sftp");
			if (this.channel != null && !this.channel.isConnected()) {
				this.channel.connect();
			}
		}
		catch (JSchException e) {
			this.close();
			LOG.error("failed to connect", e);
			throw new IllegalStateException("failed to connect", e);
		}
	}

	public boolean changeDir(String directory) throws IOException {
		try {
			this.channel.cd(directory);
		}
		catch (SftpException e) {
			LOG.error("failed to create open directory '" + directory + "'.", e);
			throw new NestedIOException("failed to create open directory '" + directory + "'.", e);
		}
		return true;
	}

	public void downloadFromDir(Map<String,String> fileMap,List<File> fileList,String fileSort,List<ExtAttr> moveFileInfos,Integer num,String remoteDir, String localPath,
			String fileFormats,DownloadCallBack<ChannelSftp> callBack, String isScanSubDirStr) throws IOException {
		
		this.downloadFromDir(fileMap, fileList, fileSort, moveFileInfos, num, remoteDir, localPath, null, fileFormats, callBack,isScanSubDirStr);
	}

	private Vector<Object> ls(String remoteDir) throws SftpException {

		final java.util.Vector v = new Vector();
	     LsEntrySelector selector = new LsEntrySelector(){
	       int count = 100;
	       public int select(LsEntry entry){
	    	   
	    	   if(count-- > 0) {
	    		   
	    		   	v.addElement(entry);
	  	         	return CONTINUE;
	    	   } else {
	    		   
	    		   	return BREAK;
	    	   }
	       }
	     };
	     channel.ls(remoteDir, selector);
	     return v;
	}

	public void downloadFromDir(Map<String,String> fileMap,List<File> fileList,String fileSort,List<ExtAttr> moveFileInfos,Integer num,String remoteDir, String localPath,
			String filterExpression,String fileFormats, DownloadCallBack<ChannelSftp> callBack, String isScanSubDirStr)
			throws IOException {
		
		if(LOG.isDebugEnabled()) {
			
			LOG.debug("=========> download file size:"+fileList.size());
		}
		List<Object> list = null;
		List<Object> currentList = new ArrayList<Object>();
		try{
			if(!StringUtils.isEmpty(filterExpression)) {
				
				filterExpression = FileFilterExpression.validateExpression(filterExpression);
				list = Arrays.asList(this.listFiles(remoteDir,filterExpression));
			} else {
				
				list = this.ls(remoteDir);
			}
			
			if(LOG.isDebugEnabled()) {
				
				LOG.debug("==========> remoteDir:"+remoteDir+", file number is:"+list.size());
			}
			channel.cd(remoteDir);
			if(list.size() <= 0 || isNoFile(list)) {
				if(LOG.isDebugEnabled()) {
					
					LOG.debug("==========> no file return");
				}
				
				return;
			}
			List<Object> le = fileSortByType(list,fileSort);
			
			boolean isBreak = false;
			for (int i = 0; i < le.size(); i++) {
				LsEntry lsEntry = (LsEntry)le.get(i);
				SftpATTRS fileAttrs = lsEntry.getAttrs();
				if(fileAttrs.getSize()>0){
					if(num==fileList.size()+fileMap.size()){
						isBreak = true;
						break;
					}
					
					//先执行遗留.ing文件
					for(String key:fileMap.keySet()){
						
						String lostIngFile = fileMap.get(key);
						
						String saveFilePath = localPath+"/"+key;
						File file = new File(saveFilePath+".ing");
						FileOutputStream fio =  new FileOutputStream(file);
//						channel.get(lostIngFile,fio);
						getFile(lostIngFile, fio);
						
						file.renameTo(new File(saveFilePath));	
						if(callBack!=null)
							callBack.doFile(channel,lostIngFile, saveFilePath);
						fileList.add(new File(saveFilePath));	
						fileMap.remove(key);
					}
					
					String downloadFile = lsEntry.getFilename();
					
					//挑选出指定的文件，如果fileInfos为空，则搬迁所有文件
					if(moveFileInfos!=null&&moveFileInfos.size()>0){
						Boolean flag=true;															//默认当前文件不搬迁，true表示不搬迁当前文件，继续下一个文件，false表示搬迁
						for (ExtAttr temp : moveFileInfos) {
							if(downloadFile.equals(temp.getFileName())){			
								flag=false;														
							}
						}
						if(flag){
							continue;
						}
					}

					File file = null;
					try{
						if(".".equals(downloadFile)||"..".equals(downloadFile)  || downloadFile.endsWith(".ing")){
							continue;
						}
						boolean isDir = fileAttrs.isDir();
						if(isDir){
							
							if(!"N".equals(isScanSubDirStr)) {
								
								//创建本地目录
								File ffLocalDir = new File(localPath+"/"+downloadFile);
								ffLocalDir.mkdir();
								downloadFromDir(fileMap,fileList,fileSort,moveFileInfos,num,remoteDir+"/"+downloadFile,ffLocalDir.getAbsolutePath(),filterExpression,fileFormats,callBack, isScanSubDirStr);
								channel.cd("..");
							}
						}else{
							if(fileFormats!=null && !fileFormats.equals("")){
								downloadFile = FileFilterExpression.getFileName(downloadFile, fileFormats);	//瀵规枃浠跺悕绉拌繘琛屾牸寮忓寲
							}
							String saveFilePath = localPath+"/"+downloadFile;
							String reDownloadFile = downloadFile+".ing";
							channel.rename(downloadFile, reDownloadFile);
							file = new File(saveFilePath+".ing");
							fileMap.put(downloadFile, reDownloadFile);
							
							FileOutputStream fio =  new FileOutputStream(file);
							getFile(reDownloadFile, fio);
							file.renameTo(new File(saveFilePath));	
							if(callBack!=null)
								callBack.doFile(channel,reDownloadFile, saveFilePath);
							fileList.add(new File(saveFilePath));	
							fileMap.remove(downloadFile);
							
							currentList.add(saveFilePath);
						}
					}catch (SftpException  sException) {  
						 if(ChannelSftp.SSH_FX_NO_SUCH_FILE == sException.id){
							 if(LOG.isInfoEnabled()){
								 LOG.info("File name "+downloadFile+" is not found!");
							 }
							 continue;
						 }else{
							 //下载失败，删除ing文件
							 if(file!=null){
								 FileUtil.deleteContents(file);
								 throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN, ERROR_CODE_9999, downloadFile+" download exception!", sException);
							 }else{
								 throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN, ERROR_CODE_9999, " download exception!", sException);
							 }
						 }
					}catch (OverlappingFileLockException  sException) {  
						 if(LOG.isInfoEnabled()){
							 LOG.info(downloadFile + "is Locked"); 
						 }
						 continue;
					}
				}
			}
			
			if(list.size() > 0 && !isBreak && fileList.size()>0 && currentList.size()>0) {
				
				downloadFromDir(fileMap, fileList, fileSort, moveFileInfos, num, remoteDir,  localPath,filterExpression, fileFormats,callBack, isScanSubDirStr);
			}
		}
		catch (Exception e)  
		{  
			LOG.error(LogModel.EVENT_APP_EXCPT, new BusinessException(9210, remoteDir+ " dowload exception", e));
			throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN, ERROR_CODE_9999, remoteDir+" download exception!", e);
		}    
	}
	
	
	private boolean isNoFile(List<Object> list) {

		for(Object obj : list) {
			
			LsEntry lsEntry = (LsEntry)obj;
			String fileName = lsEntry.getFilename();
			if(!(".".equals(fileName) || "..".equals(fileName))) {
				
				return false;
			}
		}
		return true;
	}
	private void getFile(String reDownloadFile, FileOutputStream fio)
			throws SftpException, IOException {
		InputStream is = channel.get(reDownloadFile);
		byte[] buff = new byte[1024 * 2];
		int read;
		if (is != null) {
		    do {
		        read = is.read(buff, 0, buff.length);
		        if (read > 0) {
		        	fio.write(buff, 0, read);
		        }
		        fio.flush();
		    } while (read >= 0);
		}
		fio.close();
	}
	
	public void upload(String directory, String uploadFile) throws IOException {
		try {
			if(exists(directory))
				channel.cd(directory);
			else
			{
				StringBuffer temp = new StringBuffer("/");
				String[] dirs = directory.split("/"); 
				for(String d:dirs){
					temp.append(d).append("/");
					if(!exists(temp.toString())){
						channel.mkdir(temp.toString());
					}
				}
				channel.cd(directory);
			}
			File file = new File(uploadFile);
			if(file.isDirectory()){
				if(exists(file.getName()))
					channel.cd(file.getName());
				else{
					channel.mkdir(file.getName());
					channel.cd(file.getName());
				}
				String[] files = file.list();             
				for (int i = 0; i < files.length; i++) {      
					File file1 = new File(file.getPath()+"/"+files[i] );      
					if(!file1.exists()||".".equals(file1.getName())||"..".equals(file1.getName()) || ".ing".endsWith(file1.getName()))
						continue;
					if(file1.isDirectory()){      
						upload(directory+"/"+file.getName(),file1.getAbsolutePath());      
						channel.cd("..");   
					}else{                    
						//如果要上传的文件已经存在，则需要先删除
						FileInputStream fis = new FileInputStream(file1);
						channel.put(fis, file1.getName()+".ing");
						fis.close();                           
						rename(file1.getName()+".ing", file1.getName());
					}                 
				} 
			}else{
				//如果要上传的文件已经存在，则需要先删除
				if(LOG.isInfoEnabled())
					LOG.debug("ready to upload ,file="+file.getAbsolutePath());
				FileInputStream fis = new FileInputStream(file);
				channel.put(fis, file.getName()+".ing");
				fis.close();
				rename(file.getName()+".ing",file.getName());
			}
		} catch (Exception e) {
			LOG.error(LogModel.EVENT_APP_EXCPT, new BusinessException(9210, "upload exception", e));
			throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN, ERROR_CODE_9999, "upload exception!", e);
		}
	}
	
	public Object[] listFiles(String path, final String filterExpression) throws IOException {
		Assert.hasText(path, "path must not be null");
		Vector<LsEntry> entriesList = new Vector<LsEntry>();
		try {
			Vector<LsEntry> lsEntries = this.channel.ls(path);
			LsEntry entry = null;
			if (lsEntries != null) {
				for (int i = 0; i < lsEntries.size(); i++) {
					entry = lsEntries.get(i);
					if(FileFilterExpression.validateFileName(entry.getFilename(), filterExpression)) {
						entriesList.add(entry);
					}
				}
				return entriesList.toArray();
			}
		} catch (SftpException e) {
			LOG.error("Failed to list files", e);
			throw new NestedIOException("Failed to list files", e);
		}
		return entriesList.toArray();
	}

	public LsEntry[] list(String path, String filterPattern) throws IOException {
		Assert.state(this.channel != null, "session is not connected");
		try {
			Vector<?> lsEntries = this.channel.ls(path);
			if (lsEntries != null) {
				LsEntry[] entries = new LsEntry[lsEntries.size()];
				for (int i = 0; i < lsEntries.size(); i++) {
					Object next = lsEntries.get(i);
					Assert.state(next instanceof LsEntry, "expected only LsEntry instances from channel.ls()");
					entries[i] = (LsEntry) next;
				}
				return entries;
			}
		}
		catch (SftpException e) {
			LOG.error("Failed to list files", e);
			throw new NestedIOException("Failed to list files", e);
		}
		return new LsEntry[0];
	}
	
	//文件排序算法
		private List<Object> fileSortByType(List<Object> list, String sortType) {
			// 按照文件从大到小排序
			if(sortType.equals(ORDERBY_LENGTH_DESC)){
				Collections.sort(list, new Comparator<Object>() {
					public int compare(Object obj1, Object obj2) {
						LsEntry f1 = (LsEntry)obj1;
						LsEntry f2 = (LsEntry)obj2;
						long diff = f2.getAttrs().getSize() - f1.getAttrs().getSize(); 
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
				Collections.sort(list, new Comparator<Object>() {
					public int compare(Object obj1, Object obj2) {
						LsEntry f1 = (LsEntry)obj1;
						LsEntry f2 = (LsEntry)obj2;
						long diff = f1.getAttrs().getSize()  - f2.getAttrs().getSize() ;
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
				Collections.sort(list, new Comparator<Object>() {
					public int compare(Object obj1, Object obj2) {
						LsEntry f1 = (LsEntry)obj1;
						LsEntry f2 = (LsEntry)obj2;
						if (f1.getAttrs().isDir() && f2.getAttrs().isFifo())
							return -1;
						if (f1.getAttrs().isFifo() && f2.getAttrs().isDir())
							return 1;
						return f1.getFilename().compareTo(f2.getFilename());
					}
				});
				// 按照文件名称排序(z-aZ-A9-0)
			}else if(sortType.equals(ORDERBY_NAME_DESC)){
				Collections.sort(list, new Comparator<Object>() {
					public int compare(Object obj1, Object obj2) {
						LsEntry f1 = (LsEntry)obj1;
						LsEntry f2 = (LsEntry)obj2;
						if (f1.getAttrs().isDir() && f2.getAttrs().isFifo())
							return -1;
						if (f1.getAttrs().isFifo() && f2.getAttrs().isDir())
							return 1;
						return f2.getFilename().compareTo(f1.getFilename());
					}
				});
			// 按日期降序排序	
			}else if(sortType.equals(ORDERBY_MODIFY_DATE_DESC)){
				Collections.sort(list, new Comparator<Object>() {
					public int compare(Object obj1, Object obj2) {
						LsEntry f1 = (LsEntry)obj1;
						LsEntry f2 = (LsEntry)obj2;
						long diff = f2.getAttrs().getMTime() - f1.getAttrs().getMTime() ;
						if (diff > 0)
							return 1;
						else if (diff == 0)
							return 0;
						else
							return -1;
					}
				});
		// 按日期升序排序	
		}else if(sortType.equals(ORDERBY_MODIFY_DATE_ASC)){
			Collections.sort(list, new Comparator<Object>() {
				public int compare(Object obj1, Object obj2) {
					LsEntry f1 = (LsEntry)obj1;
					LsEntry f2 = (LsEntry)obj2;
					long diff = f1.getAttrs().getMTime() - f2.getAttrs().getMTime() ;
					if (diff > 0)
						return 1;
					else if (diff == 0)
						return 0;
					else
						return -1;
				}
			});
		// 按文件类型降序排序	
		}else if(sortType.equals(ORDERBY_FILE_TYPE_DESC)){
			Collections.sort(list, new Comparator<Object>() {
				public int compare(Object obj1, Object obj2) {
					LsEntry f1 = (LsEntry)obj1;
					LsEntry f2 = (LsEntry)obj2;
					String suffix1 = "";
					String suffix2 = "";
					int flag = f1.getFilename().lastIndexOf(".");
					if(flag != -1) {
						suffix1 = f1.getFilename().substring(flag+1,f1.getFilename().length());	//获取后缀名
					}
					flag = f2.getFilename().lastIndexOf(".");
					if(flag != -1) {
						suffix2 = f2.getFilename().substring(flag+1,f2.getFilename().length());	//获取后缀名
					}
					return suffix2.compareTo(suffix1);
				}
			});
	// 按文件类型升序排序	
	}else if(sortType.equals(ORDERBY_FILE_TYPE_ASC)){
		Collections.sort(list, new Comparator<Object>() {
			public int compare(Object obj1, Object obj2) {
				LsEntry f1 = (LsEntry)obj1;
				LsEntry f2 = (LsEntry)obj2;
				String suffix1 = "";
				String suffix2 = "";
				int flag = f1.getFilename().lastIndexOf(".");
				if(flag != -1) {
					suffix1 = f1.getFilename().substring(flag+1,f1.getFilename().length());	//获取后缀名
				}
				flag = f2.getFilename().lastIndexOf(".");
				if(flag != -1) {
					suffix2 = f2.getFilename().substring(flag+1,f2.getFilename().length());	//获取后缀名
				}
				return suffix1.compareTo(suffix2);
			}
		});
	}
			
			return list;
		}
	
}
