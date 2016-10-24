package com.honestwalker.androidutils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.honestwalker.androidutils.IO.LogCat;

/**
 * Class对象工具类，主要用于反射等相关辅助
 * @author honestwalker
 *
 */
public class ClassUtil {
	
	/**
	 * 输出对象所有字段的值
	 * @param obj          目标对象
	 * @param split        如何分割属性， 如 \r\n就是没个属性后换行显示，  ";" 就是用分号分割显示
	 * @param showNull     是否显示null值的属性
	 * @return
	 */
	public static String getFieldNameAndValue(Object obj , String split , boolean showNull) {
		
		StringBuffer valueSB = new StringBuffer();
		
		Field[] fs = obj.getClass().getDeclaredFields();
		for(Field f : fs) {
			
			// 设置Accessible为true才能直接访问private属性
			f.setAccessible(true); 
			try {
				if(f.get(obj) == null) {
					if(showNull) {
						valueSB.append(f.getName() + "=null" + split);
					}
				} else {
					valueSB.append(f.getName() + "=" + f.get(obj).toString() + split);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return valueSB.toString();
	}
	
	/**
	 * 获取对象属性值map
	 * @param obj
	 * @return
	 */
	public static Map<String , String> getFieldNameAndValueMapping(Object obj) {
		HashMap<String, String> params = new HashMap<String, String>();
		
		Field[] fs = obj.getClass().getDeclaredFields();
		for(Field f : fs) {
			
			// 设置Accessible为true才能直接访问private属性
			f.setAccessible(true); 
			try {
				if(f.get(obj) != null) {
					params.put(f.getName(), f.get(obj).toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return params;
		
	}
	
	/**
	 * 获取对象全部属性，也包括private
	 * @return
	 */
	public static Field[] getAllFields(Class clazz) {
		Field[] fs = clazz.getDeclaredFields();
		return fs;
	}

	/**
	 * 两个对象同名字段对拷，不会拷贝超类
	 * @param srcObj
	 * @param desObj
	 */
	public static void reflectCopy(Object srcObj , Object desObj) {
		reflectCopy(srcObj, desObj, false , true);
	}

	/**
	 * 两个对象同名字段对拷
	 * @param srcObj
	 * @param desObj
	 * @param copySupper  是否同时拷贝超类
	 * @param copyCover   如果目标类属性已经有值，是否覆盖
	 */
	public static void reflectCopy(Object srcObj ,
								   Object desObj ,
								   boolean copySupper ,
								   boolean copyCover) {

		if(copySupper) {
			if(srcObj.getClass().getSuperclass().equals(Object.class)) {	// Object 不拷贝
				LogCat.d("ddddd" , "父类是Object 不拷贝 " + desObj.getClass());
				copySupper = false;
			}
		}

		if(srcObj == null) {
			desObj = null;
		} else {
			Field[] srcObjFields = getAllFields(srcObj.getClass());
			Field[] srcSupperObjFields = null;
			
			int srcObjFieldCount = srcObjFields.length;
			int srcSuperObjFieldCount = 0;
			
			if(copySupper && srcObj.getClass().getSuperclass() != null) {
				srcSupperObjFields = getAllFields(srcObj.getClass().getSuperclass());
				srcSuperObjFieldCount = srcSupperObjFields.length;
			}
			
			Field[] allSrcFields = new Field[srcObjFieldCount + srcSuperObjFieldCount];
			
			int index = 0;
			for(Field f : srcObjFields) {
				allSrcFields[index] = f;
				index++;
			}
			if(copySupper && srcSupperObjFields != null) {
				for(Field f : srcSupperObjFields) {
					allSrcFields[index] = f;
					index++;
				}
			}
			
			Map<String, Field> desObjFieldsMap = getAllFieldsMap(desObj.getClass() , copySupper);
			for(Field field : allSrcFields) {
				field.setAccessible(true);
				String fieldName = field.getName();
				if(desObjFieldsMap.containsKey(fieldName)) {
					try {
						Object desFieldValue = desObjFieldsMap.get(field.getName()).get(desObj);
						if(desFieldValue == null || (desFieldValue != null && copyCover)) {
							desObjFieldsMap.get(field.getName()).set(desObj, field.get(srcObj));
						}
					} catch (IllegalArgumentException e) {
					} catch (IllegalAccessException e) {
					}
				}
			}
			
		}
	}


	/**
	 * 反射拷贝 <br />
	 * 批量拷贝两个ArrayList中的对象，两个对象不需要是同类型，只要他们有一样名称的字段就拷贝<br />
	 * 注意，此拷贝过程会清空desObjArr，也就是目标列表里面的数据，然后全部拷贝自源列表
	 * @param srcObjArr 源列表
	 * @param desObjArr 目标列表
	 * @param desClass  目标列表的数据类型
	 * @return
	 */
	public static <T,D> void  reflectCopyArray(ArrayList<T> srcObjArr , ArrayList<D> desObjArr , Class<D> desClass) {
		reflectCopyArray(srcObjArr , desObjArr , desClass , false , true);
	}

	/**
	 * 反射拷贝 <br />
	 * 批量拷贝两个ArrayList中的对象，两个对象不需要是同类型，只要他们有一样名称的字段就拷贝<br />
	 * 注意，此拷贝过程会清空desObjArr，也就是目标列表里面的数据，然后全部拷贝自源列表
	 * @param srcObjArr 源列表
	 * @param desObjArr 目标列表
	 * @param desClass  目标列表的数据类型
	 * @param copySuper  是否拷贝父类属性
	 * @param desClass  是否覆盖拷贝已经存在的数据
	 * @return
	 */
	public static <T,D> void  reflectCopyArray(ArrayList<T> srcObjArr , ArrayList<D> desObjArr , Class<D> desClass , boolean copySuper , boolean copyCover) {

		if(srcObjArr == null) return;
		if(desObjArr == null) {
			desObjArr = new ArrayList();
		} else {
			desObjArr.clear();
		}

		for(T obj : srcObjArr) {
			try {
				D desInstence = desClass.newInstance();
				reflectCopy(obj , desInstence , copySuper , copyCover);
				desObjArr.add(desInstence);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 获取一个类的属性map key是属性名 value是field
	 * @param clazz
	 * @return
	 */
	public static Map<String , Field> getAllFieldsMap(Class clazz , boolean copySupper) {
		Field[] fs = clazz.getDeclaredFields();
		Map<String , Field> map = new HashMap<String, Field>();
		for(Field field : fs) {
			field.setAccessible(true); 
			map.put(field.getName(), field);
		}
		if(copySupper && clazz.getSuperclass() != null) {
			Field[] fsSuper = clazz.getSuperclass().getDeclaredFields();
			for(Field field : fsSuper) {
				field.setAccessible(true); 
				map.put(field.getName(), field);
			}
		}
		return map;
	}

	/**
	 * 判断类是否包含某属性
	 * @param clazz
	 * @param fieldName 属性名
	 * @param searchParent 是否查找父类
	 * @return
	 */
	public static boolean hasField(Class clazz , String fieldName , boolean searchParent) {
		if(clazz == null) return false;
		try {
			clazz.getDeclaredField(fieldName);
			return true;
		} catch (Exception e) {
			if(searchParent) {
				return hasField(clazz.getSuperclass() , fieldName , true);
			}
			return false;
		}
	}

	/**
	 * 获得对象属性
	 * @param clazz
	 * @param fieldName
	 * @param searchParent 是否查找父类
	 * @return
	 */
	public static Field getField(Class clazz , String fieldName , boolean searchParent) {
		if(clazz == null) return null;
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (Exception e) {
			if(searchParent) {
				return getField(clazz.getSuperclass(), fieldName, true);
			}
			return null;
		}
	}

	/**
	 * 判断类是否包含某方法
	 * @param clazz
	 * @param methodName 方法名
	 * @param searchParent 是否查找父类
	 * @return
	 */
	public static boolean hasMethod(Class clazz , String methodName , boolean searchParent) {
		if(clazz == null) return false;
		try {
			clazz.getDeclaredMethod(methodName);
			return true;
		} catch (Exception e) {
			if(searchParent) {
				return hasMethod(clazz.getSuperclass(), methodName, true);
			}
			return false;
		}
	}

	/**
	 * 获得对象方法
	 * @param clazz
	 * @param methodName
	 * @param searchParent 是否查找父类
	 * @return
	 */
	public static Method getMethod(Class clazz , String methodName , boolean searchParent) {
		if(clazz == null) return null;
		try {
			return clazz.getDeclaredMethod(methodName);
		} catch (Exception e) {
			if(searchParent) {
				return getMethod(clazz.getSuperclass(), methodName, true);
			}
			return null;
		}
	}
	
}
