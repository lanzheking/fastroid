package com.honestwalker.android.commons.cache;

import android.content.Context;

import com.honestwalker.android.commons.config.ContextProperties;
import com.honestwalker.android.commons.db.DatabaseIO;
import com.honestwalker.androidutils.IO.ObjectStreamIO;
import com.honestwalker.androidutils.IO.SharedPreferencesLoader;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by honestwalker on 15-11-9.
 */
public class CacheManager<T> {

    private String name;

    private CacheType cacheType;

    private Class clazz;

    public static HashMap<String, Object> applicationContextParams = new HashMap<String, Object>();

    /**
     * <b>缓存类型有三种</b>     <br />
     * ApplicaiontContext 内存 <br />
     * DataBase 数据库缓存      <br />
     * ObjectStream 对象流缓存  <br />
     */
    public static enum CacheType {
        ApplicaiontContext, DataBase, ObjectStream, SharedPreferences
    }

    /**
     *
     * @param cacheType
     *            缓存类型
     * @param name
     *            缓存名字 对象流缓存中是文件名，
     */
    public CacheManager(CacheType cacheType, String name) {
        this.cacheType = cacheType;
        this.name = name;
    }
    /**
     *
     * @param cacheType
     *            缓存类型
     * @param clazz
     *            缓存名字 数据库存储是表名（javaBean类）
     */
    public CacheManager(CacheType cacheType, Class clazz) {
        this.cacheType = cacheType;
        this.clazz = clazz;
        this.name = clazz.toString();
    }

    /**
     * 获取缓存对象，只支持ApplicaiontContext, ObjectStream, SharedPreferences
     */
    private T get() {

        if (CacheType.ApplicaiontContext.equals(cacheType)) {
            return (T) applicationContextParams.get(name);
        } else if (CacheType.SharedPreferences.equals(cacheType)) {
            T t = (T) SharedPreferencesLoader.getString(name);
            if (t != null) {
                return t;
            }
        } else if (CacheType.ObjectStream.equals(cacheType)) {
            try {
                return (T) ObjectStreamIO.input(
                        ContextProperties.getConfig().cachePath, name);
            } catch (Exception e) {
                ExceptionUtil.showException(e);
            }
        }
        return null;
    }

    /**
     * 获取缓存对象，支持ApplicaiontContext, DataBase, ObjectStream, SharedPreferences
     */
    public T get(Context context) {

        if (CacheType.DataBase.equals(cacheType)) {
            List<?> list = DatabaseIO.getInstance(context).queryAll(clazz);
            if (list != null && list.size() != 0) {
                try {
                    return (T) list;
                }catch (ClassCastException e){
                    return (T)list.get(0);
                }
            }else {
                return null;
            }
        } else {
            return get();
        }
    }

    /**
     * 存储缓存，如果为空则删除已有缓存<br />
     * 支持ApplicaiontContext, DataBase, ObjectStream, SharedPreferences
     */
    public void set(Context context, T t) {

        if (CacheType.DataBase.equals(cacheType)) {
            if (t != null) {
                if (t instanceof List) {
                    DatabaseIO.getInstance(context).deleteAll(clazz);
                    DatabaseIO.getInstance(context).saveAll((List<?>) t);
                }else {
                    if (DatabaseIO.getInstance(context).queryAll(clazz) != null) {
                        DatabaseIO.getInstance(context).deleteAll(clazz);
                    }
                    DatabaseIO.getInstance(context).save(t);
                }
            }else {
                DatabaseIO.getInstance(context).deleteAll(clazz);
            }
        } else {
            set(t);
        }
    }

    /**
     * 存储缓存，如果为空则删除已有缓存<br />
     * 只支持ApplicaiontContext, ObjectStream, SharedPreferences
     *
     * @param cacheObj
     * @return
     */
    private void set(T cacheObj) {

        if (CacheType.ApplicaiontContext.equals(cacheType)) {
            applicationContextParams.put(name, cacheObj);
        } else if (CacheType.SharedPreferences.equals(cacheType)) {
            SharedPreferencesLoader.putString(name, cacheObj + "");
        } else if (CacheType.ObjectStream.equals(cacheType)) {
            try {
                if (cacheObj == null) {
                    ObjectStreamIO.remove(
                            ContextProperties.getConfig().cachePath, name);
                } else {
                    ObjectStreamIO.output(
                            ContextProperties.getConfig().cachePath, cacheObj,
                            name);
                }
            } catch (Exception e) {
                ExceptionUtil.showException(e);
            }
        }
    }

}
