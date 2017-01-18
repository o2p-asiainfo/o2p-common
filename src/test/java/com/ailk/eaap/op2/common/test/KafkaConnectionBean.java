package com.ailk.eaap.op2.common.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.base.Objects;

@Component("kafka")
public class KafkaConnectionBean {
	@Value("${zkConnection}")
	private String zkConnection;
	
	@Value("${brokerList}")
	private String brokerList;
	
	@Value("${groupId}")
	private String groupId;
	
	public String getZkConnection() {
		return zkConnection;
	}
	public void setZkConnection(String zkConnection) {
		this.zkConnection = zkConnection;
	}
	public String getBrokerList() {
		return brokerList;
	}
	public void setBrokerList(String brokerList) {
		this.brokerList = brokerList;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("zkConnection", this.zkConnection)
			.add("brokerList", this.brokerList)
			.add("groupId", this.groupId)
			.toString();
	}	
}
