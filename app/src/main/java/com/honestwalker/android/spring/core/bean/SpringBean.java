package com.honestwalker.android.spring.core.bean;

import com.honestwalker.android.spring.context.FieldType;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by lanzhe on 17-8-31.
 */
public class SpringBean implements Serializable {

    private String classPath;

    private ScopeType scope;

    private boolean lazyInit;

    private HashMap<String, FieldType> fieldType = new HashMap<>();
    private HashMap<String, String> fieldValueMapping = new HashMap<>();

    private HashMap<String, ConstructorArg> constructorArgMapping = new HashMap<>();

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public ScopeType getScope() {
        return scope;
    }

    public void setScope(ScopeType scope) {
        this.scope = scope;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public HashMap<String, FieldType> getFieldType() {
        return fieldType;
    }

    public void setFieldType(HashMap<String, FieldType> fieldType) {
        this.fieldType = fieldType;
    }

    public HashMap<String, String> getFieldValueMapping() {
        return fieldValueMapping;
    }

    public void setFieldValueMapping(HashMap<String, String> fieldValueMapping) {
        this.fieldValueMapping = fieldValueMapping;
    }

    public HashMap<String, ConstructorArg> getConstructorArgMapping() {
        return constructorArgMapping;
    }

    public void setConstructorArgMapping(HashMap<String, ConstructorArg> constructorArgMapping) {
        this.constructorArgMapping = constructorArgMapping;
    }
}
