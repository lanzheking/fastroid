package com.honestwalker.androidutils.commons.adapter.listener;

import android.view.View;

import com.honestwalker.androidutils.EventAction.ActionClick;
import com.honestwalker.androidutils.commons.adapter.BaseArrayAdapter;

import java.lang.reflect.Method;

/**
 * Created by honestwalker on 16-1-4.
 */
public class BeanHolderAdapterClickListener extends ActionClick {

    private BaseArrayAdapter adapter;

    public BeanHolderAdapterClickListener(BaseArrayAdapter adapter, Object receiver, Method method, Object[] args) {
        super(receiver, method, args);
        this.adapter = adapter;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        adapter.notifyDataSetChanged();
    }
}