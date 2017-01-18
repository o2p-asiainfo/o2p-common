package com.ailk.eaap.o2p.util.file;

/**
 * for O2P File exchanging exception declare
 * @author 颖勤
 * @since 2015-06-24
 */
public class FileHandleException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public FileHandleException(String msg){
		super(msg);
	}
	public FileHandleException(String msg,Throwable cause){
		super(msg,cause);
	}

}
