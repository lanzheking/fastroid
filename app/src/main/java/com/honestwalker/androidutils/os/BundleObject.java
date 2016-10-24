package com.honestwalker.androidutils.os;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 自由向BundleObject put 参数，然后使用Message msg = new Message(); msg = data; data是BundleObject的实例
 * @author Administrator
 *
 */
public class BundleObject implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Map<String,Object> objs = new HashMap<String, Object>();
	
	public void put(String key,Object obj) {
		this.objs.put(key, obj);
	}
	
	public Object get(String key) {
		return this.objs.get(key);
	}
	
	/**
	 * 不存在返回null
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		if(this.objs.get(key) != null) {
			return this.objs.get(key).toString();
		} else {
			return null;
		}
	}
	
	/**
	 * 不存在返回默认值
	 * @param key
	 * @param defaultResult
	 * @return
	 */
	public String getString(String key,String defaultResult) {
		if(this.objs.get(key) != null) {
			return this.objs.get(key).toString();
		} else {
			return defaultResult;
		}
	}
	
	public Boolean getBoolean(String key,Boolean defaultResult) {
		try{
			Boolean result = Boolean.parseBoolean(get(key).toString().toLowerCase());
			return result;
		} catch (Exception e) {
			return defaultResult;
		}
	}
	
	public int getInt(String key,int defaultResult){
		try{
			Integer result = Integer.parseInt(get(key).toString().toLowerCase());
			return result;
		} catch (Exception e) {
			return defaultResult;
		}
	}
	
	public double getDouble(String key,double defaultResult) {
		try{
			Double result = Double.parseDouble(get(key).toString().toLowerCase());
			return result;
		} catch (Exception e) {
			return defaultResult;
		}
	}
	
	public float getFloat(String key,float defaultResult) {
		try{
			Float result = Float.parseFloat(get(key).toString().toLowerCase());
			return result;
		} catch (Exception e) {
			return defaultResult;
		}
	}
	
	public long getLong(String key , long defaultResult) {
		try{
			Long result = Long.parseLong(get(key).toString().toLowerCase());
			return result;
		} catch (Exception e) {
			return defaultResult;
		}
	}
	
	@Override
	public String toString() {
		Iterator<Entry<String, Object>> iter = objs.entrySet().iterator();
		String result = "";
		while(iter.hasNext()) {
			Entry<String,Object> en = iter.next();
			result += "[" + en.getKey() + " = " + en.getValue() + "] ";
		}
		if(result.equals("")) {
			return super.toString();
		} else {
			return result;
		}
	}
	
}
