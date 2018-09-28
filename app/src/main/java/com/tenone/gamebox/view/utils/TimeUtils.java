package com.tenone.gamebox.view.utils;

import android.annotation.SuppressLint;

import java.io.File;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressLint("SimpleDateFormat")
public class TimeUtils {

	/**
	 * 获取当前时间 年-月-日
	 */
	public static String getTime() {
		Calendar dateAndTime = Calendar.getInstance( Locale.CHINA );
		int mYear = dateAndTime.get( Calendar.YEAR );
		int mMonth = dateAndTime.get( Calendar.MONTH );
		int mDay = dateAndTime.get( Calendar.DAY_OF_MONTH );
		return mYear + "-" + mMonth + "-" + mDay;
	}

	/**
	 * 获取当前时间 年-月-日-时-分-秒
	 *
	 * @return
	 */
	public static String getCurrentTime() {
		Calendar dateAndTime = Calendar.getInstance( Locale.CHINA );
		int mYear = dateAndTime.get( Calendar.YEAR );
		int mMonth = dateAndTime.get( Calendar.MONTH ) + 1;
		int mDay = dateAndTime.get( Calendar.DAY_OF_MONTH );
		int mHour = dateAndTime.get( Calendar.HOUR_OF_DAY );
		int mMin = dateAndTime.get( Calendar.MINUTE );
		int mSec = dateAndTime.get( Calendar.SECOND );
		return mYear + "-" + mMonth + "-" + mDay + "-" + mHour + "-" + mMin
				+ "-" + mSec;
	}


	/**
	 * 获取当前时间 年
	 *
	 * @return
	 */
	public static int getCurrentYear() {
		Calendar dateAndTime = Calendar.getInstance( Locale.CHINA );
		int mYear = dateAndTime.get( Calendar.YEAR );
		return mYear;
	}

	/**
	 * 获取当前时间 月
	 *
	 * @return
	 */
	public static int getCurrentMonth() {
		Calendar dateAndTime = Calendar.getInstance( Locale.CHINA );
		int mMonth = dateAndTime.get( Calendar.MONTH ) + 1;
		return mMonth;
	}

	/**
	 * 获取当前时间 日
	 *
	 * @return
	 */
	public static int getCurrentDay() {
		Calendar dateAndTime = Calendar.getInstance( Locale.CHINA );
		int mDay = dateAndTime.get( Calendar.DAY_OF_MONTH );
		return mDay;
	}

	public static Date initDate(String time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
		ParsePosition pos = new ParsePosition( 0 );
		Date strtodate = dateFormat.parse( time, pos );
		return strtodate;
	}

	public static String timeFormat(long timeMillis, String pattern) {
		try {
			SimpleDateFormat format = new SimpleDateFormat( pattern );
			return format.format( new Date( timeMillis ) );
		} catch (NullPointerException e) {
			return "";
		}
	}

	public static String formatPhotoDate(long time) {
		return timeFormat( time, "yyyy-MM-dd hh:mm" );
	}

	public static String formatData(long time, String pattern) {
		Date date = new Date( time );
		SimpleDateFormat formatter = new SimpleDateFormat( pattern );
		String dateString = formatter.format( date );
		return dateString;
	}

	public static String formatPhotoDate(String path) {
		File file = new File( path );
		if (file.exists()) {
			long time = file.lastModified();
			return formatPhotoDate( time );
		}
		return "1970-01-01";
	}

	public static String formatDateSec(long time) {
		return timeFormat( time, "yyyy-MM-dd HH:mm:ss" );
	}

	public static String formatDateMin(long time) {
		return timeFormat( time, "yyyy-MM-dd HH:mm" );
	}

	public static String formatDateDay(long time) {
		return timeFormat( time, "yyyy\u5e74MM\u6708dd\u65e5" );
	}
	public static long getDateForm(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss",
				Locale.CHINA);
		Date date;
		long times = 0;
		try {
			date = sdr.parse(time);
			times = date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return times;
	}

	private static String getWeek(long timeStamp) {
		int mydate = 0;
		String week = null;
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date(timeStamp));
		mydate = cd.get(Calendar.DAY_OF_WEEK);
		// 获取指定日期转换成星期几
		if (mydate == 1) {
			week = "\u5468\u65e5";
		} else if (mydate == 2) {
			week = "\u5468\u4e00";
		} else if (mydate == 3) {
			week = "\u5468\u4e8c";
		} else if (mydate == 4) {
			week = "\u5468\u4e09";
		} else if (mydate == 5) {
			week = "\u5468\u56db";
		} else if (mydate == 6) {
			week = "\u5468\u4e94";
		} else if (mydate == 7) {
			week = "\u5468\u516d";
		}
		return week;
	}

}
