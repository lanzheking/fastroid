package com.honestwalker.android.commons.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.honestwalker.android.commons.activity.BaseWebActivityEntry;
import com.honestwalker.android.commons.menu.adapter.MenuPagerAdapter;
import com.honestwalker.android.commons.title.TitleArg;
import com.honestwalker.android.commons.title.TitleArgBuilder;
import com.honestwalker.android.commons.title.TitleBuilder;
import com.honestwalker.android.commons.utils.StartActivityHelper;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.StringUtil;
import com.honestwalker.androidutils.UIHandler;
import com.honestwalker.androidutils.activity.fragment.menubar.MenubarItemBean;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import java.util.ArrayList;

/**
 * Created by honestwalker on 15-12-9.
 */
public class MenuBuilder {

    private MenuConfig menuConfig;

    protected BlurViewPager     pager;
    protected TabPageIndicator  indicator;

    protected ArrayList<BaseTabIconFragment> fragments = new ArrayList<BaseTabIconFragment>();

    private Activity context;
    private IMenuContext menuContext;
    private int menuRawResId;
    private int menuTabLayoutResId;

    private ArrayList<MenubarItemBean> menubarItemBeanList = new ArrayList<>();

    private MenuPagerAdapter adapter;

    private boolean isActivityMenu;

    public MenuConfig build(IMenuContext menuContext , BlurViewPager pager , TabPageIndicator  indicator , int menuTabLayoutResId , Class rClass) {
        this.menuContext = menuContext;
        this.context   = menuContext.getActivity();
        this.pager     = pager;
        this.indicator = indicator;
        this.menuTabLayoutResId = menuTabLayoutResId;
        buildMenu(rClass);
        return menuConfig;
    }

    public MenuConfig build(IMenuContext menuContext , BlurViewPager pager , TabPageIndicator  indicator, int menuTabLayoutResId , int menuRawResId , Class rClass) {
        this.menuContext = menuContext;
        this.context   = menuContext.getActivity();
        this.pager     = pager;
        this.indicator = indicator;
        this.menuRawResId = menuRawResId;
        this.menuTabLayoutResId = menuTabLayoutResId;
        buildMenu(rClass);
        return menuConfig;
    }

    public MenuConfig build(IMenuContext menuContext , BlurViewPager pager , TabPageIndicator  indicator, int menuTabLayoutResId , ArrayList<MenubarItemBean> menubarItemBeanList , Class rClass) {
        this.menuContext = menuContext;
        this.context   = menuContext.getActivity();
        this.pager     = pager;
        this.indicator = indicator;
        this.menuTabLayoutResId = menuTabLayoutResId;
        this.menubarItemBeanList = menubarItemBeanList;
        buildMenu(rClass);
        return menuConfig;
    }

    private void buildMenu(Class rClass) {

        menuConfig = new MenuConfig();

        if(menuRawResId > 0) {
            try {
                menuConfig.initMenu(context, rClass, menuRawResId);
            } catch (Exception e){}
        } else if(menubarItemBeanList != null){
            menuConfig.initMenu(menubarItemBeanList);
        }

        buildMenufragment();
    }

    private void buildMenufragment() {

        try {

            fragments.clear();
            for(MenubarItemBean menubarItem : menuConfig.getMenubarItemBeans()) {

                String target = menubarItem.getMenubarPageBean().getTarget();
                if(StringUtil.isEmptyOrNull(target)) {
                    target = Target.FRAGMENT;
                }

                BaseTabIconFragment targetFragment = null;

                Bundle data = menubarItem.getMenubarPageBean().getData();

                if(Target.FRAGMENT.equals(target)) {
                    targetFragment = (BaseTabIconFragment) Fragment.instantiate(context, menubarItem.getMenubarPageBean().getTargetClass().getName());
                } else if(Target.FRAGMENT_WEB.equals(target)) {
                    targetFragment = (BaseWebViewFragment) Fragment.instantiate(context, menubarItem.getMenubarPageBean().getTargetClass().getName());
                } else if(Target.ACTIVITY.equals(target)) {
                    targetFragment = new BaseTabIconFragment();
                } else if(Target.ACTIVITY_WEB.equals(target)) {
                    targetFragment = new BaseTabIconFragment();
                }

                if(targetFragment != null) {
                    if(data != null) targetFragment.setArguments(data);
                    targetFragment.setMenubarItemBean(menubarItem);
                    fragments.add(targetFragment);
                }

            }

        } catch (Exception e) {
            LogCat.d("MENU", "菜单解析失败");
            ExceptionUtil.showException("MENU", e);
        }
        updateMenuItem();

        if(fragments.size() > 0) {
            //new TitleBuilder(context, TitleArgBuilder.getTitle(fragments.get(0).getMenubarItemBean().getMenubarPageBean().getTitle()));
            changeTitle(0);
        }

    }

    /**
     * 更新菜单对应Fragments页面
     */
    private void updateMenuItem(){

        if(adapter == null) {
            adapter = new MenuPagerAdapter(
                    getFragmentManager() , fragments );

        }

        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(fragments.size());

        indicator.setMenuTabLayoutResId(menuTabLayoutResId);

        indicator.setMenuConfig(menuConfig);

        pager.setTabPageIndicator(indicator);

        pager.setDisableDragScroll(true);
        indicator.setOnPageChangeListener(onPageChangeListener);

        adapter.notifyDataSetChanged();
        indicator.notifyDataSetChanged();

    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
            menuContext.onPageScrolled(i, v, i1);
        }

        @Override
        public void onPageSelected(int i) {

            BaseTabIconFragment fragment = fragments.get(i);
            MenubarItemBean menubarItemBean = fragment.getMenubarItemBean();
            String pageTarget = menubarItemBean.getMenubarPageBean().getTarget();
            String targetValue = fragment.getMenubarItemBean().getMenubarPageBean().getTargetUrl();
            if(Target.FRAGMENT.equals(pageTarget)) {
            } else if(Target.FRAGMENT_WEB.equals(pageTarget)) {
            } else if(Target.ACTIVITY.equals(pageTarget)) {
                try {
                    Intent intent = new Intent();
                    intent.putExtra("title" , menubarItemBean.getMenubarPageBean().getTitle());
                    StartActivityHelper.toActivity(context, Class.forName(targetValue));
                } catch (Exception e) {
                    ExceptionUtil.showException(e);
                }
            } else if(Target.ACTIVITY_WEB.equals(pageTarget)) {
                BaseWebActivityEntry.toActivity(context, targetValue);
                UIHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        indicator.setCurrentItem(0);
                    }
                }, 500);
            }

            // 如果重写了onTitleChanged 并且有返回的titleArg ， 使用重写的titleArg ， 否则使用默认的title样式
            changeTitle(i);

            menuContext.onPageSelected(i);

        }

        @Override
        public void onPageScrollStateChanged(int i) {
            menuContext.onPageScrollStateChanged(i);
        }
    };

    private FragmentManager getFragmentManager() {
        if(menuContext instanceof FragmentActivity) {
            return ((FragmentActivity)menuContext).getSupportFragmentManager();
        } else if(menuContext instanceof Fragment) {
            return ((Fragment)menuContext).getChildFragmentManager();
        } else {
            return null;
        }
    }

    public MenubarItemBean getMenubarItemBean(int position) {
        if(position > menuConfig.getMenubarItemBeans().size() - 1 || position < 0) {
            return null;
        }
        return menuConfig.getMenubarItemBeans().get(position);
    }

    private void changeTitle(int i){
        TitleArg titleArg = menuContext.onTitleChange(i , fragments.get(i).getMenubarItemBean().getMenubarPageBean().getTitle() , fragments.get(i).getMenubarItemBean());
        if(titleArg == null) {
            new TitleBuilder(context,TitleArgBuilder.getTitle(fragments.get(i).getMenubarItemBean().getMenubarPageBean().getTitle()));
        } else {
            new TitleBuilder(context,titleArg);
        }
    }

}