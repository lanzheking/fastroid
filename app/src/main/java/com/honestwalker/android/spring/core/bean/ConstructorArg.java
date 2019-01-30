package com.honestwalker.android.spring.core.bean;

import com.honestwalker.android.spring.context.FieldType;

import java.io.Serializable;

/**
 * Created by lanzhe on 18-5-28.
 */
public class ConstructorArg implements Serializable {

    /** 记录属性值，不是最终值，是xml bean配置的value值 */
    private String value;

    /** 记录数据类型, constructor-arg 必须指定type */
    private Class type;

    /** 记录引用类型 */
    private FieldType fieldType;

    public ConstructorArg() {}

    public ConstructorArg(String value, Class type, FieldType fieldType) {
        this.value = value;
        this.type = type;
        this.fieldType = fieldType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }
}
