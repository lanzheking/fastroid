package com.honestwalker.androidutils.commons.adapter;

import android.view.View;

import java.util.HashMap;

/**
 * Depiction:
 * <p/>
 * Auth         :  zhe.lan@honestwalker.com <br />
 * Add Date     :  16-3-7 下午3:02. <br />
 * Rewrite Date :  16-3-7 下午3:02. <br />
 */
public interface OnBeanHolderItemLoaded<T> {

    public void onItemLoaded(View contentView, T item, int position, HashMap<Integer, View> viewMapping);

}
