package com.honestwalker.androidutils.activity.fragment.menubar;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.StringUtil;
import com.honestwalker.androidutils.views.BaseMyViewLinearLayout;
import com.honestwalker.androidutils.views.QtyView;

public class MenubarLayout extends BaseMyViewLinearLayout implements OnClickListener {
	
	private final String TAG = "MENU";
	
	private MenubarBean menubarBean;
	
	private QtyView[] qtyView;
	
	private View tabViews[];
	private ImageView iconViews[];
	private TextView  labelTVs[];
	
	private int currentSelected = 0;
	
	private OnMenubarChangeListener onMenubarChangeListener;
	
	private MenubarFragmentActivity menubarActivity;

	public MenubarLayout(Context context) {
		super(context);
		init();
	}
	
	public MenubarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public MenubarLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	protected void init() {
	}

	@Override
	protected int contentViewLayout() {
		return 0;
	}

	public void initMenubarLayout(MenubarFragmentActivity menubarActivity) {
		this.menubarActivity = menubarActivity;
//		createMenuItem();
	}
	
//	private void createMenuItem() {
//
//		qtyView = new QtyView[menubarBean.getMenubarItemBeans().length];
//
//		tabViews = new View[menubarBean.getMenubarItemBeans().length];
//
//		iconViews = new ImageView[menubarBean.getMenubarItemBeans().length];
//
//		labelTVs = new TextView[menubarBean.getMenubarItemBeans().length];
//
//
//		int index = 0;
//		for(MenubarItemBean menuItemBean : menubarBean.getMenubarItemBeans()) {
//
//			View tabView = inflate(getContext(), R.layout.view_menubar_tab, null);
//
//			tabView.setLayoutParams(new LinearLayout.LayoutParams(
//					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
//
//			ImageView iconIV = (ImageView) tabView.findViewById(R.id.tab_iv);
//			if(menuItemBean.getIconResId() > 0) {
//				iconIV.setImageResource(menuItemBean.getIconResId());
//			} else {
//				iconIV.setVisibility(View.GONE);
//			}
//			iconViews[index] = iconIV;
//
//			TextView labelTV = (TextView) tabView.findViewById(R.id.tab_label);
//			labelTVs[index] = labelTV;
//			labelTVs[index].setSelected(false);
//			if(!StringUtil.isEmptyOrNull(menuItemBean.getText())) {
//				labelTVs[index].setText(menuItemBean.getText());
//
//				if(menuItemBean.getLabelColorResId() > 0) {
//					XmlPullParser xrp = getResources().getXml(menuItemBean.getLabelColorResId());
//					try {
//						ColorStateList csl = ColorStateList.createFromXml(getResources(), xrp);
//						labelTVs[index].setTextColor(csl);
//					} catch (Exception e) {}
//				}
//
//			} else {
//				labelTVs[index].setVisibility(View.GONE);
//			}
//
//			qtyView[index] = (QtyView) tabView.findViewById(R.id.tag_qtyview);
//
//			View menuTabBG = tabView.findViewById(R.id.menu_tab_bg);
//			menuTabBG.setBackgroundResource(menubarBean.getBackground_res());
//			menuTabBG.setClickable(true);
//			menuTabBG.setOnClickListener(this);
//
//			tabViews[index] = menuTabBG;
//
//			tabViews[index].setTag(index);
//
//			index++;
//
//			addView(tabView);
//		}
//
//		tabViews[0].setSelected(true);
//		iconViews[0].setSelected(true);
//		labelTVs[0].setSelected(true);
//
//	}
	
	public void setMenubarBean(MenubarBean menubarBean) {
		this.menubarBean = menubarBean;
	}
	
	/**
	 * 获取菜单项数量
	 * @return
	 */
	public int getMenuItemCount() {
		if(this.menubarBean.getMenubarItemBeans() == null) return 0;
		return menubarBean.getMenubarItemBeans().length;
	}
	
	/**
	 * 设置指定菜单项的qty数量
	 * @param index
	 * @param qty
	 */
	void setQty(int index , int qty) {
		if(qty < 0) qty = 0;
		if(index >= 0 && index < getMenuItemCount()) {
			qtyView[index].setQty(qty);
		}
	}

	@Override
	public void onClick(View arg0) {
		int clickIndex = (Integer) arg0.getTag();
		setSelectMenubarTabPage(clickIndex);
	}
	
	/**
	 * 清空所有按钮的选择状态
	 */
	private void clearSelector() {
		for(View view : tabViews) {
			view.setSelected(false);
		}
		for(ImageView iconIV : iconViews) {
			iconIV.setSelected(false);
		}
		for(TextView tv : labelTVs) {
			tv.setSelected(false);
		}
	}
	
	public int getSelectedIndex() {
		return currentSelected;
	}

	/**
	 * 切换页面
	 * @param index
	 */
	void setSelectMenubarTabPage(int index) {
		if(this.onMenubarChangeListener != null) {
			this.onMenubarChangeListener.onChange(index);
		}
		
		lightMenubarTabPage(index);
		
		menubarActivity.changeTabPage(index);
		
		if(this.onMenubarChangeListener != null) {
			this.onMenubarChangeListener.onChanged(index);
		}
		currentSelected = index;
		
	}
	
	/**
	 * 点亮指定标签页 ， 不会切换页面
	 * @param index
	 */
	void lightMenubarTabPage(int index) {
		if(index == currentSelected) return;
		if(index < 0 ) index = 0;
		if(index >= getMenuItemCount()) index = getMenuItemCount() - 1;
		
		clearSelector();
		
		tabViews[index].setSelected(true);
		iconViews[index].setSelected(true);
		labelTVs[index].setSelected(true);
		
	}
	
	/**
	 * 设置menubar 行为监听其
	 * @param listener
	 */
	public void setOnMenuBarChangeListener(OnMenubarChangeListener listener) {
		this.onMenubarChangeListener = listener;
	}
	
	/** 获取当前选择页数 */
	public int getCurrentSelected() {
		return this.currentSelected;
	}
	
}
