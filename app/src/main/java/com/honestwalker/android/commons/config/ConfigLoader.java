package com.honestwalker.android.commons.config;

import android.content.Context;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.IO.LogCat;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigLoader {

	public static ContextProperties load(Context context, int mainConfigRes) throws Exception {

		Properties properties = new Properties();
		properties.load(context.getResources().openRawResource(
				mainConfigRes));

		Map<String, String> propertiesMap = new HashMap<String, String>();

		Enumeration<Object> enumeration = properties.keys();
		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement().toString();
			String value = properties.getProperty(key);
			value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
			propertiesMap.put(key, value);
		}

		ContextProperties config = ContextProperties.getConfig();
		Field[] fields = config.getClass().getFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			if (propertiesMap.containsKey(fieldName)) {
				String value = properties.getProperty(fieldName);
				Object type = field.getGenericType();
				try {
					if (Integer.class.equals(type)
							|| "int".equals(type.toString())) {
						field.set(config, Integer.parseInt(value));
					} else if (Boolean.class.equals(type)
							|| "boolean".equals(type.toString())) {
						field.set(config, Boolean.parseBoolean(value));
					} else if (Long.class.equals(type)
							|| "long".equals(type.toString())) {
						field.set(config, Long.parseLong(value));
					} else if (Double.class.equals(type)
							|| "double".equals(type.toString())) {
						field.set(config, Double.parseDouble(value));
					} else if (String.class.equals(type)) {
						field.set(config, value);
					}
				} catch (Exception e) {
					throw new Exception("读取" + fieldName + "=" + value + "失败",e);
				}
			}
		}

		{ // 把config.cache设置为绝对路径
			String localCachePath = context.getCacheDir().getPath();
			if (localCachePath.endsWith("/")) {
				localCachePath = localCachePath.substring(0,
						localCachePath.length() - 1);
			}
			if (config.cachePath != null) {
				if (!config.cachePath.startsWith("/")) {
					config.cachePath = "/" + config.cachePath;
				}
				config.cachePath = localCachePath + config.cachePath;
			} else {
				config.cachePath = localCachePath;
			}
		}

		return config;
	}

}
