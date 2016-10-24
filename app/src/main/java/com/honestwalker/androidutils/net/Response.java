package com.honestwalker.androidutils.net;

/**
 * Http response 响应封装
 * @author Administrator
 *
 */
public class Response {
	private String url;
	private RequestParameter parameter;
	private String response;
	
	public Response(String url,RequestParameter parameter,String response) {
		this.url 			 = url;
		this.parameter = parameter;
		this.response   = response;
	}
	
	public String getUrl() {
		return url;
	}
	private void setUrl(String url) {
		this.url = url;
	}
	public RequestParameter getParameter() {
		return parameter;
	}
	private void setParameter(RequestParameter parameter) {
		this.parameter = parameter;
	}
	public String getResponse() {
		return response;
	}
	private void setResponse(String response) {
		this.response = response;
	}
	
}
