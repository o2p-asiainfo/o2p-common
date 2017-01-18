package com.ailk.eaap.op2.common;

 
import java.io.PrintWriter;
import java.io.StringWriter;

import com.asiainfo.foundation.exception.BusinessException;
import com.asiainfo.foundation.util.ExceptionUtils;


/**
 * 
 * 异常通用类<br>
 * EAAP异常通用类
 * <p>
 * @version 1.0
 * @author laozhu Jan 30, 2013
 * <hr>
 * 修改记录
 * <hr>
 * 1、修改人员:wuwz     修改时间:2014-10-28<br>       
 *    修改内容:继承BusinessException
 * <hr>
 */
public class EAAPException extends BusinessException {

	private static final long serialVersionUID = 1L;
	
	private String code;

	private String msg;
	 
	private String segmentSign;

	private String stackTraceInfo;

	public EAAPException(String segmentSign, String code, String msg) {
		super(Integer.parseInt(code), "segmentSign:"+segmentSign+"."+msg);
		this.segmentSign = segmentSign;
		this.code = code;
		this.msg = msg;
		this.stackTraceInfo = "";
	}

	public EAAPException(String segmentSign, String code, String msg, Exception e) {
		super(Integer.parseInt(code), "segmentSign:"+segmentSign+"."+msg, e);
		this.segmentSign = segmentSign;
		this.code = code;
		this.msg = msg;
		this.stackTraceInfo = ExceptionUtils.populateExecption(e);
	}

	public EAAPException(String segmentSign, String code, String msg,
			String stackTraceInfo) {
		super(Integer.parseInt(code), "segmentSign:"+segmentSign+"."+msg+".stackTraceInfo:"+stackTraceInfo);
		this.segmentSign = segmentSign;
		this.code = code;
		this.msg = msg;
		this.stackTraceInfo = stackTraceInfo;
	}


	/**
	 * 获取异常所有详细信息
	 * 
	 * @return
	 */
	public String getAllDetailMsg() {
		String segName=null;
		if(segmentSign.equals(EAAPTags.SEG_ORGREG_SIGN)){
			segName = EAAPTags.SEG_ORGREG_NAME;
		}else if(segmentSign.equals(EAAPTags.SEG_USERLOGIN_SIGN)){
			segName = EAAPTags.SEG_USERLOGIN_NAME;
		}else if(segmentSign.equals(EAAPTags.SEG_DEVAPP_SIGN)){
			segName = EAAPTags.SEG_DEVAPP_NAME;
		}else if(segmentSign.equals(EAAPTags.SEG_DRAVER_SIGN)) {
			segName = EAAPTags.SEG_DRAVER_NAME;
		}
		return "module:"+ segName+"|errorCode:" + code + "|errorMsg:" + msg + "|errorMsgDesc:" + stackTraceInfo;
	}
	public String getSegmentSign() {
		return segmentSign;
	}

	public void setSegmentSign(String segmentSign) {
		this.segmentSign = segmentSign;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getmsg() {
		return msg;
	}

	public void setmsg(String msg) {
		this.msg = msg;
	}

	public String getStackTraceInfo() {
		return stackTraceInfo;
	}

	public void setStackTraceInfo(String stackTraceInfo) {
		this.stackTraceInfo = stackTraceInfo;
	}

/*	public static void memcacheException(Exception e) throws EAAPException{
		
		if(e instanceof TimeoutException){
		
			throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN,ErrorDomain.ERROR_CODE_9999,ErrorDomain.connect_memcache_timeout,e);
		}
		else if(e instanceof InterruptedException){
		
			throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN,ErrorDomain.ERROR_CODE_9999,ErrorDomain.memcache_thread_interrupt,e);
		}
		else if(e instanceof MemcachedException){
		
			throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN,ErrorDomain.ERROR_CODE_9999,ErrorDomain.memcache_self_error,e);
		}else if (e instanceof EAAPException){
		
			throw (EAAPException)e;
		}else{
			
			throw new EAAPException(EAAPTags.SEG_DRAVER_SIGN,ErrorDomain.ERROR_CODE_9999,ErrorDomain.system_error,e);
		}
	}*/
}
