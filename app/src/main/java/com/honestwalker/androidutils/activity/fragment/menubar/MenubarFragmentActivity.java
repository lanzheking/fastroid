package com.honestwalker.androidutils.activity.fragment.menubar;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.StringUtil;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.IO.RClassUtil;
import com.honestwalker.androidutils.activity.menubar.MenubarInitException;
import com.honestwalker.androidutils.exception.ExceptionUtil;

public abstract class MenubarFragmentActivity<TUser> extends FragmentActivity {
	
	/** 记录页面Fragment对象 */
	public Fragment[] mFragments;
	
	private MenubarFragmentActivity context; 
	
	/** 记录页面数量 */
	private int menuItenCount = 0;
	
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	
	/** 菜单标签对象管理器，用于菜单配置读取后存放数据 */
	private MenubarItemBean[] menuItems;

	/** 菜单控件对象,activity主要通过他进行菜单操作 */
	private MenubarLayout menubarLayout;
	
	@Override
	protected void onCreate(Bundle arg0) {
		
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_fragment_menubar);
		
		menubarLayout = (MenubarLayout) findViewById(R.id.menubar_layout);
		
		this.context = this;
		
	}
	
	/**
	 * 初始化菜单
	 * @return
	 * @throws IOException 
	 * @throws JDOMException 
	 * @throws ClassNotFoundException 
	 */
	protected void initMenubar(Class RClass , int menuConfigResId) throws JDOMException, IOException, ClassNotFoundException {
		
		InputStream in = this.getResources().openRawResource(menuConfigResId);
		
		SAXBuilder sb = new SAXBuilder();
		Document doc = sb.build(in);//读入指定文件
		Element  root = doc.getRootElement();//获得根节点
		List<Element> list = root.getChildren();//将根节点下的所有子节点放入List中
		
		MenubarBean menubarBean = new MenubarBean();
		
		for (int i = 0; i < list.size(); i++) {
			Element item = (Element) list.get(i);//取得节点实例
			try {

				if(item.getName().equals("items")) {
					
					List<Element> itemList = item.getChildren();
					
					menuItenCount = itemList.size();
					mFragments = new Fragment[menuItenCount];
					menuItems  = new MenubarItemBean[menuItenCount];
					
					menubarBean.setMenubarItemBeans(menuItems);
					
					for(int is=0; is < itemList.size() ; is++) {
						readMenubarItem(is , RClass , itemList.get(is));
					}
					
				} else if(item.getName().equals("item-background")) {
					
					String itemBackground = item.getValue();
					if(!StringUtil.isEmptyOrNull(itemBackground)) {
						if(itemBackground.startsWith("@drawable/")) {
							itemBackground = itemBackground.replace("@drawable/", "");
							int resId = RClassUtil.getDrawableResIdByName(RClass, itemBackground);
							menubarBean.setBackground_res(resId);
							
						} else if(itemBackground.startsWith("@color/")) {
							LogCat.d("MENU" , "设置颜色");
							itemBackground = itemBackground.replace("@color/", "");
							int resId = RClassUtil.getColorResIdByName(RClass, itemBackground);
							menubarBean.setBackground_res(resId);
						}
					}
					
				}
				
			} catch (Exception e) {
				ExceptionUtil.showException(e);
				return;
			}
		}
		
		fragmentManager = getSupportFragmentManager(); 
	   fragmentTransaction = fragmentManager.beginTransaction();  
		   
	   fragmentTransaction.add(R.id.frame_layout, mFragments[0]).show(mFragments[0]).commit();;
		menubarLayout.setMenubarBean(menubarBean);
		
		menubarLayout.initMenubarLayout(this);
		
		menubarLayout.lightMenubarTabPage(0);
		
//		return menubarLayout;
	}
	
	/**
	 * 读取菜单配置
	 * @param RClass
	 * @param item
	 * @throws ClassNotFoundException
	 * @throws MenubarInitException
	 */
	private void readMenubarItem(int index , Class RClass , Element item) throws ClassNotFoundException, MenubarInitException {
		
		// 读取id
		if(item.getAttribute("id") == null ) {
			throw new MenubarInitException("菜单配置 id 参数错误，请检查配置！");
		}
		String id = item.getAttribute("id").getValue();
		
		// 读取class
		if(item.getChild("class") == null) {
			throw new MenubarInitException("菜单配置 activity 参数错误，请检查配置！");
		}
		String fragmentClass = item.getChild("class").getValue();
		
		int iconResId = -1;
		{	// 读取图标
			String icon = null;
			if(item.getChild("icon") != null) {
				icon = item.getChild("icon").getValue();
			}
			
			try {
				if(icon != null) {
					iconResId = RClassUtil.getDrawableResIdByName(RClass , icon);
				}
			} catch (android.content.res.Resources.NotFoundException e) {
				ExceptionUtil.showException(e);
			}
		}
		
		// 读取name
		String label = "";
		int labelColorResId = 0;
		{
			
			if(item.getChild("label") != null) {
				label = item.getChild("label").getValue();
				if(!StringUtil.isEmptyOrNull(label)) {
					
					if(item.getChild("label-color") != null) {
						String color = item.getChild("label-color").getValue();
						color = color.replace("@color/", "");
						labelColorResId = RClassUtil.getColorResIdByName(RClass, color);
						LogCat.d("COLOR", "labelColorResId=" + labelColorResId);
					}
					
				} else {
					label = "";
				}
			}
			
//			if(item.getAttribute("label") != null) {
//				text    = item.getAttribute("label").getValue();
//				if(text == null) text = "";
//				if(text.startsWith("@string/")) {	// 如果是引用字符串解析R文件获取字符串
//					try {
//						int textResId = RClassUtil.getStringResIdByName(RClass, text);
//						text = context.getResources().getString(textResId);
//					} catch (Exception e) {
//						text = "";
//					}
//				}
//			}
		}
		
		addMenuItem(index , id, label, labelColorResId , iconResId , fragmentClass);
		
	}
	
	/**
	 * 页面创建
	 * @param index
	 * @param id
	 * @param label
	 * @param iconResId
	 * @param targetClass
	 */
	private void addMenuItem(int index , String id , String label , int labelColorResId , int iconResId , String targetClass) {
//		menuItems[index] = new MenubarItemBean();
//		MenubarItemBean menubarItemBean = menuItems[index];
//		menubarItemBean.setId(id);
//		menubarItemBean.setIconResId(iconResId);
//		menubarItemBean.setLabel(label);
//		menubarItemBean.setTargetClass(targetClass);
//		menubarItemBean.setLabelColorResId(labelColorResId);
//		mFragments[index] = Fragment.instantiate(this, fragmentClass);
	}
	
	/**
	 * 切换页面主体
	 * @param index
	 */
	void changeTabPage(int index) {
		fragmentTransaction = getSupportFragmentManager().beginTransaction();
		for(int i=0 ; i < mFragments.length ; i++) {
			fragmentTransaction.hide(mFragments[i]);
		}
		if(mFragments[index].isAdded()) {
			fragmentTransaction.show(mFragments[index]).commit();
		} else {
			fragmentTransaction.add(R.id.frame_layout, mFragments[index]).show(mFragments[index]).commit();
			
		}
	}
	
	/** 
	 * 切换页面
	 * @param index
	 */
	protected void setSelectMenubarTabPage(int index) {
		menubarLayout.setSelectMenubarTabPage(index);
	}
	
	/**
	 * 设置菜单数量提示
	 * @param index 索引页
	 * @param qty   数量 0 时隐藏
	 */
	protected void setQty(int index, int qty) {
		menubarLayout.setQty(index, qty);
	}
	
	/**
	 * 获取页面列表
	 * @return
	 */
	public BaseMenuFragment[] getPages() {
		return (BaseMenuFragment[]) mFragments;
	}
	
	/**
	 * 获取当前页
	 * @return
	 */
	public BaseMenuFragment getCurrentPage() {
		return (BaseMenuFragment) mFragments[menubarLayout.getCurrentSelected()];
	}
	
	public int getCurrentPageIndex() {
		return menubarLayout.getCurrentSelected();
	}
	
	public abstract TUser getLoginSuccessUser();
	
}