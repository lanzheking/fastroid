package com.honestwalker.android.commons.jscallback.io;

import android.content.Context;
import android.util.Log;

import com.honestwalker.android.commons.jscallback.bean.JSActionConfigBean;
import com.honestwalker.androidutils.IO.RClassUtil;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by honestwalker on 15-6-2.
 */
public class ConfigLoader {

    public static ArrayList<JSActionConfigBean> loadConfig(Context context) throws FileNotFoundException, JDOMException, IOException {

        int jsCallbackConfigResId = 0;

        try {
            Class rClass = Class.forName(context.getPackageName() + ".R");
            jsCallbackConfigResId = RClassUtil.getResId(rClass, "raw.webview_js_callback");  // 读取配置索引值
        } catch (Exception e) {
            ExceptionUtil.showException("JS" , e);
            return new ArrayList<JSActionConfigBean>();
        }

        InputStream in = context.getResources().openRawResource(jsCallbackConfigResId);
        SAXBuilder sb = new SAXBuilder();
        Document doc = sb.build(in);//读入指定文件
        Element root = doc.getRootElement();//获得根节点
        List<Element> list = root.getChildren();//将根节点下的所有子节点放入List中
        ArrayList<JSActionConfigBean> jsActionConfigBeanList = new ArrayList<JSActionConfigBean>();
        for (int i = 0; i < list.size(); i++) {
            Element item = (Element) list.get(i);//取得节点实例
            JSActionConfigBean bean = readChildren(item);
            if(bean != null) {
                jsActionConfigBeanList.add(bean);
            }
        }
        return jsActionConfigBeanList;
    }

    /** 读取根节点 */
    private static JSActionConfigBean readChildren(Element item) {
        if("action".equals(item.getName())) {
            JSActionConfigBean bean = loadAction(item);
            return bean;
        }
//      else {
//
//      }
        return null;
    }

    private static JSActionConfigBean loadAction(Element item) {
//        String beanClass  = readSingleNoteValue(item , "bean-class");
        String actionClass = readSingleNoteValue(item , "class");
        String actionKey = item.getAttributeValue("key");
//        Log.d("TEST", "actionKey=" + actionKey + "  beanClass=" + beanClass + "   clazz=" + actionClass);
        try {
            JSActionConfigBean bean = new JSActionConfigBean();
//            Class beanClazz = Class.forName(beanClass);
            Class actionClazz = Class.forName(actionClass);
//            bean.setBeanClass(beanClazz);
            bean.setClazz(actionClazz);
            bean.setKey(actionKey);
            return bean;
        } catch (Exception e) {
            Log.e("TEST" , e.toString());
            return null;
        }
    }

    /** 读取下一个单一节点的值 */
    private static String readSingleNoteValue(Element item , String noteName) {
        if(item != null && item.getChildren(noteName) != null && item.getChildren(noteName).size() > 0) {
            return ((Element)item.getChildren(noteName).get(0)).getValue();
        } else {
            return null;
        }
    }

}
