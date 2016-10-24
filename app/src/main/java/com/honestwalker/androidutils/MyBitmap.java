package com.honestwalker.androidutils;

import java.io.Serializable;


public class MyBitmap implements Serializable {
	
	/**
	 * * serialVersionUID解释:100 *
	 * http://www.blogjava.net/invisibletank/archive/2007/11/15/160684.html101
	 */
	private static final long serialVersionUID = 1L;
	private byte[] bitmapBytes = null;
	private String name = null;

	public MyBitmap(byte[] bitmapBytes, String name) {
		// TODO Auto-generated constructor stub108
		this.bitmapBytes = bitmapBytes;
		this.name = name;
	}

	public byte[] getBitmapBytes() {
		return this.bitmapBytes;
	}

	public String getName() {
		return this.name;
	}
	
}