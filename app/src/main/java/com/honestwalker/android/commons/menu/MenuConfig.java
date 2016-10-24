package com.honestwalker.android.commons.menu;

import android.content.Context;
import android.os.Bundle;

import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.IO.RClassUtil;
import com.honestwalker.androidutils.activity.fragment.menubar.MenubarItemBean;
import com.honestwalker.androidutils.activity.fragment.menubar.MenubarPageBean;
import com.honestwalker.androidutils.activity.menubar.MenubarInitException;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by honestwalker on 15-9-25.
 */
public final class MenuConfig {

    private static int menuItemCount = 0;

    /** 菜单标签对象管理器，用于菜单配置读取后存放数据 */
    private final ArrayList<MenubarItemBean> menuItems = new ArrayList<MenubarItemBean>();

    public List<MenubarItemBean> getMenubarItemBeans() {
        return menuItems;
    }

    public void initMenu(ArrayList<MenubarItemBean> menuItems) {
        this.menuItems.clear();
        this.menuItems.addAll(menuItems);
        this.menuItemCount = this.menuItems.size();
    }

    public void initMenu(Context context , Class rClass , int menuConfigResId) throws JDOMException, IOException {

        InputStream in = context.getResources().openRawResource(menuConfigResId);

        SAXBuilder sb = new SAXBuilder();
        Document doc = sb.build(in);//读入指定文件
        Element root = doc.getRootElement();//获得根节点
        List<Element> rootChildrenList = root.getChildren();//将根节点下的所有子节点放入List中

        menuItemCount = rootChildrenList.size();

        for (int i = 0; i < rootChildrenList.size(); i++) {
            Element item = (Element) rootChildrenList.get(i);//取得节点实例
            try {

                if(item.getName().equals("items")) {        // 读取items节点配置
                    List<Element> itemList = item.getChildren();
                    for(int itemIndex=0; itemIndex < itemList.size() ; itemIndex++) {       // 读取item节点配置
                        if("item".equals(itemList.get(itemIndex).getName())) {
                            readMenubarItem(context , rClass , itemList.get(itemIndex));
                        }
                    }
                    menuItemCount++;
                }

            } catch (Exception e) {
                ExceptionUtil.showException(e);
            }
        }


    }

    /**
     * 解析条目
     * @param context
     * @param rClass
     * @param item
     * @throws ClassNotFoundException
     * @throws MenubarInitException
     */
    private void readMenubarItem(Context context , Class rClass , Element item) throws ClassNotFoundException, MenubarInitException {

        if(item.getAttribute("id") == null ) {
            throw new MenubarInitException("菜单配置 id 参数错误，请检查配置！");
        }
        // 读取 id
        String id  = item.getAttribute("id").getValue();
        LogCat.d("MENU" , "id=" + id);

        int tabBackgroundResId = 0;
        if (item.getAttribute("drawable") != null) {
            String tabBackgroundResIdStr = item.getAttributeValue("drawable");
            if (tabBackgroundResIdStr != null) {
                tabBackgroundResId = RClassUtil.getDrawableResIdByName(rClass, tabBackgroundResIdStr);
            }
        }

        int tabBackgroundColorResId = 0;
        if (item.getAttribute("color") != null) {
            String tabBackgroundColorResIdStr = item.getAttributeValue("color");
            if (tabBackgroundColorResIdStr != null) {
                tabBackgroundColorResId = RClassUtil.getColorResIdByName(rClass, tabBackgroundColorResIdStr);
            }
        }

        Element pageEmt = item.getChild("page");
        // 读取 page
        if(pageEmt == null) {
            throw new MenubarInitException("菜单配置 activity 参数错误，请检查配置！");
        }

        Element actionEmt = pageEmt.getChild("action");

        MenubarPageBean menubarPageBean = new MenubarPageBean();

        String actionValue = actionEmt.getValue();

        String pageTarget = pageEmt.getAttributeValue("target");
        String pageTitle =  pageEmt.getAttributeValue("title");

        LogCat.d("MENU" , "actionValue=" + actionValue + "  pageTarget=" + pageTarget + "   pageTitle=" + pageTitle);

        int iconResId = -1;
        {	// 读取图标
            String iconNormalStr = null;
            Element iconEmt = item.getChild("icon");
            if(iconEmt != null) {
                iconNormalStr = iconEmt.getAttributeValue("drawable");
            }

            try {
                if(iconNormalStr != null) {
                    iconResId = RClassUtil.getDrawableResIdByName(rClass, iconNormalStr);
                }
            } catch (android.content.res.Resources.NotFoundException e) {
                ExceptionUtil.showException(e);
            }

            LogCat.d("MENU" , "iconResId=" + iconResId);

        }


        Element labelEmt = item.getChild("label");
        String label = "";
        int labelColorResId = 0;
        int labelSizeResId = 0;
        String labelColorResIdStr = "";
        String labelSizeResIdStr = "";
        {
            if(labelEmt != null) {
                label    = labelEmt.getValue();
                if(label == null) label = "";
                if(label.startsWith("@string/")) {
                    try {
                        int textResId = RClassUtil.getStringResIdByName(rClass, label);
                        label = context.getResources().getString(textResId);
                    } catch (Exception e) {
                        label = "";
                    }
                }

                labelColorResIdStr = labelEmt.getAttributeValue("color");
                labelColorResId = RClassUtil.getColorResIdByName(rClass, labelColorResIdStr);

                labelSizeResIdStr = labelEmt.getAttributeValue("size");
                if(labelSizeResIdStr != null) {
                    labelSizeResId    = RClassUtil.getDimenResIdByName(rClass, labelSizeResIdStr);
                }

            }
            LogCat.d("MENU", "label=" + label + "  labelColorResId=" + labelColorResId + "   labelSizeResId=" + labelSizeResId);
        }

        Class actClass = null;
//        String targetUrl = pageEmt.getValue() == null ? "" : pageEmt.getValue();

        String targetUrl = actionEmt.getValue();

        if(Target.FRAGMENT.equals(pageTarget)) {
            actClass = Class.forName(actionValue);
        } else if(Target.FRAGMENT_WEB.equals(pageTarget)) {
            actClass = BaseWebViewFragment.class;
            menubarPageBean.setTargetUrl(targetUrl);
        } else if(Target.ACTIVITY.equals(pageTarget)) {
            menubarPageBean.setTargetUrl(targetUrl);
        } else if(Target.ACTIVITY_WEB.equals(pageTarget)) {
            menubarPageBean.setTargetUrl(targetUrl);
        }

        menubarPageBean.setTargetClass(actClass);
        menubarPageBean.setTarget(pageTarget);
        menubarPageBean.setTitle(pageTitle);

        Bundle data = getPageData(pageEmt);
        menubarPageBean.setData(data);

        addMenuItem(id, tabBackgroundResId , tabBackgroundColorResId , label, labelColorResId , labelSizeResId, iconResId, menubarPageBean);

    }

    private Bundle getPageData(Element pageEnt) {
        Bundle data = new Bundle();
        Element paramsEmt = pageEnt.getChild("params");
        if(paramsEmt != null) {
            List<Element> paramEmtList = paramsEmt.getChildren("param");
            for(Element paramEmt : paramEmtList) {
                if(paramEmt.getAttribute("key") != null) {
                    String key = paramEmt.getAttribute("key").getValue();
                    String value = paramEmt.getValue();
                    data.putString(key , value);
                }
            }
        }
        return data;
    }

    private void addMenuItem(String id , int tabBackgroundResId , int tabBackgroundColorResId , String label , int labelColorResId , int labelSizeResId , int iconResId , MenubarPageBean menubarPageBean) {
        MenubarItemBean menuItemBean = new MenubarItemBean();

        menuItemBean.setId(id);
        menuItemBean.setIconResId(iconResId);
        menuItemBean.setLabel(label);
        menuItemBean.setMenubarPageBean(menubarPageBean);
        menuItemBean.setLabelColorResId(labelColorResId);
        menuItemBean.setLabelSizeResId(labelSizeResId);
        menuItemBean.setTabBackgroundResId(tabBackgroundResId);
        menuItemBean.setTabBackgroundColorResId(tabBackgroundColorResId);

        menuItems.add(menuItemBean);
    }

    private Class getDrawableClass(Class rClass) {
        try {
            Field drawableField = rClass.getDeclaredField("drawable");
            return drawableField.getClass();
        } catch (Exception e){
            return null;
        }
    }

}
