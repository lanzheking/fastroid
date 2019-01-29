package com.honestwalker.androidutils.activity.fragment.menubar;

/**
 * 菜单数据对象
 */
public class MenubarBean {
	
	private int background_res;

	private MenubarItemBean[] menubarItemBeans;

	public int getBackground_res() {
		return background_res;
	}

	public void setBackground_res(int background_res) {
		this.background_res = background_res;
	}

	public MenubarItemBean[] getMenubarItemBeans() {
		return menubarItemBeans;
	}

	public void setMenubarItemBeans(MenubarItemBean[] menubarItemBeans) {
		this.menubarItemBeans = menubarItemBeans;
	}
	
}
