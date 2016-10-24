package com.honestwalker.androidutils.os;

import android.os.Bundle;
import android.os.Message;

public class MyMessage {
	public int what = 0;
	public Object obj;
	private Message message;
	
	public MyMessage() {
	}
	
	public MyMessage(int what) {
		this(what,null);
	}
	
	public MyMessage(int what,Object obj) {
		this.what = what;
		this.obj    = obj;
		message = new Message();
		message.what = what;
		message.obj    = obj;
	}
	
	public Message getMessage() {
		return message;
	}
	
	public Message put(String key, String value) {
		Bundle bundle = new Bundle();
		bundle.putString(key, value);
		message.setData(bundle);
		return message;
	}
	
	public Message put(String[] keys,String[] values) {
		if(keys == null || keys.length == 0) {
			return message;
		}
		Bundle bundle = new Bundle();
		for(int i=0;i<keys.length;i++) {
			if(i < values.length) {
				bundle.putString(keys[i], values[i]);
			} else {
				bundle.putString(keys[i], "");
			}
		}
		message.setData(bundle);
		return message;
	}
	
}
