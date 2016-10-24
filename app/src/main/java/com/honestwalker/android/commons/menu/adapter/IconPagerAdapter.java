package com.honestwalker.android.commons.menu.adapter;

/**
 * Created by lanzhe on 15-9-24.
 */
public interface IconPagerAdapter {

    public int getIconResId(int index);

    public String getLabel(int position);

    public int getLabelColorResId(int position);

    public int getLabelSizeResId(int position);

    public int getTabBackgroundResId(int position);

    public int getTabBackgroundColorResId(int position);
}
