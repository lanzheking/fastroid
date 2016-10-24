package com.honestwalker.androidutils.commons.adapter.listener;

import android.view.View;

import com.honestwalker.androidutils.EventAction.ActionLongClick;
import com.honestwalker.androidutils.commons.adapter.BaseArrayAdapter;

import java.lang.reflect.Method;

/**
 * Created by honestwalker on 16-1-4.
 */
public class BeanHolderAdapterLongClickListener extends ActionLongClick {

    private BaseArrayAdapter adapter;

    public BeanHolderAdapterLongClickListener(BaseArrayAdapter adapter, Object receiver, Method method, Object[] args) {
        super(receiver, method, args);
        this.adapter = adapter;
    }

    @Override
    public boolean onLongClick(View v) {
        super.onLongClick(v);
        adapter.notifyDataSetChanged();
        return false;
    }
}