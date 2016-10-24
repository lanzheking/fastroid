package com.honestwalker.androidutils.InputVerify;

import android.content.Intent;

import com.honestwalker.androidutils.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by honestwalker on 16-2-18.
 */
public class PhoneVerify {

    public static boolean isMobileNO(String mobile) {
//        Pattern p = Pattern.compile("^((17[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//        Matcher m = p.matcher(mobiles);
//        return m.matches();
        if(StringUtil.isEmptyOrNull(mobile)) {
            return false;
        }
        if(mobile.length() != 11) {
            return false;
        }
        try {
            Long.valueOf(mobile);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
