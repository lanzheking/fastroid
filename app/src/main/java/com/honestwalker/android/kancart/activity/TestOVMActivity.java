package com.honestwalker.android.kancart.activity;

import android.os.Bundle;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.android.commons.activity.BaseActivity;
import com.honestwalker.android.commons.utils.IntentBind.IntentBind;
import com.honestwalker.android.commons.utils.XUtilsExt.OVMViewUtils;
import com.honestwalker.android.kancart.activity.ovmbean.TestOVMBean;

/**
 * Created by honestwalker on 15-10-27.
 */
public class TestOVMActivity extends BaseActivity {

    @IntentBind
    private TestOVMBean testOVMBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ovm);
        TestOVMBean bean = new TestOVMBean();
        OVMViewUtils.inject(bean);
//        LogCat.d("OVM" , "testOVMBean=" + bean.unameET);

    }

    @Override
    protected void loadData() {
    }
}
