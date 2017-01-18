package com.ailk.eaap.o2p.common.hide;
/**
 * 截断类型说明
 * @author MAWL
 *
 */
public enum HideSplitType {

	/**
	 * 带截断处理
	 */
	IS_SPLIT("0"),
	/**
	 * 不带截断处理
	 */
	NO_SPLIT("1");
	private String type;
	private HideSplitType(String type){
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String toString(){
		return this.getType();
	}
}
