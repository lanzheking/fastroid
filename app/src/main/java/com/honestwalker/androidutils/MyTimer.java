package com.honestwalker.androidutils;

import java.util.Timer;
import java.util.TimerTask;

public abstract class MyTimer {

	public static Timer run(TimerTask task,long when ) {
		Timer timer = new Timer();
		timer.schedule(task, when);
		return timer;
	}
	
	public static Timer run(TimerTask task,long when, long period) {
		Timer timer = new Timer();
		timer.schedule(task, when,period);
		return timer;
	}
}
