package com.honestwalker.androidutils.commons.adapter.listener;

import android.view.View;

import com.honestwalker.androidutils.EventAction.ActionClick;
import com.honestwalker.androidutils.commons.adapter.BaseArrayAdapter;
import com.honestwalker.androidutils.commons.adapter.BaseMounter;

import java.lang.reflect.Method;

/**
 * Created by honestwalker on 16-1-4.
 */
public class BeanHolderMounterClickListener extends ActionClick {

    private BaseMounter mounter;

    public BeanHolderMounterClickListener(BaseMounter mounter, Object receiver, Method method, Object[] args) {
        super(receiver, method, args);
        this.mounter = mounter;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}