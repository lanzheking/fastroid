package com.honestwalker.androidutils.commons.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.ViewUtils.ViewSizeHelper;
import com.honestwalker.androidutils.commons.adapter.BeanHolderEvent.BeanHolderClickEvent;
import com.honestwalker.androidutils.commons.adapter.BeanHolderEvent.BeanHolderLongClickEvent;
import com.honestwalker.androidutils.commons.adapter.listener.BeanHolderMounterClickListener;
import com.honestwalker.androidutils.commons.adapter.listener.BeanHolderMounterLongClickListener;
import com.honestwalker.androidutils.equipment.DisplayUtil;
import com.honestwalker.androidutils.exception.ExceptionUtil;
import com.honestwalker.androidutils.views.AsyncImageView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Depiction:
 * <p/>
 * Auth         :  zhe.lan@honestwalker.com <br />
 * Add Date     :  16-2-29 下午4:52. <br />
 * Rewrite Date :  16-2-29 下午4:52. <br />
 */
public class BeanHolderMounter<T> extends BaseMounter<T> {

    public BeanHolderMounter(Context mContext, LinearLayout parentLayout , int viewResId, List<T> data) {
        super(mContext, viewResId, data);
        getViews(parentLayout);
    }

    protected void addItemData(View convertView, T item, int position) {
        loadData(convertView, item);
    }

    /**
     * 加载数据的一些默认操作
     * @param convertView
     * @param t
     */
    private void loadData(View convertView , T t) {

        bindBeanHolder(convertView, t);

        bindBeanHolderEvent(convertView, t);

    }

    /**
     * 数据源对象绑定控件 ， 与赋值
     * @param convertView
     * @param t
     */
    private void bindBeanHolder(View convertView,  T t) {

        Field[] fields = t.getClass().getDeclaredFields();
        for(Field f : fields) {
            f.setAccessible(true);

            BeanHolder beanHolder = f.getAnnotation(BeanHolder.class);
            if(beanHolder != null) {

                int id = beanHolder.id();
                float scaleWidth = beanHolder.scaleViewWidth();
                float scaleSize = beanHolder.scaleSize();
                int width = beanHolder.viewWidth();
                int height = beanHolder.viewHeight();

                View view = convertView.findViewById(id);

                if(width > -1) {
                    ViewSizeHelper.getInstance(mContext).setWidth(view , width);
                }
                if(height > -1) {
                    ViewSizeHelper.getInstance(mContext).setWidth(view , height);
                }
                // 设置尺寸
                if(scaleWidth > -1) {
                    int swidth = (int) (DisplayUtil.getWidth(mContext) * scaleWidth);
                    if(scaleSize > -1) {
                        int shieght = (int) (swidth * scaleSize);
                        ViewSizeHelper.getInstance(mContext).setSize(view, swidth , shieght);
                    } else {
                        ViewSizeHelper.getInstance(mContext).setWidth(view , swidth);
                    }
                }

                // 复制
                try {
                    Object value = f.get(t);

//                    Field mField = t.getClass().getField(f.getName());
//                    String fieldName = mField.getName();
//                    String getterMethodName = "get" + fieldName.substring(0 , 1).toUpperCase() + fieldName.substring(1);
//                    LogCat.d("ddddd" , "getterMethodName=" +  getterMethodName);
//                    Method getterMethod = t.getClass().getMethod(getterMethodName);
//                    Object value = getterMethod.invoke(t);
//                    LogCat.d("ddddd" , getterMethodName + "()=" +  value);

                    if(view instanceof TextView) {
                        ((TextView)view).setText(value + "");
                    } else if(view instanceof AsyncImageView) {
                        ((AsyncImageView)view).loadUrl((String) value);
                    } else if(view instanceof ImageView) {
                        if(value instanceof Drawable) {
                            ((ImageView)view).setImageDrawable((Drawable) f.get(t));
                        } else if(value instanceof Integer) {
                            ((ImageView)view).setImageResource((int) value);
                        } else if(value instanceof Bitmap) {
                            ((ImageView)view).setImageBitmap((Bitmap) value);
                        }
                    }

                } catch (Exception e) {
                    ExceptionUtil.showException(e);
                }

            }

        }

    }

    /**
     * 事件绑定
     * @param convertView
     * @param t
     */
    private void bindBeanHolderEvent(View convertView , final T t) {
        Method[] methods = t.getClass().getMethods();

        for(Method m : methods) {

            /// 处理点击事件绑定
            BeanHolderClickEvent beanHolderClickEvent = m.getAnnotation(BeanHolderClickEvent.class);
            if(beanHolderClickEvent != null) {
                int id = beanHolderClickEvent.id();
                View view = convertView.findViewById(id);

                view.setClickable(true);
                try {
                    Object[] methodArgs = {view};
                    view.setOnClickListener(new BeanHolderMounterClickListener( this , t , m , methodArgs));
                } catch (Exception e){
                }
            }

            /// 处理长按时间绑定
            BeanHolderLongClickEvent beanHolderLongClickEvent = m.getAnnotation(BeanHolderLongClickEvent.class);
            if(beanHolderLongClickEvent != null) {
                int id = beanHolderLongClickEvent.id();
                View view = convertView.findViewById(id);

                view.setLongClickable(true);
                try {
                    Object[] methodArgs = {view};
                    view.setOnLongClickListener(new BeanHolderMounterLongClickListener(this, t, m, methodArgs));
                } catch (Exception e){
                }
            }

        }

    }


}
