package com.honestwalker.android.commons.menu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.honestwalker.android.commons.menu.BaseTabIconFragment;
import com.honestwalker.androidutils.IO.LogCat;

import java.util.ArrayList;

/**
 * Created by honestwalker on 15-9-24.
 */
public class MenuPagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

    private ArrayList<BaseTabIconFragment> fragments = new ArrayList<BaseTabIconFragment>();

    public MenuPagerAdapter(FragmentManager fm, ArrayList<BaseTabIconFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position % getCount());
    }

    @Override
    public String getPageTitle(int position) {
        return fragments.get(position % getCount()).getMenubarItemBean().getMenubarPageBean().getTitle();
    }

    @Override
    public String getLabel(int position) {
        LogCat.d("MENU" , "position=" + position);
        LogCat.d("MENU" , "fragments=" + fragments);
        LogCat.d("MENU" , "fragments.get(position % getCount())=" + (fragments.get(position % getCount())));
        LogCat.d("MENU" , "fragments.get(position % getCount()).getMenubarItemBean()=" + (fragments.get(position % getCount()).getMenubarItemBean()));
        LogCat.d("MENU" , "fragments.get(position % getCount()).getMenubarItemBean().getLabel()=" + fragments.get(position % getCount()).getMenubarItemBean().getLabel());
        return fragments.get(position % getCount()).getMenubarItemBean().getLabel();
    }

    @Override
    public int getLabelColorResId(int index) {
        return fragments.get(index % getCount()).getMenubarItemBean().getLabelColorResId();
    }

    @Override
    public int getLabelSizeResId(int index) {
        return fragments.get(index % getCount()).getMenubarItemBean().getLabelSizeResId();
    }

    @Override
    public int getTabBackgroundResId(int index) {
        return fragments.get(index % getCount()).getMenubarItemBean().getTabBackgroundResId();
    }

    @Override
    public int getTabBackgroundColorResId(int index) {
        return fragments.get(index % getCount()).getMenubarItemBean().getTabBackgroundColorResId();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getIconResId(int index){
        return fragments.get(index % getCount()).getMenubarItemBean().getIconResId();
    }



}