package com.honestwalker.android.commons;

import android.app.Activity;
import android.os.Bundle;

import com.honestwalker.android.commons.menu.BlurViewPager;
import com.honestwalker.android.commons.menu.IMenuContext;
import com.honestwalker.android.commons.menu.MenuBuilder;
import com.honestwalker.android.commons.menu.TabPageIndicator;
import com.honestwalker.android.commons.title.TitleArg;
import com.honestwalker.android.fastroid.R;
import com.honestwalker.android.kc_commons.ui.activity.BaseFragmentActivity;
import com.honestwalker.android.kc_test.KCTestLauncher;
import com.honestwalker.androidutils.activity.fragment.menubar.MenubarItemBean;

/**
 * 菜单activity 父类
 */
public abstract class MenuActivity extends BaseFragmentActivity implements IMenuContext {

    protected BlurViewPager     pager;
    protected TabPageIndicator  indicator;

    private KCTestLauncher kcTestLauncher;

    private MenuBuilder menuBuilder = new MenuBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ViewUtils.inject(this);
        initView();

        kcTestLauncher = new KCTestLauncher();
        kcTestLauncher.start(this);



    }

    /**
     * 切换菜单
     * @param page
     */
    public void setMenuPage(Integer page) {
        indicator.setCurrentItem(page);
    }

    @Override
    protected void onResume() {
        super.onResume();
        kcTestLauncher.next(this);
    }

    private void initView() {

        pager     = (BlurViewPager) findViewById(R.id.pager);
        indicator = (TabPageIndicator) findViewById(R.id.indicator);

        int menuTabResId = getMenuTabResId();
        menuBuilder.build(this, pager, indicator, menuTabResId, getMenubarResId(), getRClass() );

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {}

    @Override
    public void onPageSelected(int i) {}

    @Override
    public void onPageScrollStateChanged(int i) {}

    @Override
    public TitleArg onTitleChange(int position, String title, MenubarItemBean menubarItemBean) {return null;}

    protected MenubarItemBean getMenubarItemBean(int position) {
        return menuBuilder.getMenubarItemBean(position);
    }

    /**
     * 返回菜单的raw id
     * @return
     */
    protected abstract int getMenubarResId();

    /**
     * 必须返回主项目的R 的 class
     */
    protected abstract Class getRClass();

    /**
     * 可以通过重写该方法来重新指定menu tab 样式 , 可选
     */
    protected int getMenuTabResId() {
        return R.layout.common_view_menu_tab;
    }

}
