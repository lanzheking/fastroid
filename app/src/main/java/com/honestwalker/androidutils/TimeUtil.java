package com.honestwalker.androidutils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TimeUtil {

	/**
	 * 判断两个时间先后，时间格式必须为 00:00 不能含有年月 和 秒
	 * 
	 * @param time1
	 *            第一个时间
	 * @param time2
	 *            第二个时间
	 * @return 如果第一个时间再第二个时间之后 返回true 否则返回false
	 */
	public boolean isLater(String time1, String time2) {
		if (getWorkStartHour(time1) > getWorkStartHour(time2)) {
			return true;
		} else if (getWorkStartMinute(time1) > getWorkStartHour(time2)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取当前时间
	 * @return
	 */
	public Date getNowDate() {
		Calendar c = Calendar.getInstance();
		Date d = new Date( c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH), c
				.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c
				.get(Calendar.MINUTE), c.get(Calendar.SECOND));
		return d;
	}
	
	/**
	 * 获取当前时间 2011-10-10 21:12:50
	 * @return
	 */
	public String getNow() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" +
		c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" +
		c.get(Calendar.SECOND);
	}
	
	/**
	 * 获取当前时间，格式 21:12:50
	 * @return
	 */
	public String getNowTime() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" +
		c.get(Calendar.SECOND);
	}
	
	/**
	 * 获取今天，格式 2011-10-10
	 * @return
	 */
	public String getToday() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" +
		c.get(Calendar.DAY_OF_MONTH);
	}

	private int getWorkStartHour(String time) {
		return Integer.parseInt(time.substring(0, time.indexOf(":")));
	}

	private int getWorkStartMinute(String time) {
		return Integer.parseInt(time.substring(time.indexOf(":") + 1));
	}
	
	public Timestamp toTimestamp(Date date){
		Timestamp t = new Timestamp(date.getYear(), date.getMonth(), date.getDate(), 0, 0, 0, 0);
		return t;
	}
	
	public GregorianCalendar createCalendarByTimeZone(String ID) {
		TimeZone timeZone = TimeZone.getTimeZone("GMT-4");
		GregorianCalendar calendar = new GregorianCalendar(timeZone);
		calendar.setTime(new Date());
		return calendar;
	}
	
}