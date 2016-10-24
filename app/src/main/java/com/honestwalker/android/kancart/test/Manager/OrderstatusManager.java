package com.honestwalker.android.kancart.test.Manager;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.android.commons.activity.BaseActivity;

import java.util.ArrayList;

/**
 * Created by honestwalker on 15-12-22.
 */
public class OrderstatusManager {

    private BaseActivity context;
    private LinearLayout layout;
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    public OrderstatusManager(BaseActivity context,LinearLayout layout){
        this.context = context;
        this.layout = layout;
    }


    public void init(){
        int count = layout.getChildCount();
        for (int i = 0; i < count; i++) {
            imageViews.add((ImageView) layout.getChildAt(i));

        }
        //reSetStatus();
    }

    public void changeStatus(int status){
        ImageView imageView;
        status = status*2;
        for (int i = 0; i < status; i++) {
            imageView = imageViews.get(i);
            int x = imageView.getDrawable().getIntrinsicWidth();
            int y = imageView.getDrawable().getIntrinsicHeight();
            if(x/y > 2){
                imageView.setImageResource(R.drawable.icon_arrow_blue3x);
            }else{
                imageView.setImageResource(R.drawable.icon_state_blue3x);
            }
        }
    }

    /*public void reSetStatus(){
        for (ImageView imageView : imageViews) {
            int x = imageView.getDrawable().getIntrinsicWidth();
            int y = imageView.getDrawable().getIntrinsicHeight();
            if(x/y > 2){
                imageView.setImageResource(R.drawable.icon_arrow_gray3x);
            }else{
                imageView.setImageResource(R.drawable.icon_state_gray3x);
            }
        }
    }*/
}
