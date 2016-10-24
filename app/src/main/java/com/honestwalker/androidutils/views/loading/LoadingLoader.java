package com.honestwalker.androidutils.views.loading;

import android.content.Context;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.IO.RClassUtil;
import com.honestwalker.androidutils.equipment.DisplayUtil;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by honestwalker on 16-1-20.
 */
public class LoadingLoader {

    private static Class rClass = null;
    private static int screenWidth = 0;
    private static int screenHeight = 0;
    private static int screenHeightWithoutStatubar = 0;

    public static List<LoadingStyle> load(Context context , int loadingConfigResId) throws JDOMException, IOException {

//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        screenWidth = wm.getDefaultDisplay().getWidth();
//        screenHeight = wm.getDefaultDisplay().getHeight();
        screenWidth  = DisplayUtil.getWidth(context);
        screenHeight = DisplayUtil.getHeight(context);
        screenHeightWithoutStatubar = screenHeight - DisplayUtil.getStatusBarHeight(context);

        try {
            rClass = RClassUtil.getRClass(context);
        } catch (Exception e) {
            LogCat.d("loading" , context.getPackageName() + ".R" + " 不存在");
        }

        InputStream in = context.getResources().openRawResource(loadingConfigResId);
        SAXBuilder sb = new SAXBuilder();
        Document doc = sb.build(in);            //读入指定文件
        Element root = doc.getRootElement();    //获得根节点 styles
        List<Element> styleEmtList = root.getChildren("style");
        List<LoadingStyle> loadingStyleList = loadLoadingStyleList(styleEmtList);

        LogCat.d("loading", "----------------------" + styleEmtList.size());
        for(LoadingStyle ls : loadingStyleList) {
            LogCat.d("loading", "cancelAble " + ls.isCancelable());
            LogCat.d("loading", "touchCancelAble " + ls.isTouchCancelable());
            LogCat.d("loading", "layoutRes " + ls.getLayoutResId());
            LogCat.d("loading", "styleRes " + ls.getStyleResId());
            LogCat.d("loading", "textResourceId " + ls.getTextResourceId());
            LogCat.d("loading", "width " + ls.getWidth());
            LogCat.d("loading", "height " + ls.getHeight());
            LogCat.d("loading", "text " + ls.getText());
        }
        LogCat.d("loading", "----------------------");

        return loadingStyleList;

    }

    private static List<LoadingStyle> loadLoadingStyleList(List<Element> styleEmtList) {
        List<LoadingStyle> loadingStyleList = new ArrayList<>();

        if(styleEmtList == null) return loadingStyleList;

        for(Element emt : styleEmtList) {
            LoadingStyle style = new LoadingStyle();
            style.setId(emt.getAttributeValue("id"));

            int width = 0;
            double scaleWidth = getDoubleValue(emt, "width");
            if(scaleWidth > 0) {
                width = (int) (screenWidth * scaleWidth);
            }
            style.setWidth(width);

            int height = 0;
            double scaleHeight = getDoubleValue(emt, "height");
            if(scaleHeight > 0) {
                height = (int) (screenWidth * scaleHeight);
            }
            style.setHeight(height);

            style.setText(getValue(emt, "text"));
            style.setCancelable(getBooleanValue(emt, "cancelAble", true));
            style.setTouchCancelable(getBooleanValue(emt, "touchCancelAble", true));

            style.setLayoutResId(getValueByResourceValue(emt, "layoutRes", R.layout.common_view_dialog_loading));

            style.setStyleResId(getValueByResourceValue(emt, "styleRes" , R.style.loading_dialog_style));

            style.setTextResourceId(getValueByResourceValue(emt, "textResourceId", R.id.dialog_loading_tv));

            loadingStyleList.add(style);
        }

        return loadingStyleList;
    }

    private static String getValue(Element emt , String childName) {
        Element childEmt = emt.getChild(childName);
        return childEmt.getValue();
    }

    private static Boolean getBooleanValue(Element emt , String childName , boolean defaultValue) {
        String valueStr = getValue(emt , childName);
        try {
            return Boolean.parseBoolean(valueStr);
        } catch (Exception e){}
        return defaultValue;
    }

    private static Double getDoubleValue(Element emt , String childName) {
        String valueStr = getValue(emt , childName);
        try {
            return Double.parseDouble(valueStr);
        } catch (Exception e) {}
        return 0d;
    }

    /**
     * 读取资源id
     * @param emt  当前element实体， 也就是style节点
     * @param childName   字段名 如  layoutId 或 styleRes
     * @return
     */
    private static int getValueByResourceValue(Element emt , String childName , int defaultValue) {
        String value = getValue(emt , childName);
        int resId = RClassUtil.getResId(rClass, value.replace("R.", ""));
        if(resId < 1) resId = defaultValue;
        return resId;
    }

}
