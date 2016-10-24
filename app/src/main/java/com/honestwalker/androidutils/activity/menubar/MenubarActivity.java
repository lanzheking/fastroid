package com.honestwalker.androidutils.activity.menubar;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.IO.RClassUtil;
import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.exception.ExceptionUtil;
import com.honestwalker.androidutils.views.QtyView;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单容器集成该activity <br />
 * activity跳转进入菜单页面，并显示指定标签 传 menuitem_index = 标签索引序号
 * @author honestwalker
 *
 */
public abstract class MenubarActivity extends TabActivity {
	
	private TabWidget mTabWidget;
	private TabHost mTabHost;
	
	protected MenubarActivity context;
	
	private ArrayList<QtyView> qtyViewList = new ArrayList<QtyView>();
	
	private int menuItenCount = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = this;
		
		setContentView(R.layout.activity_menubar);
		
		mTabHost = getTabHost();
		
		init();
	}
	
	/**
	 * 初始化菜单
	 * @return
	 * @throws IOException 
	 * @throws JDOMException 
	 * @throws ClassNotFoundException 
	 */
	protected MenubarController initMenubar(Class RClass , Class RDrawableClass , int menuConfigResId) throws JDOMException, IOException, ClassNotFoundException {
		
		InputStream in = this.getResources().openRawResource(menuConfigResId);
		
		SAXBuilder sb = new SAXBuilder();
		Document doc = sb.build(in);//读入指定文件
		Element  root = doc.getRootElement();//获得根节点
		List<Element> list = root.getChildren();//将根节点下的所有子节点放入List中
		menuItenCount = list.size();
		for (int i = 0; i < list.size(); i++) {
			Element item = (Element) list.get(i);//取得节点实例
			try {
				
				LogCat.d("MENU", "item name = " + item.getName());
				
				if(item.getName().equals("items")) {
					
					List<Element> itemList = item.getChildren();
                    LogCat.d("MENU" , "itemList Size = " + itemList.size() + " RClass=" + RClass + " RDrawableClass=" + RDrawableClass);
					for(int is=0; i < itemList.size() ; is++) {
						readMenubarItem(RClass , RDrawableClass , itemList.get(is));
					}
				}
				
			} catch (Exception e) {
				ExceptionUtil.showException(e);
				return null;
			}
		}
		
		MenubarController menubarController = MenubarController.getInstance(context);
		return menubarController;
	}
	
	private void readMenubarItem(Class RClass , Class RDrawableClass , Element item) throws ClassNotFoundException, MenubarInitException {
		
		if(item.getAttribute("id") == null ) {
			throw new MenubarInitException("菜单配置 id 参数错误，请检查配置！");
		}
		
		String id 	    = item.getAttribute("id").getValue();
		
		if(item.getChild("class") == null) {
			throw new MenubarInitException("菜单配置 activity 参数错误，请检查配置！");
		}
		String pageClass = item.getChild("class").getValue();
		
		
		int iconResId = -1;
		{	// 读取图标
			String icon = null;
			if(item.getChild("icon") != null) {
				icon = item.getChild("icon").getValue();
			}
			
			try {
				if(icon != null) {
					iconResId = RClassUtil.getDrawableResIdByName(RDrawableClass , icon);
				}
			} catch (android.content.res.Resources.NotFoundException e) {
				ExceptionUtil.showException(e);
			}
		}
		
		
		String text = "";
		{
			if(item.getAttribute("label") != null) {
				text    = item.getAttribute("label").getValue();
				if(text == null) text = "";
				if(text.startsWith("@string/")) {
					try {
						int textResId = RClassUtil.getStringResIdByName(RClass, text);
						text = context.getResources().getString(textResId);
					} catch (Exception e) {
						text = "";
					}
				}
			}
		}
		
		Class actClass = Class.forName(pageClass);
		
		addMenuItem(id, text, iconResId , actClass);
		
	}
	
	/**
	 * 设置菜单项目
     * @param id
	 * @param label 标题文字
	 * @param iconResId   id
	 * @param intent
	 */
	private void setTabIndicator(String id , String label, int iconResId , Intent intent) {
		// 使用指定Tab样式
		View view = LayoutInflater.from(this.mTabHost.getContext()).inflate(R.layout.view_menubar_tab, null);

		TextView tv = (TextView) view.findViewById(R.id.tab_label);
		if(label == null) label = "";
		tv.setText(label);
		if(label.equals("")) {
			tv.setVisibility(View.GONE);
		}
		
		ImageView iv = (ImageView) view.findViewById(R.id.tab_iv);
		if(iconResId > 0) {
			iv.setImageResource(iconResId);
		} else {
			iv.setVisibility(View.GONE);
		}
		
		QtyView qtyView = (QtyView) view.findViewById(R.id.tag_qtyview);
		if(qtyView != null) {
			qtyViewList.add(qtyView);
		}

		// 创建一个新Tab
		TabHost.TabSpec localTabSpec = mTabHost.newTabSpec(id).setIndicator(view).setContent(intent);
		// 加载新Tab
		mTabHost.addTab(localTabSpec);
	}
	
	private void addMenuItem(String id , String text , int iconResId , Class actClass) {
		setTabIndicator(id , text, iconResId , new Intent(this, actClass));
	}
	
	private void init() {
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	public int getTabPageCount() {
		return menuItenCount;
	}
	
	/**
	 * 获取索引位置获取相应的qtyView
	 * @param index
	 * @return
	 */
	public QtyView getQtyView(int index) {
		if(index >= qtyViewList.size()) return null;
		return qtyViewList.get(index);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if(intent.getExtras() != null && intent.getExtras().containsKey("menuitem_index")) {
			int index = intent.getExtras().getInt("menuitem_index");
			
			if(index < 0) index = 0;
			if(index >= getTabPageCount()) index = getTabPageCount();
			
			mTabHost.setCurrentTab(index);
			
		}
	}
	
}
