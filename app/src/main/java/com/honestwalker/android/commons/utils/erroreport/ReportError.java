package com.honestwalker.android.commons.utils.erroreport;

import com.honestwalker.androidutils.ClassUtil;

public class ReportError {

	private String client;
	private String name;
	private String remark;
	private String version;
	private String logContent;
	private Throwable exception;
	
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getLogContent() {
		return logContent;
	}
	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}
	
	public Throwable getException() {
		return exception;
	}
	public void setException(Throwable exception) {
		this.exception = exception;
	}
	
	@Override
	public String toString() {
		return ClassUtil.getFieldNameAndValue(this, ";", true);
	}
	

}
