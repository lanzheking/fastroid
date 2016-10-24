package com.honestwalker.android.commons.menu;

import android.app.Activity;

import com.honestwalker.android.commons.title.TitleArg;
import com.honestwalker.androidutils.activity.fragment.menubar.MenubarItemBean;

/**
 * Created by honestwalker on 15-12-9.
 */
public interface IMenuContext {
    public Activity getActivity();

    public void onPageScrolled(int i, float v, int i1);

    public void onPageSelected(int i);

    public void onPageScrollStateChanged(int i);

    public TitleArg onTitleChange(int position, String title, MenubarItemBean menubarItemBean);

}
