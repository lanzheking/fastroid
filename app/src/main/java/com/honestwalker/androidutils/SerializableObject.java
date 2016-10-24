package com.honestwalker.androidutils;

import java.io.Serializable;

public class SerializableObject<T extends Serializable> implements Serializable {
	private T[] data;

	public T[] getData() {
		return data;
	}

	public void setData(T[] data) {
		this.data = data;
	}
	
}
