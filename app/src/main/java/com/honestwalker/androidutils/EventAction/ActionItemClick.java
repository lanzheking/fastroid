package com.honestwalker.androidutils.EventAction;

import java.lang.reflect.Method;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.honestwalker.androidutils.exception.ExceptionUtil;

/**
 * item click事件反射方式调用
 * @author honestwalker
 *
 */
public class ActionItemClick implements OnItemClickListener {
	private Method method;
	private Object[] args;
	private Object receiver;
	public ActionItemClick(Object receiver,Method method , Object[] args) {
		this.args      = args;
		this.method    = method;
		this.receiver  =  receiver;
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		try {
			args[0] = arg2;
			method.invoke(receiver, args);
		} catch (Exception e) {
			ExceptionUtil.showException(e);
		}
	}
}