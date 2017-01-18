package com.ailk.eaap.op2.common.test;


import com.ailk.eaap.op2.common.JsonUtil;


public class MyDataSource {
	private String driverClass;
	private String jdbcUrl;
	private String user;
	private String password;
	private String maxPoolSize;
	private String minPoolSize;
	private String initialPoolSize;
	private String acquireRetryDelay;
	private String breakAfterAcquireFailure;
	private String testConnectionOnCheckout;
	private String testConnectionOnCheckin;
	private String idleConnectionTestPeriod;
	private String acquireRetryAttempts;
	private String testSql;
	public String getDriverClass() {
		return driverClass;
	}
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getMaxPoolSize() {
		return maxPoolSize;
	}
	public void setMaxPoolSize(String maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}
	public String getMinPoolSize() {
		return minPoolSize;
	}
	public void setMinPoolSize(String minPoolSize) {
		this.minPoolSize = minPoolSize;
	}
	public String getInitialPoolSize() {
		return initialPoolSize;
	}
	public void setInitialPoolSize(String initialPoolSize) {
		this.initialPoolSize = initialPoolSize;
	}
	public String getAcquireRetryDelay() {
		return acquireRetryDelay;
	}
	public void setAcquireRetryDelay(String acquireRetryDelay) {
		this.acquireRetryDelay = acquireRetryDelay;
	}
	public String getBreakAfterAcquireFailure() {
		return breakAfterAcquireFailure;
	}
	public void setBreakAfterAcquireFailure(String breakAfterAcquireFailure) {
		this.breakAfterAcquireFailure = breakAfterAcquireFailure;
	}
	public String getTestConnectionOnCheckout() {
		return testConnectionOnCheckout;
	}
	public void setTestConnectionOnCheckout(String testConnectionOnCheckout) {
		this.testConnectionOnCheckout = testConnectionOnCheckout;
	}
	public String getTestConnectionOnCheckin() {
		return testConnectionOnCheckin;
	}
	public void setTestConnectionOnCheckin(String testConnectionOnCheckin) {
		this.testConnectionOnCheckin = testConnectionOnCheckin;
	}
	public String getIdleConnectionTestPeriod() {
		return idleConnectionTestPeriod;
	}
	public void setIdleConnectionTestPeriod(String idleConnectionTestPeriod) {
		this.idleConnectionTestPeriod = idleConnectionTestPeriod;
	}
	public String getAcquireRetryAttempts() {
		return acquireRetryAttempts;
	}
	public void setAcquireRetryAttempts(String acquireRetryAttempts) {
		this.acquireRetryAttempts = acquireRetryAttempts;
	}
	
	public String getTestSql() {
		return testSql;
	}
	public void setTestSql(String testSql) {
		this.testSql = testSql;
	}
	public String toString(){
		String s = JsonUtil.ObjToJsonStr(this);
		return s;
	}
}
