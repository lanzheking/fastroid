package com.honestwalker.androidutils.views;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.equipment.DisplayUtil;

/**
 * Created by honestwalker on 15-8-14.
 */
public class AlertDialogPage extends DialogPage implements View.OnClickListener {

    private Button leftBTN;
    private Button rightBTN;
    private TextView contentTV;

    private AlertDialogStyle style = AlertDialogStyle.SingleBTN;

    private View.OnClickListener leftBTNOnClickListener;
    private View.OnClickListener rightBTNOnClickListener;

    private String content;
    private String leftBTNText = "确认";
    private String rightBTNText = "取消";

    public static enum AlertDialogStyle {
        SingleBTN , DoubleBTN
    }

    public AlertDialogPage(Activity context) {
        super(context, R.layout.page_alert_dialog);
        initStyle(style);
    }

    public AlertDialogPage(Activity context, AlertDialogStyle style) {
        super(context, R.layout.page_alert_dialog);
        if(style != null) {
            this.style = style;
        }
        initStyle(style);
    }

    private void initStyle(AlertDialogStyle style) {
        if(AlertDialogStyle.DoubleBTN.equals(style)) {
            leftBTN.setVisibility(View.VISIBLE);
            leftBTN.setBackgroundResource(R.drawable.xmlbtn_white_left_bottom_radius);
            rightBTN.setVisibility(View.VISIBLE);
            rightBTN.setBackgroundResource(R.drawable.xmlbtn_white_right_bottom_radius);
        } else {
            rightBTN.setVisibility(View.GONE);
            leftBTN.setBackgroundResource(R.drawable.xmlbtn_white_bottom_radius);
        }
    }

    @Override
    protected void initView() {
        leftBTN  = (Button) findViewById(R.id.leftBTN);
        rightBTN = (Button) findViewById(R.id.rightBTN);
        leftBTN.setOnClickListener(this);
        rightBTN.setOnClickListener(this);
        contentTV = (TextView) findViewById(R.id.textview1);
        showDismissView(false);
        setWidth((int) (DisplayUtil.getWidth(context)  * 0.6));
    }

    public void setContent(String content) {
        if(content == null) content = "";
        this.content = content;
        if(contentTV != null) {
            contentTV.setText(content);
        }
    }

    public void setLeftBTNText(String txt) {
        this.leftBTNText = txt;
        leftBTN.setText(leftBTNText);
    }

    public void setRightBTNText(String txt) {
        this.rightBTNText = txt;
        rightBTN.setText(rightBTNText);
    }

    @Override
    protected void displayContent() {
        leftBTN.setText(leftBTNText);
        rightBTN.setText(rightBTNText);
        contentTV.setText(content);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.leftBTN) {
            if(this.leftBTNOnClickListener != null) {
                this.leftBTNOnClickListener.onClick(v);
            } else {
                dismiss();
            }
        } else if(v.getId() == R.id.rightBTN) {
            if(this.rightBTNOnClickListener != null) {
                this.rightBTNOnClickListener.onClick(v);
            } else {
                dismiss();
            }
        }
    }

    public void setSingleBTNOnClickListener(View.OnClickListener listener) {
        this.leftBTNOnClickListener = listener;
    }

    public void setLeftBTNOnClickListener(View.OnClickListener listener) {
        this.leftBTNOnClickListener = listener;
    }

    public void setRightBTNOnClickListener(View.OnClickListener listener) {
        this.rightBTNOnClickListener = listener;
    }

}
