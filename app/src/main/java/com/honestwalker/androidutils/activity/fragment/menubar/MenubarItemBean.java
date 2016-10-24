package com.honestwalker.androidutils.activity.fragment.menubar;

public class MenubarItemBean {

	private String id;
	private int iconResId;
	private String label;

	private MenubarPageBean menubarPageBean;

	private int labelColorResId;

	private boolean isHome = false;
	private int labelSizeResId;
	private int tabBackgroundResId;
	private int tabBackgroundColorResId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getIconResId() {
		return iconResId;
	}

	public void setIconResId(int iconResId) {
		this.iconResId = iconResId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getLabelColorResId() {
		return labelColorResId;
	}

	public void setLabelColorResId(int labelColorResId) {
		this.labelColorResId = labelColorResId;
	}

	public MenubarPageBean getMenubarPageBean() {
		return menubarPageBean;
	}

	public void setMenubarPageBean(MenubarPageBean menubarPageBean) {
		this.menubarPageBean = menubarPageBean;
	}

	public void setIsHome(boolean isHome) {
		this.isHome = isHome;
	}
	public boolean isHome() {
		return isHome;
	}

	public void setLabelSizeResId(int labelSizeResId) {
		this.labelSizeResId = labelSizeResId;
	}

	public int getLabelSizeResId() {
		return labelSizeResId;
	}

	public void setTabBackgroundResId(int tabBackgroundResId) {
		this.tabBackgroundResId = tabBackgroundResId;
	}

	public int getTabBackgroundResId() {
		return tabBackgroundResId;
	}

	public void setTabBackgroundColorResId(int tabBackgroundColorResId) {
		this.tabBackgroundColorResId = tabBackgroundColorResId;
	}

	public int getTabBackgroundColorResId() {
		return tabBackgroundColorResId;
	}
}
