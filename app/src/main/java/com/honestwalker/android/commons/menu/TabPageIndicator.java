package com.honestwalker.android.commons.menu;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.honestwalker.android.commons.menu.adapter.IconPagerAdapter;
import com.honestwalker.androidutils.activity.fragment.menubar.MenubarItemBean;

public class TabPageIndicator extends LinearLayout implements PageIndicator {
    /**
     * Title text used when no title is provided by the adapter.
     */
    private static final CharSequence EMPTY_TITLE = "";

    /**
     * Interface for a callback when the selected tab has been reselected.
     */
    public interface OnTabReselectedListener {
        /**
         * Callback when the selected tab has been reselected.
         *
         * @param position Position of the current center item.
         */
        void onTabReselected(int position);
    }

    private Runnable mTabSelector;

    private int oldSelectedIndex;

    private int menuTabLayoutResId;

    private MenuConfig menuConfig;

    public int getOldSelectedIndex() {
        return oldSelectedIndex;
    }

    private OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
            TabView tabView = (TabView) view;
            oldSelectedIndex = mViewPager.getCurrentItem();
            final int newSelected = tabView.getIndex();
            mViewPager.setCurrentItem(newSelected);
            if (oldSelectedIndex == newSelected && mTabReselectedListener != null) {
                mTabReselectedListener.onTabReselected(newSelected);
            }
        }
    };

    public OnClickListener getmTabClickListener() {
        return mTabClickListener;
    }

    /**
     * True to enable icon color and text color render while scrolling ,false otherwise.
     */
    private boolean enableColorRenderWhileScrolling = true;

    /**
     * Set true to enable icon color and text color render while scrolling ,false otherwise.
     */
    public void setEnableColorRenderWhileScrolling(
            boolean enableColorRenderWhileScrolling) {
        this.enableColorRenderWhileScrolling = enableColorRenderWhileScrolling;
    }

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mListener;

    private int mSelectedTabIndex;

    private OnTabReselectedListener mTabReselectedListener;

    public TabPageIndicator(Context context) {
        this(context, null);
    }

    public TabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
    }

    public void setOnTabReselectedListener(OnTabReselectedListener listener) {
        mTabReselectedListener = listener;
    }

    public void setMenuTabLayoutResId(int menuTabLayoutResId) {
        this.menuTabLayoutResId = menuTabLayoutResId;
    }

    public int getMenuTabLayoutResId() {
        return this.menuTabLayoutResId;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        setCurrentItem(mSelectedTabIndex);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTabSelector != null) {
            post(mTabSelector);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
    }

    private void addTab(int index, CharSequence text, int tabBackgroundResId , int tabBackgroundColorResId ,int iconResId , int labelColorResId , int labelSizeResId) {

        TabView tabContentView = new TabView(getContext() , this);

        if(tabBackgroundResId > 0) {
            tabContentView.setBackgroundResource(tabBackgroundResId);
        }
        if(tabBackgroundColorResId > 0) {
            tabContentView.setBackgroundColor(tabBackgroundColorResId);
        }

        if(tabContentView.getTabTV() != null) {
            if(text != null) {
                tabContentView.getTabTV().setText(text);
                if(labelColorResId > 0) {
                    tabContentView.getTabTV().setTextColor(getContext().getResources().getColorStateList(labelColorResId));
                }
                if(labelSizeResId > 0) {
//                    tabContentView.getTabTV().setTextSize(labelSizeResId);
                    tabContentView.getTabTV().setTextSize(TypedValue.COMPLEX_UNIT_PX , getContext().getResources().getDimension(labelSizeResId));
                }
            } else {
                tabContentView.getTabTV().setVisibility(View.GONE);
            }
        }

        if(tabContentView.getTabIV() != null) {
            if(iconResId > 0) {
                tabContentView.getTabIV().setImageResource(iconResId);
            } else {
                tabContentView.getTabIV().setVisibility(View.GONE);
            }
        }

        tabContentView.setmIndex(index);

        tabContentView.getTabTV().setVisibility(View.GONE);

        addView(tabContentView, new LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {

        MenubarItemBean bean = getMenuConfig().getMenubarItemBeans().get(position);
        String target = bean.getMenubarPageBean().getTarget();

        if(!Target.ACTIVITY_WEB.equals(target) && !Target.ACTIVITY.equals(target)) {
            setCurrentItem(position);
        }

        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }

    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        notifyDataSetChanged();
    }

    public void notifyIconQtyChanged(int index, int qty) {
        if (index > getChildCount() - 1) return;
        for (int i = 0; i < getChildCount(); i++) {
            TabView tabView = (TabView) getChildAt(i);
            if (tabView != null && index == i) {
                tabView.setQtyText(qty);
                invalidate();
                break;
            }
        }
    }

    public void notifyDataSetChanged() {
        removeAllViews();
        PagerAdapter adapter = mViewPager.getAdapter();
        IconPagerAdapter iconAdapter = null;
        if (adapter instanceof IconPagerAdapter) {
            iconAdapter = (IconPagerAdapter) adapter;
        }
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            CharSequence label = iconAdapter.getLabel(i);
            if (label == null) {
                label = EMPTY_TITLE;
            }
            addTab(i, label, iconAdapter.getTabBackgroundResId(i) , iconAdapter.getTabBackgroundColorResId(i)  , iconAdapter.getIconResId(i) , iconAdapter.getLabelColorResId(i) , iconAdapter.getLabelSizeResId(i));

        }
        if (mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        setCurrentItem(mSelectedTabIndex);
        requestLayout();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            return;
//            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mSelectedTabIndex = item;
        mViewPager.setCurrentItem(item);

        final int tabCount = getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final TabView child = (TabView) getChildAt(i);
            final boolean isSelected = (i == mSelectedTabIndex);
            if (child != null) {
                child.setSelected(isSelected);
            }
        }
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }

//    public void gradientAlpha(int leftIndex, int rightIndex, float leftAlpha, float rightAlpha) {
//        if (enableColorRenderWhileScrolling) {
//            //左
//            if (getChildAt(leftIndex) != null) {
//                ((TabView) getChildAt(leftIndex)).getTabIV().setAlpha(leftAlpha);
//                ((TabView) getChildAt(leftIndex)).getTabTV().setAlpha(leftAlpha);
//                ((TabView) getChildAt(leftIndex)).getTabInactiveTV().setAlpha(rightAlpha);
//            }
//            //右
//            if (getChildAt(rightIndex) != null) {
//                ((TabView) getChildAt(rightIndex)).getTabIV().setAlpha(rightAlpha);
//                ((TabView) getChildAt(rightIndex)).getTabTV().setAlpha(rightAlpha);
//                ((TabView) getChildAt(rightIndex)).getTabInactiveTV().setAlpha(leftAlpha);
//            }
//        }
//    }

    public MenuConfig getMenuConfig() {
        return menuConfig;
    }

    public void setMenuConfig(MenuConfig menuConfig) {
        this.menuConfig = menuConfig;
    }

}
