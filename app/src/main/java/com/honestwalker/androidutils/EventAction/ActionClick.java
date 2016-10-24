package com.honestwalker.androidutils.EventAction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.view.View;
import android.view.View.OnClickListener;

import com.honestwalker.androidutils.exception.ExceptionUtil;

public class ActionClick implements OnClickListener {

	private Method method;
	private Object[] args;
	private Object receiver;
	public ActionClick(Object receiver,Method method , Object[] args) {
		this.args    = args;
		this.method  = method;
		this.receiver = receiver;
	}
	@Override
	public void onClick(View v) {
		try {
			method.invoke(receiver, args);
		} catch (IllegalArgumentException e1) {
			ExceptionUtil.showException("AndroidRuntime", e1);
		} catch (IllegalAccessException e1) {
			ExceptionUtil.showException("AndroidRuntime", e1);
		} catch (InvocationTargetException e1) {
			ExceptionUtil.showException("AndroidRuntime", e1);
		}
	}

}
