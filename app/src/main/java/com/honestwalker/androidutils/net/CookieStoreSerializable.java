package com.honestwalker.androidutils.net;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CookieStoreSerializable implements Serializable{
	
	private static final long serialVersionUID = 7246096608126405286L;
	
	private List<CookieSerializable> cookies = new ArrayList<CookieSerializable>();
	
	public void add(CookieSerializable cookieSerializable) {
		if(cookieSerializable != null) {
			cookies.add(cookieSerializable);
		}
	}
	
	public Boolean contains(CookieSerializable cookieSerializable) {
		return cookies.contains(cookieSerializable);
	}
	
	public Boolean containsName(String name) {
		for(CookieSerializable cookie : cookies) {
			if(cookie.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public void clear() {
		cookies.clear();
	}
	
	public List<CookieSerializable> getCookies() {
		return cookies;
	}
	
}
