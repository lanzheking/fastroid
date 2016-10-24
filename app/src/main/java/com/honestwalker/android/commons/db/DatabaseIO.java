package com.honestwalker.android.commons.db;


import android.content.Context;

import com.honestwalker.androidutils.exception.ExceptionUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

/**
 * 数据库操作工具类
 */
public final class DatabaseIO {

    static final String DB_NAME = "kancartIO.db";

    Context context;
    DbUtils dbUtils;
    public DbUtils getDbUtils() {
        return dbUtils;
    }

    static DatabaseIO databaseIO;

    public static DatabaseIO getInstance(Context context) {
        if (databaseIO == null) {
            databaseIO = new DatabaseIO(context);
        }
        databaseIO.context = context;
        return databaseIO;
    }

    private DatabaseIO(Context context){
        this.context = context;
        dbUtils = DbUtils.create(context, DB_NAME);
        dbUtils.configAllowTransaction(true);
    }

    /*********************************************
     *
     *  用法用例：
     *  DbTestBean bean = new DbTestBean();
     *  bean.setName("alex");
     *	bean.setGender("male");
     *  不支持构造方法的赋值，比如：DbTestBean bean = new DbTestBean("alex"，"male");
     *
     *************************************************/

    /**
     * sql insert 添加或更新<b/>
     * @param t 数据库表中内容行，属性必须包含（int或long型的）id
     */
    public void saveOrUpdate(Object t){
        try {
            dbUtils.saveOrUpdate(t);
        } catch (DbException e) {
            ExceptionUtil.showException(e);
        }
    }

    /**
     * sql insert 添加或更新全部行<b/>
     * @param entities 数据库表中内容行，属性必须包含（int或long型的）id
     */
    public void saveOrUpdateAll(List<?> entities){
        try {
            dbUtils.saveOrUpdateAll(entities);
        } catch (DbException e) {
            ExceptionUtil.showException(e);
        }
    }

    /**
     * sql insert 添加<b/>
     * @param t 数据库表中内容行，属性必须包含（int或long型的）id
     */
    public void save(Object t){
        try {
            dbUtils.save(t);
        } catch (DbException e) {
            ExceptionUtil.showException(e);
        }
    }

    /**
     * sql insert 添加或更新全部行<b/>
     * @param entities 数据库表中内容行，属性必须包含（int或long型的）id
     */
    public void saveAll(List<?> entities){
        try {
            dbUtils.saveAll(entities);
        } catch (DbException e) {
            ExceptionUtil.showException(e);
        }
    }

    /**
     * sql insert 添加<b/>
     * save 和 saveBindingId 区别<b/>
     * saveBindingId 在save后会把生成的ID赋值到对象中，save没有这个功能<b/>
     *
     * @param t 数据库表中内容行，属性必须包含（int或long型的）id
     */
    public <T> T saveBindingId(T t){
        try {
            dbUtils.saveBindingId(t);
            return t;
        } catch (DbException e) {
            ExceptionUtil.showException(e);
        }
        return null;
    }

    /**
     * sql query 查询全部<b/>
     * @param entityType 数据库表名，属性必须包含（int或long型的）id
     */
    public List<?> queryAll(Class<?> entityType){
        return queryConditions(entityType, null);
    }

    /**
     * sql query 条件查询<b/>
     * @param whereBuilder (用法实例：1.WhereBuilder.b("name", "=", "Alex") 2.WhereBuilder.b("age", ">", "20"))
     */
    public List<?> queryConditions(Class<?> entityType,WhereBuilder whereBuilder){
        try {
            return dbUtils.findAll(Selector.from(entityType).where(whereBuilder));
        } catch (DbException e) {
            ExceptionUtil.showException(e);
        }
        return null;
    }

    /**
     * sql query 条件查询<b/>
     * @param whereBuilder (用法实例：1.WhereBuilder.b("name", "=", "Alex") 2.WhereBuilder.b("age", ">", "20"))
     */
    public List<?> queryFirst(Class<?> entityType,WhereBuilder whereBuilder){
        try {
            return dbUtils.findFirst(Selector.from(entityType).where(whereBuilder));
        } catch (DbException e) {
            ExceptionUtil.showException(e);
        }
        return null;
    }

    /**
     * sql query 条件查询<b/>
     * @param idValue 要查的表某行的ID
     */
    public <T> T queryByID(Class<T> entityType,Object idValue){
        try {
            return dbUtils.findById(entityType, idValue);
        } catch (DbException e) {
            ExceptionUtil.showException(e);
        }
        return null;
    }

    /**
     * sql count 查询符合查询条件的条数<b/>
     * @param whereBuilder (用法实例：1.WhereBuilder.b("name", "=", "Alex") 2.WhereBuilder.b("age", ">", "20"))
     */
    public long countConditions(Class<?> entityType,WhereBuilder whereBuilder){
        try {
            return dbUtils.count(Selector.from(entityType).where(whereBuilder));
        } catch (DbException e) {
            ExceptionUtil.showException(e);
        }
        return 0;
    }

    /**
     * sql count 查询全部条数<b/>
     * @param entityType 数据库表名，属性必须包含（int或long型的）id
     */
    public long countAll(Class<?> entityType){
        return countConditions(entityType, null);
    }

    /**
     * sql update 条件更新
     * @param updateColumnNames 需要修改的列名，若参数不为空，只修改这些列
     */
    public void update(Object t,String... updateColumnNames){
        update(t, null, updateColumnNames);
    }

    /**
     * sql update 条件更新
     * @param whereBuilder (用法实例：1.WhereBuilder.b("name", "=", "Alex") 2.WhereBuilder.b("age", ">", "20"))
     * @param updateColumnNames 需要修改的列名，若参数不为空，只修改这些列
     */
    public void update(Object t,WhereBuilder whereBuilder,String... updateColumnNames){
        try {
            dbUtils.update(t, whereBuilder, updateColumnNames);
        } catch (DbException e) {
            ExceptionUtil.showException(e);
        }
    }

    /**
     * sql update 条件更新全部
     */
    public void updateAll(List<?> ts,WhereBuilder whereBuilder,String... updateColumnNames){
        try {
            dbUtils.updateAll(ts, whereBuilder, updateColumnNames);
        } catch (DbException e) {
            ExceptionUtil.showException(e);
        }
    }

    public void updateAll(List<?> ts,String... updateColumnNames){
        updateAll(ts, null, updateColumnNames);
    }

    /**
     * sql delete 删除
     */
    public void deleteOnConditions(Class<?> entityType,WhereBuilder whereBuilder){
        try {
            dbUtils.delete(entityType,whereBuilder);
        } catch (DbException e) {
            ExceptionUtil.showException(e);
        }
    }

    /**
     * sql delete 删除某个数据
     */
    public void delete(Object t){
        try {
            dbUtils.delete(t);
        } catch (DbException e) {
            ExceptionUtil.showException(e);
        }
    }

    /**
     * sql delete 删除表中所有数据
     */
    public void deleteAll(Class<?> entityType){
        try {
            dbUtils.deleteAll(entityType);
        } catch (DbException e) {
            ExceptionUtil.showException(e);
        }
    }

    /**
     * sql delete 删除列表中某些行
     */
    public void deleteAll(List<?> ts){
        try {
            dbUtils.delete(ts);
        } catch (DbException e) {
            ExceptionUtil.showException(e);
        }
    }


}