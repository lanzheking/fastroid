package com.honestwalker.android.commons.menu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.honestwalker.android.fastroid.R;


/**
 * Created by honestwalker on 15-12-14.
 */
public class MyspinneraAdapter extends BaseAdapter {
    String[] strings ;
    Context context;
    public MyspinneraAdapter(Context context, String[] strings){
        this.context = context;
        this.strings = strings;
    }
    @Override
    public int getCount() {
        return strings.length;
    }

    @Override
    public Object getItem(int position) {
        return strings[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater= LayoutInflater.from(context);

        convertView=layoutInflater.inflate(R.layout.spinneritem_select, null);

        TextView textView = (TextView) convertView.findViewById(R.id.textview1);
        textView.setText(strings[position]);
        return convertView;
    }
}
