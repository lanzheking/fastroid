package com.honestwalker.androidutils.Init;

import android.content.Context;

/** 
 * “初始化策略” 接口 ， 每个“初始化策略”实现这个接口，
 * 并在init方法中写具体初始化逻辑
 * @author honestwalker
 *
 */
public interface InitAction {
	
	public void init(Context context);
	
}
