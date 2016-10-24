package com.honestwalker.android.commons.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.ViewUtils.ViewSizeHelper;
import com.honestwalker.androidutils.commons.adapter.BaseArrayAdapter;
import com.honestwalker.androidutils.commons.adapter.BaseViewHolder;

import java.util.List;

/**
 * Created by honestwalker on 15-6-17.
 */
public class WelcomeViewFlowAdapter extends BaseArrayAdapter<Integer> {

    public WelcomeViewFlowAdapter(Context context, List<Integer> data) {
        super(context, R.layout.item_welcomeviewflow, data);
    }

    @Override
    protected void addItemData(View convertView, Integer item, int position) {
        ViewHolder holder = getViewHolder(convertView,ViewHolder.class);
        holder.showNavImg.setImageResource(item.intValue());
    }

    private class ViewHolder extends BaseViewHolder {

        private ImageView showNavImg;

        public ViewHolder(View baseView) {
            super(baseView);
            showNavImg = (ImageView) findViewById(R.id.imageview1);
        }
    }

}
