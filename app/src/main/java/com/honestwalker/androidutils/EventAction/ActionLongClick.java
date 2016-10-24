package com.honestwalker.androidutils.EventAction;

import java.lang.reflect.Method;

import android.view.View;
import android.view.View.OnLongClickListener;

import com.honestwalker.androidutils.exception.ExceptionUtil;

/**
 * long click事件反射方式调用
 * @author honestwalker
 *
 */
public class ActionLongClick implements OnLongClickListener {
	private Method method;
	private Object[] args;
	private Object receiver;
	public ActionLongClick(Object receiver,Method method , Object[] args) {
		this.args     = args;
		this.method   = method;
		this.receiver  =  receiver;
	}
	@Override
	public boolean onLongClick(View v) {
		try {
			method.invoke(receiver, args);
		} catch (Exception e) {
			ExceptionUtil.showException(e);
		}
		return false;
	}
}
