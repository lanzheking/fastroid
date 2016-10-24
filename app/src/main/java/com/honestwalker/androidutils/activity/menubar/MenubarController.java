package com.honestwalker.androidutils.activity.menubar;

import com.honestwalker.androidutils.views.QtyView;

/**
 * Menubar控制器
 * @author honestwalker
 *
 */
public final class MenubarController {
	
	private static MenubarController instance;
	private MenubarActivity mainActivity;
	
	private MenubarController(MenubarActivity mainActivity) {
		this.mainActivity = mainActivity;
	}
	
	public static MenubarController getInstance(MenubarActivity mainActivity) {
		if(instance == null) {
			instance = new MenubarController(mainActivity);
		}
		return instance;
	}
	
	public int getTabPageCount() {
		return mainActivity.getTabPageCount();
	}
	
	public void setQty(int index , int qty) {
		QtyView qtyView = mainActivity.getQtyView(index);
		if(qtyView != null) {
			qtyView.setQty(qty);
		}
	}
	
}
