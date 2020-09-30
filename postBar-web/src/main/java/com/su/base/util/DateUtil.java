package com.su.base.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.su.base.constant.DateConstant;

public class DateUtil {
	/**
	 * 获取年
	 * @param reportDate
	 * @return
	 */
	public static int getYear(Date reportDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(reportDate);
		return calendar.get(Calendar.YEAR);
	}
	/**
	 * 获取月
	 * @param reportDate
	 * @return
	 */
	public static int getMonth(Date reportDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(reportDate);
		return calendar.get(Calendar.MONTH);
	}
	/**
	 * 获取季
	 * @param reportDate
	 * @return
	 */
	public static int getSeason(Date reportDate) {
		return (getMonth(reportDate) / 3 + 1);
	}
	/**
	 * 日期转字符串
	 * @param reportDate
	 * @param dateConstant
	 * @return
	 */
	public static String dateConvertStr(Date reportDate, DateConstant dateConstant) {
		DateFormat dateFormat = new SimpleDateFormat(dateConstant.getPatter());
		return dateFormat.format(reportDate);
	}
	/**
	 * 字符串转日期
	 * @param reportDate
	 * @param dateConstant
	 * @return
	 */
	public static Date strConvertDate(String dateStr, DateConstant dateConstant) {
		DateFormat dateFormat = new SimpleDateFormat(dateConstant.getPatter());
		try {
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 日期加减
	 * @param date
	 * @param i
	 * @return
	 */
	public static Date getAfterNDate(Date date, int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, i);
		return calendar.getTime();
	}

}
