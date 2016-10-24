package com.honestwalker.androidutils;

public class StringUtil {

	/**
	 * 是否包含数字
	 * @param str
	 * @return
	 */
	public static boolean hasDigital(String str) {
		if(!isEmptyOrNull(str)) {
			for(char ch : str.toCharArray()) {
				if( ((int)ch) >= 49 && ((int)ch) <= 57 ) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}
	
	/**
	 * 是否包含字母
	 * @param str
	 * @return
	 */
	public static boolean hasLetter(String str) {
		if(!isEmptyOrNull(str)) {
			for(char ch : str.toCharArray()) {
				if( ((int)ch) >= 65 && ((int)ch) <= 122 ) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}
	
	/**
	 * 字符串为null 空格 空字符串都返回true 否则false
	 * @param str
	 * @return
	 */
	public static boolean isEmptyOrNull(String str) {
		if(str == null || str.trim().equals("")) {
			return true;
		} else {
			return false;
		}
	}
	

	/** 字符串首字母转大写 */
	public static String toFirstLetterUpperCase(String str) {  
	    if(str == null || str.length() < 2){  
	        return str;  
	    }  
	    String firstLetter = str.substring(0, 1).toUpperCase();  
	    return firstLetter + str.substring(1, str.length());  
	}
	
	public static void main(String[] args) {
	}
	
}
