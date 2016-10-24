package com.honestwalker.androidutils.net;

import java.io.Serializable;
import java.util.Date;

public class CookieSerializable implements Serializable{
	
	private static final long serialVersionUID = -401922944851872552L;

	private String attribute;
	private String comment;
	private String commentURL;
	private String domain;
	private Date   expiryDate;
	private String name;
	private String path;
	private int[] ports;
	private String value;
	private int version;
	
	public CookieSerializable(String name,String value) {
		this.name  = name;
		this.value = value;
	}
	
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCommentURL() {
		return commentURL;
	}
	private void setCommentURL(String commentURL) {
		this.commentURL = commentURL;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int[] getPorts() {
		return ports;
	}
	private void setPorts(int[] ports) {
		this.ports = ports;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "Cookie=" + " attribute=" + attribute + " comment=" + comment +
				" commentURL=" + commentURL + " domain=" + domain + " expiryDate=" + expiryDate + " name=" + name + " path=" + path + " value=" + value + " version=" + version;
	}
	
}
