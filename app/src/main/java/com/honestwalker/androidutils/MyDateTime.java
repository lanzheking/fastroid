package com.honestwalker.androidutils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MyDateTime {
	/**
	 * 获取年份
	 * @return int 年份
	 */
	public static  int getYear(){
		return Calendar.getInstance().get(Calendar.YEAR);
	}
	
	/**
	 * 获取月份
	 * @return int 月份
	 */
	public static int getMonth(){
		return Calendar.getInstance().get(Calendar.MONTH);
	}
	
	/**
	 * 获取星期
	 * @return int 星期 。 1 是星期天 2 是星期2 以此类推
	 */
	public static int getDayOfWeek(){
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * 获取号数，就是一月中的几号
	 * @return int 号数
	 */
	public static int getDayOfMonth(){
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 获取今天是当年的第几天
	 * @return int 今天是当年的第几天
	 */
	public static int getDayOfYear(){
		return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
	}
	
	/**
	 * 获取当前小时，12小时制
	 * @return int 获取当前小时
	 */
	public static int getHour(){
		return Calendar.getInstance().get(Calendar.HOUR);
	}
	
	/**
	 * 或当前小时，24小时制
	 * @return int 获取当前小时
	 */
	public static int getHourOfDay(){
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 获取当前分钟
	 * @return int 当前分钟
	 */
	public static int getMinute(){
		return Calendar.getInstance().get(Calendar.MINUTE);
	}
	
	public static int getSecond(){
		return Calendar.getInstance().get(Calendar.SECOND);
	}
	
	/**
	 * 获取当前毫秒
	 * @return long 毫秒
	 */
	public static long getNano(){
		return Calendar.getInstance().getTimeInMillis();
	}
	
	/**
	 * 获取当前时间
	 * @return Date 当前时间
	 */
	public static Date nowOfDate(){
		return Calendar.getInstance().getTime();
	}
	
	/**
	 * 获取当前时间
	 * @return Timestamp 当前时间
	 */
	public static Timestamp nowOfTimestamp(){
		Timestamp time = new Timestamp(getNano());
		return time;
	}
	
	/**
	 * 格式化输出时间
	 * @param formatStr 格式化字符串
	 * @return String 个时候后的时间
	 */
	public static String format(String formatStr){
		SimpleDateFormat  sdf = new SimpleDateFormat(formatStr);
		return sdf.format(nowOfDate());
	}
	
	/**
	 * 获取当前时间的UTC时间
	 * @return
	 */
	public static Date getNowUTCTime() {
		final Calendar cal = Calendar.getInstance();
	    System.out.println(cal.getTime());  
	    //2、取得时间偏移量：    
	    final int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
	    System.out.println(zoneOffset);  
	    //3、取得夏令时差：    
	    final int dstOffset = cal.get(Calendar.DST_OFFSET);
	    System.out.println(dstOffset);  
	    //4、从本地时间里扣除这些差量，即可以取得UTC时间：    
	    cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
	    return cal.getTime();
	}
	
	/**
	 * 获取当前时间的UTC时间字符串
	 * @return
	 */
	public static String getNowUTCTimeStr() {
		Date date = getNowUTCTime();
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formater.format(date);
	}
	
}
