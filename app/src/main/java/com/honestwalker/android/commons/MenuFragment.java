package com.honestwalker.android.commons;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.honestwalker.android.fastroid.R;
import com.honestwalker.android.commons.menu.BaseTabIconFragment;
import com.honestwalker.android.commons.menu.BlurViewPager;
import com.honestwalker.android.commons.menu.IMenuContext;
import com.honestwalker.android.commons.menu.MenuBuilder;
import com.honestwalker.android.commons.menu.TabPageIndicator;
import com.honestwalker.androidutils.UIHandler;
import com.honestwalker.androidutils.activity.fragment.menubar.MenubarItemBean;

import java.util.ArrayList;

/**
 * Created by honestwalker on 15-12-9.
 */
public abstract class MenuFragment extends BaseTabIconFragment implements IMenuContext {

    protected View contentView;

    protected BlurViewPager     pager;
    protected TabPageIndicator  indicator;

    private MenuBuilder menuBuilder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_menubar , null);

        UIHandler.init();

        initView();

        return contentView;
    }

    private void initView() {
        pager       = (BlurViewPager) contentView.findViewById(R.id.pager);
        indicator   = (TabPageIndicator) contentView.findViewById(R.id.indicator);
        menuBuilder = new MenuBuilder();
        menuBuilder.build(MenuFragment.this, pager, indicator, getMenuTabLayoutResId() , getRClass() );
//        loadMenu();
    }

//    private void loadMenu() {
//        LoadingHelper.show(getActivity(), "loading");
//
//        UIHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ToastHelper.alert(getActivity(), "异步加载fragment菜单模拟  ");
//                LogCat.d("MENU", "================" + Thread.currentThread().getId());
//                menuBuilder.build(MenuFragment.this, pager, indicator, R.layout.common_view_menu_tab_simple, R.raw.menubar_order);
//                LoadingHelper.dismiss(getActivity());
//            }
//        } , 4000);
//
//    }

    protected void rebuildMenu(int menuConfig) {
        getMenuBuild().build(MenuFragment.this, pager, indicator, getMenuTabLayoutResId(), menuConfig, getRClass() );
    }

    protected void rebuildMenu(ArrayList<MenubarItemBean> menubarItemBeanList) {
        getMenuBuild().build(MenuFragment.this, pager, indicator, getMenuTabLayoutResId(), menubarItemBeanList, getRClass() );
    }

    protected MenuBuilder getMenuBuild() {
        return menuBuilder;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageSelected(int i) {
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    protected abstract int getMenuTabLayoutResId();

    protected abstract Class getRClass();

}
