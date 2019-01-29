package com.honestwalker.androidutils.IO;


import com.google.gson.Gson;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * Created by lanzhe on 17-6-22.
 */

public class ObjectCopy {



    /**
     * 列表反射复制
     *
     * @param src
     * @param desc
     * @param descClass
     */
    public static void reflectCopyArray(List src, List desc, Class descClass) {
        desc.clear();
        for (Object o : src) {
            Object descObj;
            try {
                descObj = descClass.newInstance();
            } catch (Exception e) {
                continue;
            }
            reflectCopy(o, descObj);
            desc.add(descObj);
        }
    }

    /**
     * 反射复制
     *
     * @param src
     * @param desc
     */
    public static void reflectCopy(Object src, Object desc) {
        reflectCopy(src, desc, null);
    }

    public static void reflectCopy(Object src, Object desc, boolean setNullValue) {
        reflectCopy(src, desc, setNullValue, null);
    }

    public static void reflectCopy(Object src, Object desc, ObjectReflectCoptListener objectReflectListener) {
        reflectCopy(src, desc, false, objectReflectListener);
    }

    /**
     * 反射复制，把源对象非空字段复制到目标对象的空字段，其他忽略
     */
    public static void reflectCopyToNullValue(Object src, Object desc) {
        if (null == src || null == desc) {
            return;
        }

        Field[] srcFeidls = src.getClass().getDeclaredFields();
        Field[] descFields = desc.getClass().getDeclaredFields();
        for (Field srcField : srcFeidls) {
            srcField.setAccessible(true);
            for (Field descField : descFields) {
                descField.setAccessible(true);
                if (srcField.getName().equals(descField.getName())) {
                    try {
                        if (srcField.get(src) != null) {

                            Object srcValue = srcField.get(src);

                            Object descValue = descField.get(desc);

                            if (descValue != null) continue;

                            Object value = getValueByType(descField, srcValue);

                            if (descField.getType().equals(Integer.class)) {
                                value = Integer.parseInt(value + "");
                            } else if (descField.getType().equals(String.class)) {
                                value = value;
                            } else if (descField.getType().equals(Float.class)) {
                                value = Float.parseFloat(value + "");
                            } else if (descField.getType().equals(Long.class)) {
                                value = Long.parseLong(value + "");
                            } else if (descField.getType().equals(Short.class)) {
                                value = Short.parseShort(value + "");
                            } else if (descField.getType().equals(Timestamp.class)) {
                                String dateStr = value + "";
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                java.util.Date date = sdf.parse(dateStr);
                                value = new Timestamp(date.getTime());
                            } else if (descField.getType().equals(Double.class)) {
                                value = Double.parseDouble(value + "");
//                            } else if(descField.getType().equals(Integer[].class)) {
//                            } else if(descField.getType().equals(String[].class)) {
//                            } else if(descField.getType().equals(Date.class)) {
                            }
                            descField.set(desc, value);
                        }
                    } catch (Exception e) {
                    }
                    continue;
                }
            }
        }
    }

    /**
     * 反射复制， 只复制目标空值
     * @param src
     * @param desc
     * @param objectReflectListener
     */
    public static void reflectCopySetNullOnly(Object src, Object desc) {
        reflectCopySetNullOnly(src , desc, null);
    }

    /**
     * 反射复制， 只复制目标空值
     * @param src
     * @param desc
     * @param objectReflectListener
     */
    public static void reflectCopySetNullOnly(Object src, Object desc, ObjectReflectCoptListener objectReflectListener) {
        if (null == src || null == desc) {
            return;
        }

        Field[] srcFeidls = src.getClass().getDeclaredFields();
        Field[] descFields = desc.getClass().getDeclaredFields();
        for (Field srcField : srcFeidls) {
            srcField.setAccessible(true);
            for (Field descField : descFields) {
                descField.setAccessible(true);
                try {
                    Object descValue = descField.get(desc);
                    if(descValue != null) continue;
                } catch (Exception e){}

                if (srcField.getName().equals(descField.getName())) {
                    try {
                        if (srcField.get(src) != null) {

                            Object srcValue = srcField.get(src);

                            Object value = getValueByType(descField, srcValue);

                            if (descField.getType().equals(Integer.class)) {
                                value = Integer.parseInt(value + "");
                            } else if (descField.getType().equals(String.class)) {
                                value = value;
                            } else if (descField.getType().equals(Float.class)) {
                                value = Float.parseFloat(value + "");
                            } else if (descField.getType().equals(Long.class)) {
                                value = Long.parseLong(value + "");
                            } else if (descField.getType().equals(Short.class)) {
                                value = Short.parseShort(value + "");
                            } else if (descField.getType().equals(Timestamp.class)) {
                                String dateStr = value + "";
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                java.util.Date date = sdf.parse(dateStr);
                                value = new Timestamp(date.getTime());
                            } else if (descField.getType().equals(Double.class)) {
                                value = Double.parseDouble(value + "");
//                            } else if(descField.getType().equals(Integer[].class)) {
//                            } else if(descField.getType().equals(String[].class)) {
//                            } else if(descField.getType().equals(Date.class)) {
                            }
                            descField.set(desc, value);
                        }
                    } catch (Exception e) {
                    }
                    if (objectReflectListener != null) {
                        try {
                            objectReflectListener.onCopyField(src, desc, srcField, descField, srcField.getName(), srcField.get(src));
                        } catch (Exception e) {
                        }
                    }

                    continue;
                }
            }
        }
    }

    /**
     * 反射复制
     *
     * @param src                   来源
     * @param desc                  目标
     * @param setNullValue          空值也复制
     * @param objectReflectListener
     */
    public static void reflectCopy(Object src, Object desc, boolean setNullValue, ObjectReflectCoptListener objectReflectListener) {
        if (null == src || null == desc) {
            return;
        }

        Field[] srcFeidls = src.getClass().getDeclaredFields();
        Field[] descFields = desc.getClass().getDeclaredFields();
        for (Field srcField : srcFeidls) {
            srcField.setAccessible(true);
            for (Field descField : descFields) {
                descField.setAccessible(true);
                if (srcField.getName().equals(descField.getName())) {
                    try {
                        if (setNullValue || srcField.get(src) != null) {

                            Object srcValue = srcField.get(src);

                            Object value = getValueByType(descField, srcValue);

                            if (descField.getType().equals(Integer.class)) {
                                value = Integer.parseInt(value + "");
                            } else if (descField.getType().equals(String.class)) {
                                value = value;
                            } else if (descField.getType().equals(Float.class)) {
                                value = Float.parseFloat(value + "");
                            } else if (descField.getType().equals(Long.class)) {
                                value = Long.parseLong(value + "");
                            } else if (descField.getType().equals(Short.class)) {
                                value = Short.parseShort(value + "");
                            } else if (descField.getType().equals(Timestamp.class)) {
                                String dateStr = value + "";
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                java.util.Date date = sdf.parse(dateStr);
                                value = new Timestamp(date.getTime());
                            } else if (descField.getType().equals(Double.class)) {
                                value = Double.parseDouble(value + "");
//                            } else if(descField.getType().equals(Integer[].class)) {
//                            } else if(descField.getType().equals(String[].class)) {
//                            } else if(descField.getType().equals(Date.class)) {
                            }
                            descField.set(desc, value);
                        }
                    } catch (Exception e) {
                    }
                    if (objectReflectListener != null) {
                        try {
                            objectReflectListener.onCopyField(src, desc, srcField, descField, srcField.getName(), srcField.get(src));
                        } catch (Exception e) {
                        }
                    }

                    continue;
                }
            }
        }
    }


    /**
     * 根据属性类型，自动赋值
     *
     * @param descField 目标字段
     * @param srcValue  源对象的值
     * @return 得到目标对象属性对应的值
     */
    public static Object getValueByType(Field descField, Object srcValue) {
        if(srcValue == null) return null;
        Object value;
        if (descField.getType().equals(Integer.class)) {
            value = Integer.parseInt(srcValue + "");
        } else if (descField.getType().equals(String.class)) {
            value = srcValue;
        } else if (descField.getType().equals(Float.class)) {
            value = Float.parseFloat(srcValue + "");
        } else if (descField.getType().equals(Long.class)) {
            value = Long.parseLong(srcValue + "");
        } else if (descField.getType().equals(Short.class)) {
            value = Short.parseShort(srcValue + "");
        } else if (descField.getType().equals(Double.class)) {
            value = Double.parseDouble(srcValue + "");
        } else if (descField.getType().equals(Date.class)) {
            Date date = new Date(((Date) srcValue).getTime());
            value = date;
        } else {
            value = srcValue;
        }
        return value;
    }

    /**
     * json转换复制
     */
    public static <T> T jsonCopy(Object src, Class<T> descClass) {
        Gson gson = new Gson();
        String srcJson = gson.toJson(src);
        return gson.fromJson(srcJson, descClass);
    }

}
