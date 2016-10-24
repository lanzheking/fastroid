package com.honestwalker.androidutils;

import java.util.ArrayList;
import java.util.List;

public class ArrayConverter<T> {
	public ArrayList<T> array2List(T[] array){
		ArrayList<T> list = new ArrayList<T>();
		for(T obj : array) {
			list.add(obj);
		}
		return list;
	}
	
	public T[] list2Array(List<T> list){
		if(list!= null) {
			T[] array = (T[]) new Object[list.size()];
			for(int i=0;i<list.size();i++) {
				array[i] = (T) new Object();
				array[i] = list.get(i);
			}
			return array;
		}
		return null;
	}
}
