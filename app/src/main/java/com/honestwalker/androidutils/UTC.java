package com.honestwalker.androidutils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class UTC {
	public static void getCurrentUtcTime() {
        Date l_datetime = new Date();
        DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        TimeZone l_timezone = TimeZone.getTimeZone("GMT-0");
        formatter.setTimeZone(l_timezone);
        String l_utc_date = formatter.format(l_datetime);
        System.out.println(l_utc_date + " (Local)");
}
}
