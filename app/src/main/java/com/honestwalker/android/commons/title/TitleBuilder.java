package com.honestwalker.android.commons.title;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.StringUtil;
import com.honestwalker.androidutils.ViewUtils.ViewSizeHelper;
import com.honestwalker.androidutils.equipment.DisplayUtil;

/**
 * Created by honestwalker on 15-9-25.
 */
public class TitleBuilder {
    private Context context;

    private RelativeLayout titleLayout;
    private RelativeLayout 	titleLeftLayout;
    private TextView titleLeftTV;
    private ImageView titleLeftIV;
    private ImageView			titleRightIV;
    private RelativeLayout 		titleRightLayout;
    private TextView			titleRightTV;
    private TextView			titleTV;
    private ImageView			titleIV;

    private int screenWidth = 0;
    private int screenHeight = 0;

    private ViewSizeHelper viewSizeHelper;

    public int titleHeight = 0;
    public float titleHeightScale = 0.08f;

    public TitleBuilder(Context context) {
        this(context, null);
    }

    private Theme theme;
    public static enum Theme{
        Home,Standard
    }

    public TitleBuilder(Fragment fragment , View view , TitleArg titleArg) {
        this(fragment,view,titleArg, Theme.Standard);
    }

    public TitleBuilder(Fragment fragment , View view , TitleArg titleArg,Theme theme) {

        this.context = fragment.getActivity();
        this.theme = theme;

        viewSizeHelper = ViewSizeHelper.getInstance(fragment.getActivity());
        screenWidth = DisplayUtil.getWidth(context);
        screenHeight = DisplayUtil.getHeight(context);

        LogCat.d("TITLE", "screenWidth=" + screenWidth);

        initTitleFromFragment(view);

        setTitleViewAttr();

        if(titleArg != null) {
            setTitle(titleArg);
        }

    }

    public TitleBuilder(Context context , TitleArg titleArg) {
        this(context, titleArg, Theme.Standard);
    }

    public TitleBuilder(Context context , TitleArg titleArg,Theme theme) {
        this.context = context;
        this.theme = theme;

        viewSizeHelper = ViewSizeHelper.getInstance(context);
        screenWidth = DisplayUtil.getWidth(context) ;
        screenHeight = DisplayUtil.getHeight(context) ;

        initTitle();

        setTitleViewAttr();

        if(titleArg != null) {
            setTitle(titleArg);
        }

    }

    public void initTitle() {
        titleLayout  	  = (RelativeLayout) ((Activity)context).findViewById(R.id.title_layout);
        titleTV 	 	  = (TextView) ((Activity)context).findViewById(R.id.title_middle_tv);
        titleIV			  = (ImageView) ((Activity)context).findViewById(R.id.title_middle_iv);
        titleLeftLayout   = (RelativeLayout) ((Activity)context).findViewById(R.id.title_left_layout);
        titleLeftTV 	  = (TextView) ((Activity)context).findViewById(R.id.title_left_tv);
        titleLeftIV 	  = (ImageView) ((Activity)context).findViewById(R.id.title_left_iv);
        titleRightLayout  = (RelativeLayout) ((Activity)context).findViewById(R.id.title_right_layout);
        titleRightTV 	  = (TextView)  ((Activity)context).findViewById(R.id.title_right_tv);
        titleRightIV 	  = (ImageView) ((Activity)context).findViewById(R.id.title_right_iv);

        initSize();

    }

    public void initTitleFromFragment(View fragmentContentView) {
        titleLayout  	  = (RelativeLayout) fragmentContentView.findViewById(R.id.title_layout);
        titleTV 	 	  = (TextView) fragmentContentView.findViewById(R.id.title_middle_tv);
        titleIV			  = (ImageView) fragmentContentView.findViewById(R.id.title_middle_iv);
        titleLeftLayout   = (RelativeLayout) fragmentContentView.findViewById(R.id.title_left_layout);
        titleLeftTV 	  = (TextView) fragmentContentView.findViewById(R.id.title_left_tv);
        titleLeftIV 	  = (ImageView) fragmentContentView.findViewById(R.id.title_left_iv);
        titleRightLayout  = (RelativeLayout) fragmentContentView.findViewById(R.id.title_right_layout);
        titleRightTV 	  = (TextView)  fragmentContentView.findViewById(R.id.title_right_tv);
        titleRightIV 	  = (ImageView) fragmentContentView.findViewById(R.id.title_right_iv);

        initSize();

    }

    private void initSize() {
        float iconSize = screenWidth / 16;
        ViewSizeHelper.getInstance(context).setSize(titleRightIV, iconSize, iconSize);
        ViewSizeHelper.getInstance(context).setSize(titleLeftIV , iconSize , iconSize);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = DisplayUtil.getStatusBarHeight(context);
            ViewSizeHelper.getInstance(context).setSize(titleLayout, screenWidth, ((screenWidth * 90 / 800) * 1) + statusBarHeight);
            titleLayout.setPadding(0, statusBarHeight, 0, 0);
        } else {
            ViewSizeHelper.getInstance(context).setSize(titleLayout, screenWidth, ((screenWidth * 90 / 800) * 1));
        }
    }

    /** 设置标题的宽和高 */
    protected void setTitleViewAttr() {

        titleHeight = (int) (screenHeight * titleHeightScale);
        viewSizeHelper.setWidth(titleTV, screenWidth/3*2);
    }

    /**
     * 标题设置
     * @param titleArg
     */
    public void setTitle(TitleArg titleArg) {
        //总背景
        if (theme.equals(Theme.Home)){
            titleLayout.setBackgroundColor(context.getResources().getColor(R.color.title_home));
        } else if(theme.equals(Theme.Standard)){
            titleLayout.setBackgroundColor(context.getResources().getColor(R.color.title_back));
        }
        //中间布局
        if (!StringUtil.isEmptyOrNull(titleArg.getTitleTVStr())) {
            titleTV.setText(titleArg.getTitleTVStr());
            if (theme.equals(Theme.Home)){
                titleTV.setTextColor(context.getResources().getColor(R.color.title_text));
            } else if(theme.equals(Theme.Standard)){
                titleTV.setTextColor(context.getResources().getColor(R.color.title_text));
            }
        }
        if (titleArg.getMiddleIconRes() != 0) {
            titleIV.setImageResource(titleArg.getMiddleIconRes());
        }
        //左布局显示与否
        if (titleArg.isLeftBackVisible()) {
            titleLeftLayout.setVisibility(View.VISIBLE);
            if (theme.equals(Theme.Home)){
                titleLeftTV.setTextColor(context.getResources().getColor(R.color.title_left_text));
            } else if(theme.equals(Theme.Standard)) {
                titleLeftTV.setTextColor(context.getResources().getColor(R.color.title_left_text));
            }
            if (titleArg.getLeftIconRes() != 0) {
                titleLeftIV.setVisibility(View.VISIBLE);
                titleLeftIV.setImageResource(titleArg.getLeftIconRes());
//				viewSizeHelper.setWidth(titleLeftIV, context.getResources().
//						getDimensionPixelSize(R.dimen.title_icon_width), 1, 1);
                titleLeftTV.setVisibility(View.GONE);
                ((RelativeLayout.LayoutParams)titleLeftIV.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT);
                viewSizeHelper.setWidth(titleLeftLayout, context.getResources().
                        getDimensionPixelSize(R.dimen.title_icon_layout_width));
            }else {
//				viewSizeHelper.setWidth(titleLeftIV, context.getResources().
//						getDimensionPixelSize(R.dimen.title_icon_back_width), 1, 1);
            }
            if (titleArg.getLeftBackClickListener() != null) {
                titleLeftLayout.setOnClickListener(titleArg.getLeftBackClickListener());
            }
            if(titleArg.getLeftBtnStr() != null) {
                titleLeftTV.setVisibility(View.VISIBLE);
                titleLeftTV.setText(titleArg.getLeftBtnStr());
                titleLeftIV.setVisibility(View.GONE);
            }
        } else {
            titleLeftLayout.setVisibility(View.GONE);
        }
        //右布局显示与否
        if (titleArg.isRightBtnVisible()) {
            titleRightLayout.setVisibility(View.VISIBLE);
            if (theme.equals(Theme.Home)){
                titleRightTV.setTextColor(context.getResources().getColor(R.color.title_right_text));
            } else if(theme.equals(Theme.Standard)){
                titleRightTV.setTextColor(context.getResources().getColor(R.color.title_right_text));
            }
            if (titleArg.getRightBtnClickListener() != null) {
                titleRightLayout.setOnClickListener(titleArg.getRightBtnClickListener());
            }
            if (!StringUtil.isEmptyOrNull(titleArg.getRightBtnStr())) {
                titleRightIV.setVisibility(View.GONE);
                titleRightTV.setVisibility(View.VISIBLE);

                Paint paint = new Paint();
                paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                        context.getResources().getDimensionPixelSize(R.dimen.title_btn_text_size),
                        context.getResources().getDisplayMetrics()));
                titleRightTV.setText(titleArg.getRightBtnStr());
                if (paint.measureText(titleArg.getRightBtnStr()) < DisplayUtil.dip2px(context , 60)) {
                    viewSizeHelper.setWidth(titleRightTV,
                            context.getResources().getDimensionPixelSize(R.dimen.title_right_text_min_width));
                } else {
                    viewSizeHelper.setWidth(titleRightTV,
                            Math.round(paint.measureText(titleArg.getRightBtnStr())) + 40);
                }
            } else if (titleArg.getRightIconRes() != 0) {
                titleRightIV.setVisibility(View.VISIBLE);
                titleRightTV.setVisibility(View.GONE);
                titleRightIV.setImageResource(titleArg.getRightIconRes());
            }
        } else {
            titleRightLayout.setVisibility(View.GONE);
        }
    }
}
