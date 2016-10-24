package com.honestwalker.androidutils.commons.adapter.listener;

import android.view.View;

import com.honestwalker.androidutils.EventAction.ActionLongClick;
import com.honestwalker.androidutils.commons.adapter.BaseArrayAdapter;
import com.honestwalker.androidutils.commons.adapter.BaseMounter;

import java.lang.reflect.Method;

/**
 * Created by honestwalker on 16-1-4.
 */
public class BeanHolderMounterLongClickListener extends ActionLongClick {

    private BaseMounter mounter;

    public BeanHolderMounterLongClickListener(BaseMounter mounter, Object receiver, Method method, Object[] args) {
        super(receiver, method, args);
        this.mounter = mounter;
    }

    @Override
    public boolean onLongClick(View v) {
        super.onLongClick(v);
        return false;
    }
}