package com.honestwalker.androidutils.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.honestwalker.androidutils.equipment.DisplayUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/** 自定义控件父类，包装了一些常用参数 */
public abstract class BaseMyViewLinearLayout extends LinearLayout {
	
	private final String defaultNameSpace = "http://schemas.android.com/apk/res/android";
	
	private AttributeSet attrs;

	private View contentView;
	
	public BaseMyViewLinearLayout(Context context) {
		super(context);
		this.context = context;
		initParams();
		contentView = inflater.inflate(contentViewLayout(), this);
		ViewUtils.inject(this , contentView);
		init();
	}
	
	public BaseMyViewLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.attrs = attrs;
		initParams();
		contentView = inflater.inflate(contentViewLayout(), this);
		ViewUtils.inject(this , contentView);
		init();
	}
	
	public BaseMyViewLinearLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		this.attrs = attrs;
		initParams();
		contentView = inflater.inflate(contentViewLayout(), this);
		ViewUtils.inject(this , contentView);
		init();
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
	
	public int getIntAttributeValue(String attrName) {
		if(attrs == null) return 0;
		return attrs.getAttributeResourceValue(defaultNameSpace, attrName, 0);
	}
	public int getIntAttributeValue(String namespace , String attrName) {
		if(attrs == null) return 0;
		return attrs.getAttributeResourceValue(namespace, attrName , 0);
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

	protected abstract void init();

	protected abstract int contentViewLayout();

	protected View getContentView() {
		return contentView;
	}

}
