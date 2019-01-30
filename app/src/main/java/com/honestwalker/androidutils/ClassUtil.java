package com.honestwalker.androidutils;

import android.util.Log;

import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Class对象工具类，主要用于反射等相关辅助
 * @author honestwalker
 *
 */
public class ClassUtil {

	private final static String TAG = "ClassLoader";

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

	/**
	 * 获取类加载器
	 * @Title: getClassLoader
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @return    设定文件
	 * @return ClassLoader    返回类型
	 * @throws
	 */
	private static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	/**
	 * 加载指定包下的所有类
	 * @Title: getClassSet
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param packageName
	 * @param @return    设定文件
	 * @return Set<Class<?>>    返回类型
	 * @throws
	 */
	public static Set<Class<?>> getClassSet(String packageName) {
		Set<Class<?>> classSet = new HashSet<Class<?>>();

		try {
			Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));

			while (urls.hasMoreElements()) {

				URL url = urls.nextElement();

				if (url != null) {

					String protocol = url.getProtocol();

					if (protocol.equals("file")) {
						// 转码
						String packagePath = URLDecoder.decode(url.getFile(), "UTF-8");
						// String packagePath =url.getPath().replaceAll("%20",
						// "");
						// 添加
						addClass(classSet, packagePath, packageName);

					} else if (protocol.equals("jar")) {

						JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();

						if (jarURLConnection != null) {
							JarFile jarFile = jarURLConnection.getJarFile();

							if (jarFile != null) {

								Enumeration<JarEntry> jarEntries = jarFile.entries();

								while (jarEntries.hasMoreElements()) {

									JarEntry jarEntry = jarEntries.nextElement();

									String jarEntryName = jarEntry.getName();

									if (jarEntryName.endsWith(".class")) {

										String className = jarEntryName.substring(0, jarEntryName.lastIndexOf("."))
												.replaceAll("/", ".");
										doAddClass(classSet, className);

									}
								}

							}
						}
					}

				}

			}

		} catch (IOException e) {
			ExceptionUtil.showException(TAG, e);
		}

		return classSet;
	}
	private static void doAddClass(Set<Class<?>> classSet, String className) {
		Class<?> cls = loadClass(className, false);
		classSet.add(cls);
	}
	/**
	 * 加载类
	 * 需要提供类名与是否初始化的标志，
	 * 初始化是指是否执行静态代码块
	 * @Title: loadClass
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param className
	 * @param @param isInitialized  为提高性能设置为false
	 * @param @return    设定文件
	 * @return Class<?>    返回类型
	 * @throws
	 */
	public static Class<?> loadClass(String className, boolean isInitialized) {

		Class<?> cls;
		try {
			cls = Class.forName(className, isInitialized, getClassLoader());
			//Thread.currentThread().getContextClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			ExceptionUtil.showException(TAG, e);
			throw new RuntimeException(e);
		}

		return cls;
	}
	/**
	 * 添加文件到SET集合
	 * @Title: addClass
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param classSet
	 * @param @param packagePath
	 * @param @param packageName    设定文件
	 * @return void    返回类型
	 * @throws
	 */
	private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {

		File[] files = new File(packagePath).listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				return (file.isFile() && file.getName().endsWith(".class") || file.isDirectory());
			}
		});

		for (File file : files) {

			String fileName = file.getName();

			if (file.isFile()) {
				String className = fileName.substring(0, fileName.lastIndexOf("."));

				if (StringUtils.isNotEmpty(packageName)) {

					className = packageName + "." + className;
					Log.e("ClassLoader", className);
				}
				// 添加
				doAddClass(classSet, className);
			} else {
				// 子目录
				String subPackagePath = fileName;
				if (StringUtils.isNotEmpty(packagePath)) {
					subPackagePath = packagePath + "/" + subPackagePath;
				}

				String subPackageName = fileName;
				if (StringUtils.isNotEmpty(packageName)) {
					subPackageName = packageName + "." + subPackageName;
				}

				addClass(classSet, subPackagePath, subPackageName);
			}
		}

	}

	/**
	 * 通过反射给object 指定的 field 赋值
	 * @param object
	 * @param fieldName
	 * @param value
     */
	public static void setFieldValue(Object object, String fieldName, Object value) {
		try {
			Field field = object.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			setFieldValueByType(field, object, value);
		} catch (Exception e) {
			ExceptionUtil.showException(e);
		}
	}

	public static <T> Object getValueByType(Object value, Class<T> type) {
		if(value == null) return null;
		if(!type.equals(value.getClass())) {
			if(type.equals(String.class)) {
				return value + "";
			} else if(type.equals(Integer.class) || type.getName().equals("int")) {
				return Integer.parseInt(value + "");
			} else if(type.equals(Float.class) || type.getName().equals("float")) {
				return Float.parseFloat(value + "");
			} else if(type.equals(Double.class) || type.getName().equals("double")) {
				return Double.parseDouble(value + "");
			} else if(type.equals(Boolean.class) || type.getName().equals("boolean")) {
				return Boolean.parseBoolean(value + "");
			} else if(type.equals(Long.class) || type.getName().equals("long")) {
				return Long.parseLong(value + "");
			}
		}
		return (T)value;
	}

	private  static void setFieldValueByType(Field field, Object object, Object value) throws Exception {
		if(value == null) return;
		if(!field.getType().equals(value.getClass())) {
			if(field.getType().equals(String.class)) {
				field.set(object, value + "");
			} else if(field.getType().equals(Integer.class) || field.getType().getName().equals("int")) {
				field.set(object, Integer.parseInt(value + ""));
			} else if(field.getType().equals(Float.class) || field.getType().getName().equals("float")) {
				field.set(object, Float.parseFloat(value + ""));
			} else if(field.getType().equals(Double.class) || field.getType().getName().equals("double")) {
				field.set(object, Double.parseDouble(value + ""));
			} else if(field.getType().equals(Boolean.class) || field.getType().getName().equals("boolean")) {
				field.set(object, Boolean.parseBoolean(value + ""));
			} else if(field.getType().equals(Long.class) || field.getType().getName().equals("long")) {
				field.set(object, Long.parseLong(value + ""));
			} else {
				try {
					field.set(object, value);
				} catch (Exception e) {
					throw new Exception("字段 " + field.getName() + " 类型 " + field.getType() + " 与值" + value + " 类型" + value.getClass() + " 不匹配!");
				}
			}
		} else {
			field.set(object, value);
		}
	}

}
