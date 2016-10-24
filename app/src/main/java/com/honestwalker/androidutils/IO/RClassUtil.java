package com.honestwalker.androidutils.IO;

import android.content.Context;

import java.lang.reflect.Field;

import com.honestwalker.androidutils.exception.ExceptionUtil;

public class RClassUtil {
	
	/**
	 * 查找R 中 string 指定key的值
	 * @param RClass
	 * @param name
	 * @return
	 * @throws android.content.res.Resources.NotFoundException
	 */
	public static int getStringResIdByName(Class RClass , String name) throws android.content.res.Resources.NotFoundException {
		
		if(name == null) {
			throw new android.content.res.Resources.NotFoundException();
		}
		name = name.replace("@string/", "");
		
		try {
			// 得到 R 的 内部类 string
			Class[] innerClass = RClass.getDeclaredClasses();
			Class stringClass = null;
			for(Class clazz : innerClass) {
				if(clazz.getSimpleName().equals("string")) {
					stringClass = clazz;
					break;
				}
			}
			
			Field[] stringFields = stringClass.getFields();
			for(Field field : stringFields) {
				if(field.getName().equals(name)) {
					Object value = field.get(null);
					return Integer.parseInt(value.toString());
				}
			}
		} catch (Exception e) {
		}
		
		throw new android.content.res.Resources.NotFoundException();
	
	}
	
	/**
	 * 获取颜色值
	 * @param RClass
	 * @param name
	 * @return
	 */
	public static int getColorResIdByName(Class RClass ,String name) {
		return getRElementResIdByName(RClass, "color", name);
	}
	
	public static int getRElementResIdByName(Class RClass, String className ,String name) throws android.content.res.Resources.NotFoundException {
		if(name == null) {
			throw new android.content.res.Resources.NotFoundException();
		}
		name = name.replace("@" + className + "/", "");
		
		try {
			// 得到 R 的 内部类 drawable
			Class[] innerClass = RClass.getDeclaredClasses();
			Class drawableClass = null;
			for(Class clazz : innerClass) {
				if(clazz.getSimpleName().equals(className)) {
					drawableClass = clazz;
					break;
				}
			}
			
			if(drawableClass == null) {
				throw new android.content.res.Resources.NotFoundException();
			}
			
			Field[] stringFields = drawableClass.getFields();
			for(Field field : stringFields) {
				if(field.getName().equals(name)) {
					Object value = field.get(null);
					return Integer.parseInt(value.toString());
				}
			}
		} catch (Exception e) {
			ExceptionUtil.showException(e);
		}
		
		throw new android.content.res.Resources.NotFoundException();
	}
	
	/**
	 * 查找R 中 drawable 指定key 的值
	 * @param RClass
	 * @param name
	 * @return
	 * @throws android.content.res.Resources.NotFoundException
	 */
	public static int getDrawableResIdByName(Class RClass, String name) throws android.content.res.Resources.NotFoundException {
		
		if(name == null) {
			throw new android.content.res.Resources.NotFoundException();
		}
		name = name.replace("@drawable/", "");
		
		try {
			// 得到 R 的 内部类 drawable
			Class[] innerClass = RClass.getDeclaredClasses();
			Class drawableClass = null;
			for(Class clazz : innerClass) {
				if(clazz.getSimpleName().equals("drawable")) {
					drawableClass = clazz;
					break;
				}
			}
			
			if(drawableClass == null) {
				throw new android.content.res.Resources.NotFoundException();
			}
			
			Field[] stringFields = drawableClass.getFields();
			for(Field field : stringFields) {
				if(field.getName().equals(name)) {
					Object value = field.get(null);
					return Integer.parseInt(value.toString());
				}
			}
		} catch (Exception e) {
			ExceptionUtil.showException(e);
		}
		
		throw new android.content.res.Resources.NotFoundException();
	}
	
	public static int getDrawableResIdByName2(Class RDrawableClass, String name) throws android.content.res.Resources.NotFoundException {
		
		if(name == null) {
			throw new android.content.res.Resources.NotFoundException();
		}
		name = name.replace("@drawable/", "");
		Field[] rFields = RDrawableClass.getFields();
		for(Field field : rFields) {
			if(field.getName().equals(name)) {
				
				try {
					Object value = field.get(null);
					return Integer.parseInt(value.toString());
				} catch (Exception e) {
					throw new android.content.res.Resources.NotFoundException();
				}
				
			}
		}
		
		throw new android.content.res.Resources.NotFoundException();
	}

	public static int getDimenResIdByName(Class RClass, String name) throws android.content.res.Resources.NotFoundException {
		return getRElementResIdByName(RClass, "dimen", name);
	}

	/**
	 * 字符串形式获取R 中的索引值 ， 如 R , raw.abc   R , drawable.abc
	 * @param resStr
	 * @return
	 */
	public static int getResId(Class rClass , String resStr) {
		try {
			String rClassStr = rClass.getName();
			String[] rv = resStr.split("[.]");
			String innerClassStr = rClassStr + "$" + rv[0];
			Class innerClass = Class.forName(innerClassStr);
			int res = innerClass.getField(rv[1]).getInt(null);
			return res;
		} catch (Exception e) {
			ExceptionUtil.showException("inject" , e);
			return -1;
		}
	}

	public static Class getRClass(Context context) throws ClassNotFoundException {
		String packageName = context.getPackageName();
		String rPackageName = packageName + ".R";
		Class rClass = Class.forName(rPackageName);
		return rClass;
	}

}
