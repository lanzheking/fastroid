package com.honestwalker.android.commons.menu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.ViewUtils.ViewSizeHelper;
import com.honestwalker.androidutils.equipment.DisplayUtil;

/**
 * Created by honestwalker on 15-9-24.
 */
public class TabView extends LinearLayout {

    private int mIndex;
    private TextView tabTV;
    private ImageView tabIV;
    private ViewGroup qtyView;
    private TextView qtyTV;

    private TabPageIndicator tabPageIndicator;

    public TabView(Context context , TabPageIndicator tabPageIndicator) {
        super(context);
        this.tabPageIndicator = tabPageIndicator;
        init();
    }

    private void init() {
        inflate(getContext(), tabPageIndicator.getMenuTabLayoutResId(), this);
        setFocusable(true);

        setOnClickListener(tabPageIndicator.getmTabClickListener());

        tabTV = (TextView) findViewById(R.id.menu_tab_tv);

        tabIV = (ImageView) findViewById(R.id.menu_tab_iv);
        qtyView = (ViewGroup) findViewById(R.id.menu_tab_qty_layout);
        qtyTV = (TextView) findViewById(R.id.menu_tab_qty_tv);
        if(qtyView != null) {
            qtyView.setVisibility(View.GONE);
        }
//        tabTV.setTextColor(getResources().getColor(R.color.common_menu_tv_normal));
//        tabInactiveTV.setTextColor(getResources().getColor(R.color.common_menu_tv_selected));

        // 屏幕适配
        if(tabIV != null) {
            int screenWidth = DisplayUtil.getWidth(getContext());
            int iconSize = screenWidth / 15;
            ViewSizeHelper.getInstance(getContext()).setSize(tabIV , iconSize , iconSize);
        }


    }

    public void setQtyText(int qty) {
        if(qtyView == null) return;
        if (qty == 0) {
            qtyView.setVisibility(View.GONE);
        }else {
            qtyView.setVisibility(View.VISIBLE);
            qtyTV.setText(""+qty);
        }
    }

    public int getIndex() {
        return mIndex;
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if(tabIV != null) {
            tabIV.setSelected(selected);
        }
        if(tabTV != null) {
            tabTV.setSelected(selected);
        }

    }

    public int getmIndex() {
        return mIndex;
    }

    public void setmIndex(int mIndex) {
        this.mIndex = mIndex;
    }

    public TextView getTabTV() {
        return tabTV;
    }

    public ImageView getTabIV() {
        return tabIV;
    }

    public void setTabIV(ImageView tabIV) {
        this.tabIV = tabIV;
    }

    public ViewGroup getQtyView() {
        return qtyView;
    }

    public void setQtyView(ViewGroup qtyView) {
        this.qtyView = qtyView;
    }

    public TextView getQtyTV() {
        return qtyTV;
    }

    public void setQtyTV(TextView qtyTV) {
        this.qtyTV = qtyTV;
    }

}
