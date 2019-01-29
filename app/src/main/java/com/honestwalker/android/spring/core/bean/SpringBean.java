package com.honestwalker.android.spring.core.bean;

import com.honestwalker.android.spring.context.FieldType;

import java.util.HashMap;

/**
 * Created by lanzhe on 17-8-31.
 */

public class SpringBean {

    private String classPath;

    private Scope scope;

    private HashMap<String, String> fieldValueMapping = new HashMap<>();

    private HashMap<String, FieldType> fieldType = new HashMap<>();

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public HashMap<String, String> getFieldValueMapping() {
        return fieldValueMapping;
    }

    public void setFieldValueMapping(HashMap<String, String> fieldValueMapping) {
        this.fieldValueMapping = fieldValueMapping;
    }

    public HashMap<String, FieldType> getFieldType() {
        return fieldType;
    }

    public void setFieldType(HashMap<String, FieldType> fieldType) {
        this.fieldType = fieldType;
    }
}
