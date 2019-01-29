package com.honestwalker.android.kc_test.models;

import android.content.Context;

import com.honestwalker.androidutils.IO.LogCat;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by lanzhe on 16-10-27.
 */
public class TesterConfigReader {

    /**
     * 是否开启自动化测试
     */
//    private static boolean isEnable = false;

    private static Actions actions = null;

    public Actions load(Context context, int testerResId) throws JDOMException, IOException {

        if(TesterConfigReader.actions != null) return TesterConfigReader.actions;

        InputStream in = context.getResources().openRawResource(testerResId);

        SAXBuilder sb  = new SAXBuilder();
        Document doc   = sb.build(in);//读入指定文件
        Element root   = doc.getRootElement();//获得根节点
        List<Element> rootChildrenList = root.getChildren();//将根节点下的所有子节点放入List中

        Actions actions = new Actions();
        Map<String, Action> actionMap = new HashMap<>();

        // 读取自动化测试开关
        Element enableEmt = root.getChild("enable");
        boolean testerIsEnable = false;
        try {
            testerIsEnable = Boolean.parseBoolean(enableEmt.getValue());
        } catch (Exception e) {}

        // 如去日志tag
        Element logTagEmt = root.getChild("log-tag");
        String logTag = logTagEmt.getValue();

        // 读取日志对话框开关
        Element logDialogEmt = root.getChild("log-dialog");
        boolean logDialogEnable = false;
        try {
            logDialogEnable = Boolean.parseBoolean(logDialogEmt.getValue());
        } catch (Exception e) {}


        for (int i = 0; i < rootChildrenList.size(); i++) {
            Element item = (Element) rootChildrenList.get(i);//取得节点实例

            if(item.getName().equals("action")) {
                Action action = new Action();

                String actid = item.getAttributeValue("actid");
                action.setActid(actid);

                Queue<Task> taskQueue = new LinkedBlockingQueue<>();

                List<Element> taskEmtList = item.getChildren("task");
                LogCat.d("tester", "task长度" + taskEmtList.size());
                for (Element taskEmt : taskEmtList) {
                    String event = taskEmt.getAttributeValue("event");
                    String desc = taskEmt.getAttributeValue("desc");
                    String taskValue = taskEmt.getValue();
                    String nextAction = taskEmt.getAttributeValue("nextAction");

                    Task task = new Task();
                    task.setEvent(event);
                    task.setDesc(desc);
                    task.setValue(taskValue);
                    task.setNextAction(nextAction);

                    taskQueue.add(task);

                }
                action.setTasks(taskQueue);

                actionMap.put(actid, action);

            }
        }

        actions.setActions(actionMap);

        actions.setEnable(testerIsEnable);

        actions.setLogTag(logTag);

        actions.setLogDialogEnable(logDialogEnable);

        TesterConfigReader.actions = actions;

        return actions;

    }

}