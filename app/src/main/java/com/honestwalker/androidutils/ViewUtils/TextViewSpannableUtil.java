package com.honestwalker.androidutils.ViewUtils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * Depiction:   设置TextView部分自体颜色的工具类
 * <p/>
 * Auth         :  zhe.lan@honestwalker.com <br />
 * Add Date     :  16-2-25 下午7:50. <br />
 */
public class TextViewSpannableUtil {

    public void setText(TextView textView , String txt , int startIndex , int endIndex , int txtColor) {

        if(startIndex < 0) startIndex = 0;
        if(startIndex > txt.length()) startIndex = txt.length();
        if(endIndex > txt.length()) endIndex = txt.length();
        if(endIndex < startIndex)   endIndex = startIndex;

        SpannableStringBuilder style =new SpannableStringBuilder(txt);
        style.setSpan(new ForegroundColorSpan(txtColor),startIndex,endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(style);
    }

}
