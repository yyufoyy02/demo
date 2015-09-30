package com.gas.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间规范类
 *
 * @author Heart
 * @date 2015年5月6日
 */
public class TimeFormat {
	private static int secondUnit = 1000;// 1秒
	private static int minuUnit = 60 * secondUnit; // 1分钟
	private static int hourUnit = 60 * minuUnit; // 1小时
	private static int dayUnit = 24 * hourUnit; // 1天

	/**
	 * 把一个long型时间转成String型时间
	 *
	 * @param time
	 *            时间
	 * @param level
	 *            参考Calendar
	 * @return "yyyy-M-d k:mm:ss" 格式的时间
	 */
	public static String convertTimeLong2String(long time, int level) {
		String format = "yyyy-M-d k:mm:ss";
		switch (level) {
			case Calendar.SECOND: {
				format = "yyyy-M-d k:mm:ss";
			}
			break;
			case Calendar.MINUTE: {
				format = "yyyy-M-d k:mm";
			}
			break;
			case Calendar.HOUR: {
				format = "yyyy-M-d k";
			}
			break;
			case Calendar.DATE: {
				format = "yyyy年MM月dd日";
			}
			break;
			case Calendar.MONTH: {
				format = "yyyy-M";
			}
			break;
			case Calendar.YEAR: {
				format = "yyyy";
			}
			break;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(time);
	}
	public static long convertTimeString2Long( String time , int level )
	{
		String format = "yyyy-M-d H:mm:ss";
		switch ( level )
		{
			case Calendar.MINUTE :
			{
				format = "yyyy-M-d H:mm";
			}
			break;
			case Calendar.HOUR :
			{
				format = "yyyy-M-d H";
			}
			break;
			case Calendar.DATE :
			{
				format = "yyyy-M-d";
			}
			break;
			case Calendar.MONTH :
			{
				format = "yyyy-M";
			}
			break;
			case Calendar.YEAR :
			{
				format = "yyyy";
			}
			break;
		}
		SimpleDateFormat sdf = new SimpleDateFormat( format );
		Date date = null;
		long second = 0;
		try
		{
			date = sdf.parse( time );
		}
		 catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		if ( date != null )
		{
			second = date.getTime( );
		}
		return second;
	}
	public static String getToday()
	{
		Calendar today = Calendar.getInstance( ); // 计算今天的界限
		today.set( Calendar.YEAR , today.get( Calendar.YEAR ) );
		today.set( Calendar.MONTH , today.get( Calendar.MONTH ) );
		today.set( Calendar.DAY_OF_MONTH , today.get( Calendar.DAY_OF_MONTH ) );
		// Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
		today.set( Calendar.HOUR_OF_DAY , 0 );
		today.set( Calendar.MINUTE , 0 );
		today.set( Calendar.SECOND , 0 );
		SimpleDateFormat sdf = new SimpleDateFormat();
		Date date = today.getTime();
		sdf.applyPattern("yyyy-MM-dd");
		return sdf.format(date);
	}
}
