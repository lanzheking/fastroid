package com.honestwalker.androidutils.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.honestwalker.androidutils.equipment.DisplayUtil;

/** 自定义控件父类，包装了一些常用参数 */
public class BaseMyViewRelativeLayout extends RelativeLayout {
	
	private final String defaultNameSpace = "http://schemas.android.com/apk/res/android";
	
	private AttributeSet attrs;
	
	public BaseMyViewRelativeLayout(Context context) {
		super(context);
		this.context = context;
		initParams();
	}
	
	public BaseMyViewRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.attrs = attrs;
		initParams();
	}
	
	public BaseMyViewRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		this.attrs = attrs;
		initParams();
	}


	protected LayoutInflater inflater;
	protected Context context;
	protected int screenWidth;
	protected int screenHeight;
	
	
	private void initParams() {
		inflater = ((Activity)context).getLayoutInflater();
		this.screenWidth = DisplayUtil.getWidth(context);
		this.screenHeight = DisplayUtil.getHeight(context);
		setWillNotDraw(false);
	}
	
	public String getAttributeValue(String attrName) {
		return getAttributeValue(defaultNameSpace , attrName);
	}
	public String getAttributeValue(String namespace , String attrName) {
		if(attrs == null) return null;
		return attrs.getAttributeValue(namespace, attrName);
	}
	
	public String getStringAttributeValue(String namespace , String attrName) {
		return getAttributeValue(namespace, attrName);
	}
	public String getStringAttributeValue(String attrName) {
		return getAttributeValue(attrName);
	}
	
	public Integer getIntAttributeValue(String attrName) {
		String value = getAttributeValue(attrName);
		if(value == null) return null;
		return Integer.parseInt(value);
	}
	public Integer getIntAttributeValue(String namespace , String attrName) {
		String value = getAttributeValue(namespace, attrName);
		if(value == null) return null;
		return Integer.parseInt(value);
	}
	
	public Boolean getBooleanAttributeValue(String attrName) {
		String value = getAttributeValue(attrName);
		if(value == null) return null;
		return Boolean.parseBoolean(value);
	}
	public Boolean getBooleanAttributeValue(String namespace , String attrName) {
		String value = getAttributeValue(namespace, attrName);
		if(value == null) return null;
		return Boolean.parseBoolean(value);
	}
	
	public Long getLongAttributeValue(String attrName) {
		String value = getAttributeValue(attrName);
		if(value == null) return null;
		return Long.parseLong(value);
	}
	public Long getLongAttributeValue(String namespace , String attrName) {
		String value = getAttributeValue(namespace, attrName);
		if(value == null) return null;
		return Long.parseLong(value);
	}
	
}
