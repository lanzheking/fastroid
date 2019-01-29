package com.honestwalker.android.kc_test;

import android.app.Activity;
import android.content.Context;

import com.honestwalker.android.kc_test.actions.ActivityOperationAction;
import com.honestwalker.android.kc_test.actions.AlertAction;
import com.honestwalker.android.kc_test.actions.DelayAction;
import com.honestwalker.android.kc_test.actions.LogAction;
import com.honestwalker.android.kc_test.actions.ViewAction;
import com.honestwalker.android.kc_test.event.Event;
import com.honestwalker.android.kc_test.event.EventData;
import com.honestwalker.android.kc_test.event.Events;
import com.honestwalker.android.kc_test.models.Action;
import com.honestwalker.android.kc_test.models.Actions;
import com.honestwalker.android.kc_test.models.Task;
import com.honestwalker.android.kc_test.models.TesterConfigReader;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.StringUtil;
import com.honestwalker.androidutils.exception.ExceptionUtil;
import com.honestwalker.androidutils.pool.ThreadPool;
import com.honestwalker.androidutils.system.ProcessUtil;

import java.util.Map;
import java.util.Queue;

/**
 * Created by lanzhe on 16-10-28.
 */
public class KCTestLauncher {

    private static Actions actions;

    private Activity context;

    private static Class rClass;

    private static int testerConfig;

    /** 连续action 场景时，用于记录下一个action */
    public static String nextAction;

    private static Boolean isEnable = null;

    private static boolean isInit = false;

    private static final String TAG = "tester";

    private static LogWindow logWindow;

    public static String testLogTag = "";

    public static void init (Context context, Class rClass, int testerConfig) {

        KCTestLauncher.rClass = rClass;
        KCTestLauncher.testerConfig = testerConfig;
        KCTestLauncher.isInit = true;
        TesterConfigReader reader = new TesterConfigReader();
        try {
            actions = reader.load(context, testerConfig);
            testLogTag = actions.getLogTag();
            isEnable = actions.isEnable();
            LogCat.d(TAG, "read config isEnable = " + isEnable);
        } catch (Exception e) {
            ExceptionUtil.showException(e);
        }

        LogCat.d("tester", "actions.isLogDialogEnable()=" + actions.isLogDialogEnable());
        if(ProcessUtil.isMainProcess(context) && actions.isLogDialogEnable()) {
            logWindow = new LogWindow();
            logWindow.show(context);
        }

        registerActions();

    }

    /**
     * 注册actions，注册过的action 才能接收事件消息，和处理事件
     */
    private static void registerActions() {
        new ViewAction();
        new AlertAction();
        new LogAction();
        new ActivityOperationAction();
    }

    /**
     * logWindow 的日志添加
     * @param log
     */
    public static void log(String log) {
        if(logWindow == null) return;
        logWindow.appentText(log);
    }

    public void start(Activity context) {

        if(isEnable != null && !isEnable) return;

        if(!KCTestLauncher.isInit) {
            LogCat.d(TAG, "KC Test 未初始化");
            return;
        }

        this.context = context;

        if(isEnable != null && !isEnable) return;

        execute();
    }

    public void next(Activity context) {

        if(!KCTestLauncher.isInit) {
            LogCat.d(TAG, "KC Test 未初始化");
        }

        if(isEnable != null && !isEnable) return;

        if(StringUtil.isEmptyOrNull(KCTestLauncher.nextAction)) return;

        this.context = context;

        if(isEnable != null && !isEnable) return;

        execute();
        KCTestLauncher.nextAction = "";
    }

    private void execute() {

        Map<String, Action> actionMap = actions.getActions();

        String packageName = context.getClass().getName();
        LogCat.d("tester", "进入 " + packageName);

        Action action = null;

        if(!StringUtil.isEmptyOrNull(nextAction)) {
            action = actionMap.get(nextAction);
        } else {
            action = actionMap.get(packageName);
        }

        if(action == null) return;

        LogCat.d("tester", "执行 [" + packageName + "]  action  " + action.getActid());
        if(logWindow != null) {
            logWindow.appentText("执行 [" + packageName + "]  action  " + action.getActid());
        }

        final Queue<Task> taskQueue = action.getTasks();
        ThreadPool.threadBackgroundPool(new Runnable() {
            @Override
            public void run() {
                for (Task task : taskQueue) {
                    String event = task.getEvent();

                    EventData eventData = new EventData();
                    eventData.setContext(context);
                    eventData.setrClass(rClass);
                    eventData.setTask(task);

                    LogCat.d(TAG, "当前事件:" + event);
                    onEvent(eventData);

                }
            }
        });

    }

    /**
     * 触发事件消息
     * @param event
     */
    private void onEvent(EventData event) {
        LogCat.d(TAG, "onEvent " + event.getTask().getEvent());
        Events.dispatchEvent(event);
    }

}
