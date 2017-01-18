package com.ailk.eaap.o2p.common.hide;
/**
 * 模糊处理类型常量类
 * @author MAWL
 *
 */
public enum HideType {

	/**
	 * 自定义模糊处理类型
	 */
	DEPLOY_RULE("1"),
	/**
	 * 对姓名或地址的默认处理类型
	 */
	HIDDEN_NAME_OR_ADDRESS("2"),
	/**
	 * 对手机的默认处理类型
	 */
	HIDDEN_MOBILE("3"),
	/**
	 * 对用户身份证、税务号等证件信息模糊处理类型
	 */
	HIDDEN_ID_CARD("4"),
	/**
	 * 隐私数据进行不可逆的加密处理类型
	 */
	UN_SYMMETRIC_ENCODER("5"),
	/**
	 * 隐私数据进行不可逆的加密处理类型
	 */
	SYMMETRIC_ENCODER("6");
	private String type;
	private HideType(String type){
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
