package com.ailk.eaap.op2.common;

import java.io.Serializable;

public class Response  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8113557907219996992L;
	private String resptype;
	private String respcode;
	private String respDesc;
	public Response(){
		
	}
	
	public String getResptype() {
		return resptype;
	}
	public void setResptype(String resptype) {
		this.resptype = resptype;
	}
	public String getRespcode() {
		return respcode;
	}
	public void setRespcode(String respcode) {
		this.respcode = respcode;
	}
	public String getRespDesc() {
		return respDesc;
	}
	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}
	
}
