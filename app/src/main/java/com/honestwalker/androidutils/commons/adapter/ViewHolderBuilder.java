package com.honestwalker.androidutils.commons.adapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import android.view.View;

import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.exception.ExceptionUtil;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ViewHolderBuilder {
	
	private Class viewHolderParent;
	private BaseArrayAdapter adapter;
	
	public ViewHolderBuilder(Class viewHolderParent , BaseArrayAdapter adapter) {
		this.viewHolderParent = viewHolderParent;
		this.adapter = adapter;
	}
	
	public <T> T getViewHolder(View convertView , Class<? extends BaseViewHolder> viewHolder) {
		if(convertView.getTag() == null) {
			try {
				
				boolean isViewHolderInnerClass = false;
				
				LogCat.d("Holder", "创建 viewHolderParent=" + viewHolderParent);
				LogCat.d("Holder", "viewHolder=" + viewHolder.toString());
				if(viewHolder.toString().indexOf("$") > -1) {
					isViewHolderInnerClass = true;
				} else {
					isViewHolderInnerClass = false;
				}
				
				if(isViewHolderInnerClass) {	// viewHolder时内部类时
					Constructor c = viewHolder.getDeclaredConstructor(viewHolderParent , View.class);
					c.setAccessible(true);
					convertView.setTag((T) c.newInstance(adapter , convertView));
				} else {
					Constructor c = viewHolder.getDeclaredConstructor(View.class);
					c.setAccessible(true);
					convertView.setTag((T) c.newInstance(convertView));
				}

				T t = (T) convertView.getTag();

				findView(convertView , t);
				
				return t;
			} catch (Exception e) {
				ExceptionUtil.showException(e);
			}
			return null;
		} else {
			return (T) convertView.getTag();
		}
	}

	private void findView(View convertView , Object t) {
		Field[] fields = t.getClass().getDeclaredFields();
		for(Field field : fields) {
			field.setAccessible(true);
			ViewInject viewInject = field.getAnnotation(ViewInject.class);
			if(viewInject != null) {
				try {
					LogCat.d("vvv" , "viewInject.value()=" + viewInject.value() + "  t=" + t);
					field.set(t , convertView.findViewById(viewInject.value()));
				} catch (IllegalAccessException e) {}
			}
		}
	}
	
}
