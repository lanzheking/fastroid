package com.honestwalker.androidutils.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;

/**
 * Http请求参数封装
 * @author langel 2011-6-17
 *
 */
public class RequestParameter {
	
	List<NameValuePair> paramList = new ArrayList<NameValuePair>();
	
	public void put(String key,String value) {
		if(key != null) {
			if(value == null) {
				value = "";
			}
			value = value.trim();
			NameValuePair param = new BasicNameValuePair(key,value);
			for(NameValuePair p : paramList) {
				if(p.getName().equals(key)) {
					paramList.remove(p);
					break;
				}
			}
			paramList.add(param);
		}
	}
	
	/**
	 *清除全部参数
	 */
	public void clear() {
		paramList.clear();
	}
	
	/**
	 * 判断参数列某个参数是否存在
	 * @param key 参数名
	 * @return
	 */
	public Boolean contains(String key) {
		if(key == null) {
			return false;
		} else {
			for(NameValuePair p : paramList) {
				if(p.getName().equals(key)) {
					return true;
				}
			}
			return false;
		}
	}
	
	/**
	 * 删除某个参数
	 * @param key
	 * @return
	 */
	public void removeParam(String key) {
		if(key != null) {
			for(NameValuePair p : paramList) {
				if(p.getName().equals(key)) {
					paramList.remove(p);
					break;
				}
			}
		}
	}
	
	/**
	 * 取得指定参数的值
	 * @param key
	 * @return
	 */
	public String getParam(String key) {
		if(key != null) {
			for(NameValuePair p : paramList) {
				if(p.getName().equals(key)) {
					return p.getValue();
				}
			}
			return null;
		} else {
			return null;
		}
	}
	
	
	
	public List<NameValuePair> getParameterList() {
		return paramList;
	}
	
	/**
	 * 使用post也可以使用次参数，虽然显示的结果是?key=value形式，但实际上还是post发送
	 */
	@Override
	public String toString(){
		String result = "?";
		for(NameValuePair param : paramList) {
			result += param.getName() + "=" + param.getValue() + "&";
		}
		return result.substring(0,result.length() - 1);
	}
	
	public void convert(BasicHttpParams httpParams) {
		for(NameValuePair nvp : paramList) {
			httpParams.setParameter(nvp.getName(), nvp.getValue());
		}
	}

}
