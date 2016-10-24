package com.honestwalker.androidutils.commons.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.honestwalker.androidutils.ViewUtils.ViewSizeHelper;
import com.honestwalker.androidutils.commons.adapter.BeanHolderEvent.BeanHolderClickEvent;
import com.honestwalker.androidutils.commons.adapter.BeanHolderEvent.BeanHolderLongClickEvent;
import com.honestwalker.androidutils.commons.adapter.listener.BeanHolderAdapterClickListener;
import com.honestwalker.androidutils.commons.adapter.listener.BeanHolderAdapterLongClickListener;
import com.honestwalker.androidutils.equipment.DisplayUtil;
import com.honestwalker.androidutils.exception.ExceptionUtil;
import com.honestwalker.androidutils.views.AsyncImageView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by honestwalker on 15-11-18.
 */
public class BeanHolderAdapter<T> extends BaseArrayAdapter<T> {

    private ListView listView;

    private OnBeanHolderItemLoaded onBeanHolderItemLoaded;

    private HashMap<Integer , HashMap<Integer , View>> viewMapping = new HashMap<>();

    public BeanHolderAdapter(Context context , ListView listView, int itemResId, List<T> data) {
        super(context, itemResId, data);
        this.listView = listView;
        this.listView.setAdapter(this);
        //this.listView.setDivider(null);
    }

    private void loadData(View convertView , T t , int position) {

        bindBeanHolder(convertView, t , position);

        bindBeanHolderEvent(convertView, t);

    }

    /**
     * 数据源对象绑定控件 ， 与赋值
     * @param convertView
     * @param t
     */
    private void bindBeanHolder(View convertView,  T t , int position) {

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

                if(!viewMapping.containsKey(position)) {
                    viewMapping.put(position , new HashMap<Integer, View>());
                }
                viewMapping.get(position).put(id , view);

                if(width > -1) {
                    ViewSizeHelper.getInstance(getContext()).setWidth(view , width);
                }
                if(height > -1) {
                    ViewSizeHelper.getInstance(getContext()).setWidth(view , height);
                }
                // 设置尺寸
                if(scaleWidth > -1) {
                    int swidth = (int) (DisplayUtil.getWidth(getContext()) * scaleWidth);
                    if(scaleSize > -1) {
                        int shieght = (int) (swidth * scaleSize);
                        ViewSizeHelper.getInstance(getContext()).setSize(view, swidth , shieght);
                    } else {
                        ViewSizeHelper.getInstance(getContext()).setWidth(view , swidth);
                    }
                }

                // 复制
                try {
                    Object imageValue = f.get(t);
                    if(view instanceof TextView) {
                        ((TextView)view).setText(f.get(t) + "");
                    } else if(view instanceof AsyncImageView) {
                        ((AsyncImageView)view).loadUrl((String) imageValue);
                    } else if(view instanceof ImageView) {
                        if(imageValue instanceof Drawable) {
                            ((ImageView)view).setImageDrawable((Drawable) f.get(t));
                        } else if(imageValue instanceof Integer){
                            ((ImageView)view).setImageResource((int) imageValue);
                        } else if(imageValue instanceof Bitmap) {
                            ((ImageView)view).setImageBitmap((Bitmap) imageValue);
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
                    view.setOnClickListener(new BeanHolderAdapterClickListener( this , t , m , methodArgs));
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
                    view.setOnLongClickListener(new BeanHolderAdapterLongClickListener(this, t, m, methodArgs));
                } catch (Exception e){
                }
            }

        }

    }

    @Override
    protected void addItemData(View convertView, T item, int position) {
        loadData(convertView , item , position);
        if(onBeanHolderItemLoaded != null) {
            onBeanHolderItemLoaded.onItemLoaded(convertView , item , position , viewMapping.get(position));
        }
    }

    public void update(ArrayList<T> dataSource) {
        super.getData().clear();
        super.getData().addAll(dataSource);
        this.notifyDataSetChanged();
    }

    public void add(ArrayList<T> dataSource) {
        super.getData().addAll(dataSource);
        this.notifyDataSetChanged();
    }

    public void setOnBeanHolderItemLoaded(OnBeanHolderItemLoaded listener) {
        this.onBeanHolderItemLoaded = listener;
    }

}
