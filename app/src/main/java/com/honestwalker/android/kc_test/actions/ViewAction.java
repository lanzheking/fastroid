package com.honestwalker.android.kc_test.actions;

import android.widget.Button;
import android.widget.EditText;

import com.honestwalker.android.kc_test.KCTestLauncher;
import com.honestwalker.android.kc_test.event.ClickEvent;
import com.honestwalker.android.kc_test.event.InputEvent;
import com.honestwalker.android.kc_test.event.LongClickEvent;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.IO.RClassUtil;
import com.honestwalker.androidutils.UIHandler;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by lanzhe on 16-11-4.
 */
public class ViewAction extends KCTestBaseAction {

    @Subscribe
    public void click(final ClickEvent clickEvent) {
        LogCat.d("tester", "click 响应事件");
        UIHandler.post(new Runnable() {
            @Override
            public void run() {

                int viewResId = RClassUtil.getResId(clickEvent.getrClass(), "id." + clickEvent.getTask().getDesc());
                Button view = (Button) clickEvent.getContext().findViewById(viewResId);

                KCTestLauncher.log("执行 click 事件 @ view id:" + viewResId);

                view.performClick();

            }
        });

    }

    @Subscribe
    public void longclick(final LongClickEvent longClickEvent) {

        UIHandler.post(new Runnable() {
            @Override
            public void run() {

                int viewResId = RClassUtil.getResId(longClickEvent.getrClass(), "id." + longClickEvent.getTask().getDesc());
                Button view = (Button) longClickEvent.getContext().findViewById(viewResId);

                KCTestLauncher.log("执行 click 事件 @ view id:" + viewResId);

                view.performLongClick();

            }
        });
    }

    @Subscribe
    public void input(final InputEvent inputEvent) {
        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                int viewResId = RClassUtil.getResId(inputEvent.getrClass(), "id." + inputEvent.getTask().getDesc());
                EditText view = (EditText) inputEvent.getContext().findViewById(viewResId);

                KCTestLauncher.log("执行 input 事件 @ view id:" + viewResId);
                view.setText(inputEvent.getTask().getValue());
            }
        });
    }

}
