package com.honestwalker.androidutils.Init;


import android.app.ActivityManager;
import android.content.Context;

import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.IO.RClassUtil;
import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.exception.ExceptionUtil;
import com.honestwalker.androidutils.pool.ThreadPool;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class InitStrategy {

	private String TAG = "INIT";

	private Context context;
	
	private String currentProcessName = R.class.getPackage().getName();

	public InitStrategy(Context context, int initConfigRes) {
		this.context = context;
	}

	/** 存放适用与当前进程的“初始化策略列表” */
	private ArrayList<StrategyBean> currencyProcessInitStrategy = null;

	/** 加载策略配置文件 */
	private void load(int initStrategyResId) throws JDOMException, IOException {
		currencyProcessInitStrategy = new ArrayList<StrategyBean>();
		InputStream in = context.getResources().openRawResource(initStrategyResId);
		SAXBuilder sb = new SAXBuilder();
		Document doc = sb.build(in);//读入指定文件
		Element  root = doc.getRootElement();//获得根节点
		List<Element> list = root.getChildren();//将根节点下的所有子节点放入List中
		currentProcessName = getCurProcessName(context);
		for (int i = 0; i < list.size(); i++) {
			Element strategyItem = (Element) list.get(i);//取得节点实例
			readStrategy(strategyItem);
		}
	}
	
	/** 读取策略列表 */
	private void readStrategy(Element item) {
		StrategyBean strategyBean = new StrategyBean();
		String processName = item.getAttributeValue("process");
		if(processName != null) {
			processName = ":" + processName;
		} else {
			processName = "";
		}
		strategyBean.setProcessName(context.getPackageName() + processName);
		
		LogCat.d(TAG, "进程匹配:" + currentProcessName + "  " + context.getPackageName()  + "   strategyBean.getProcessName()=" + strategyBean.getProcessName());
		
		// 限定只有主进程才执行时，子进程略过执行
		boolean isMainTheadOnly = strategyBean.getProcessName().endsWith(":main");
		LogCat.d(TAG, "isMainTheadOnly=" + isMainTheadOnly);
		if(isMainTheadOnly && currentProcessName.indexOf(":") > -1) {
			return;
		}
		
		// 略过非当前进程。
		if(!isMainTheadOnly && strategyBean.getProcessName().indexOf(":") > -1 &&  // 全局进程初始化要加载 
			!currentProcessName.equals(strategyBean.getProcessName())  // 非当前进程不加载
			) {
			return;
		}
		
		List<Element> actionsEList = item.getChildren("actions");
		if(actionsEList != null && actionsEList.size() > 0) {
			Element actionsE = actionsEList.get(0);
			List<Element> actionEList = actionsE.getChildren("action");
			if(actionEList != null) {
				ArrayList<StrategyActionBean> actionBeans = new ArrayList<StrategyActionBean>();
				for(Element actionE : actionEList) {
					StrategyActionBean strategyActionBean = new StrategyActionBean();
					String name = actionE.getAttributeValue("name");
					String asyncStr = actionE.getAttributeValue("async");
					strategyActionBean.setName(name);
					try {
						Boolean async = (asyncStr!=null)?Boolean.parseBoolean(asyncStr):false;
						strategyActionBean.setAsync(async);
					} catch(Exception e) {
						strategyActionBean.setAsync(false);
					}
					actionBeans.add(strategyActionBean);
				}
				strategyBean.setActions(actionBeans);
			}
		}
		currencyProcessInitStrategy.add(strategyBean);
	}

	/** 获取当前进程名 */
	private String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
				.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}
	
	/**
	 * 根据配置，依次执行初始化
	 */
	public void execute() {

		int initStrategyResId = 0;

		try {

			Class rClass = Class.forName(context.getPackageName() + ".R");
			initStrategyResId = RClassUtil.getResId(rClass , "raw.init_strategy");  // 读取配置索引值

		} catch (Exception e) {
			ExceptionUtil.showException("inject" , e);
			return;
		}

		if(currencyProcessInitStrategy == null) {
			try {
				load(initStrategyResId);
			} catch (Exception e) {}
		}
		if(currencyProcessInitStrategy != null) {
			// 循环策略列表，以获取所有策略对象并执行他们的init方法。
			for(StrategyBean strategyBean : currencyProcessInitStrategy) {
				ArrayList<StrategyActionBean> strategyActionBeans = strategyBean.getActions();
				if(strategyActionBeans == null) continue;
				// 开始注入策略对象，并执行init方法
				for(StrategyActionBean strategyActionBean :strategyActionBeans) {
					try {
						// 查找对象，如果是策略对象 判断如果是async就异步执行
						Class initStrategyClass = Class.forName(strategyActionBean.getName());
						final InitAction action = (InitAction) initStrategyClass.newInstance();
						if(strategyActionBean.getAsync()) {
							ThreadPool.threadPool(new Runnable() {
								@Override
								public void run() {
									action.init(context);
								}
							});
						} else {
							action.init(context);
						}
					} catch (Exception e) {
						ExceptionUtil.showException(TAG , e);
					}
				}
			}
		}
	}

}