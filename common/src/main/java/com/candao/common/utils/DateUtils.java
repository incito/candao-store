package com.candao.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author maew
 * @category see 日期工具
 */
public class DateUtils {
	private static final Logger logger = LoggerFactory
			.getLogger(DateUtils.class);

	final static String DEFAULT_FORMAT = "yyyy-MM-dd";
	
	public final static String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	final static String ORDERID_FORMAT = "yyyyMMdd";

	/**
	 * 根据给定的格式化参数，将字符串转换为日期
	 * 
	 * @param dateString
	 * @param dateFormat
	 * @return java.util.Date
	 */
	public static java.util.Date parse(String dateString, String dateFormat) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>>parse(String dateString, String dateFormat)");
		}
		if ("".equals(dateString.trim()) || dateString == null) {
			return null;
		}
		DateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date = null;
		try {
			date = sdf.parse(dateString);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("<<<parse(return java.util.Date)");
		}
		return date;
	}

	/**
	 * 
	 * 默认将字符串转换为日期，格式(yyyy-MM-dd)
	 * 
	 * @param dateString
	 * @return
	 */
	public static java.util.Date parse(String dateString) {
		return parse(dateString, DEFAULT_FORMAT);
	}

	/**
	 * 根据给定的格式化参数，将日期转换为字符串
	 * 
	 * @param date
	 * @param dateFormat
	 * @return String
	 */
	public static String toString(java.util.Date date, String dateFormat) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> toString(java.util.Date date, String dateFormat)");
		}
		if ("".equals(date) || date == null) {
			return "bug: date is null";
		}
		DateFormat sdf = new SimpleDateFormat(dateFormat);
		String str = sdf.format(date);

		if (logger.isDebugEnabled()) {
			logger.debug("<<< toString return String");
		}
		return str;
	}

	/**
	 * 默认将日期转换为字符串，格式(yyyy-MM-dd)
	 * 
	 * @param date
	 * @return String
	 */
	public static String toString(java.util.Date date) {
		return toString(date, DEFAULT_FORMAT);
	}

	/**
	 * 默认将日期转换为字符串，格式(yyyyMMdd)
	 * 
	 * @param date
	 * @return String
	 */
	public static String toOrderIdString(java.util.Date date) {
		return toString(date, ORDERID_FORMAT);
	}

	/**
	 * 将日期转换为长整型?
	 * 
	 * @param date
	 * @return long
	 */
	public static long toLong(java.util.Date date) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>>toLong(java.util.Date date)");
		}
		if (date == null) {
			return 0;
		}
		long d = date.getTime();

		if (logger.isDebugEnabled()) {
			logger.debug("<<<toLong return long");
		}
		return d;
	}

	/**
	 * 将长整型转换为日期对象
	 * 
	 * @param time
	 * @return date
	 */
	public static java.util.Date toDate(long time) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>>toDate (long time)");
		}
		if ("".equals(time)) {
			return new Date();
		}
		Date date = new Date(time);

		if (logger.isDebugEnabled()) {
			logger.debug("<<<toDate return date");
		}
		return date;
	}
	
	/**
	 * 获得系统当前时间
	 * 
	 * @return java.util.Date
	 */
	public static String currentStringDate() {
		if (logger.isDebugEnabled()) {
			logger.debug(">>>currentDate()");
		}
		Date date = new Date();

		if (logger.isDebugEnabled()) {
			logger.debug("<<<currentDate() return date");
		}
		return toString(date);
	}

	/**
	 * 获得系统当前时间
	 * 
	 * @return java.util.Date
	 */
	public static String getBeforDay() {
		if (logger.isDebugEnabled()) {
			logger.debug(">>>before()");
		}
		Calendar cl = Calendar.getInstance();
		int day = cl.get(Calendar.DATE);  
	    cl.set(Calendar.DATE, day-1);  
		if (logger.isDebugEnabled()) {
			logger.debug("<<<currentDate() return date");
		}
		return toString(cl.getTime());
	}

	/**
	 * 获得系统当前时间(按用户自己格式)
	 * 
	 * @return java.util.Date
	 */
	public static String currentYourDate(String formate) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>>currentDate()");
		}
		Date date = new Date();

		if (logger.isDebugEnabled()) {
			logger.debug("<<<currentDate() return date");
		}
		return toString(date, formate);
	}

	/**
	 * 获得系统当前时间
	 * 
	 * @return java.util.Date
	 */
	public static java.util.Date currentDate() {
		if (logger.isDebugEnabled()) {
			logger.debug(">>>currentDate()");
		}
		Date date = new Date();

		if (logger.isDebugEnabled()) {
			logger.debug("<<<currentDate() return date");
		}
		return date;
	}

	/**
	 * 根据日历的规则，为给定的日历字段添加或减去指定的时间�?
	 * 
	 * @param field
	 *            指定的日历字段
	 * @param date
	 *            需要操作的日期对象
	 * @param value
	 *            更改的时间值
	 * @return java.util.Date
	 */
	public static Date add(int field, Date date, int value) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>>add(int field,Date date,int value)");
		}
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(field, value);
		Date newDate = ca.getTime();

		if (logger.isDebugEnabled()) {
			logger.debug("<<<add() return date");
		}
		return newDate;
	}

	/**
	 * 返回给定日历字段的值
	 * 
	 * @param field
	 *            指定的日历字段
	 * @param date
	 *            给定的日期对象
	 * @return java.util.Date
	 */
	public static int get(int field, Date date) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>>get(int field, Date date");
		}
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		int value = ca.get(field);

		if (logger.isDebugEnabled()) {
			logger.debug("<<<get() return date");
		}
		return value;
	}

	/**
	 * 返回前N个月的日期值
	 * 
	 * @param month
	 * @return
	 */
	public static Date getLastMonth(String month) {
		Calendar ca = Calendar.getInstance();
		int m = 0;
		try {
			m = Integer.parseInt(month);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		ca.add(Calendar.MONTH, -m);
		return ca.getTime();
	}

	/**
	 * 格式化为 yyyy-MM-dd HH:mm:ss 格式
	 * 
	 * @author tom_zhao
	 * @param time
	 * @return
	 */
	public static String dateToString(Date time) {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ctime = formatter.format(time);
		return ctime;
	}

	public static int getDateDiff() {

		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		String time = df.format(new Date());
		Date d;
		try {
			d = df.parse("16:00:00");
			Date now = df.parse(time);
			return (now.getTime() - d.getTime()) > 0 ? 1 : 0;
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}

	}

	public static String getCurrentTime() {
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		return df.format(new Date());
	}

	public static Date getQuarterDayDate() {

		Date d = new Date();
		return new Date(d.getTime() - 90 * 24 * 60 * 60 * 1000);
	}

	/**
	 * 格式化日期字符串，显示年月日
	 * @author weizhifang
	 * @since 2015-8-3
	 * @param time
	 * @return
	 */
	public static String stringDateFormat(String time) { 
		Date d = parse(time); 
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
		time = formatter.format(d); 
		return time; 
	}
	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2015);
		calendar.set(Calendar.MONTH, 1);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));  
		System.out.println(toString(calendar.getTime()));
	}


	/**
	 * 计算两个日期相关天数
	 * @author weizhifang
	 * @since 2015-11-19
	 * @param fDate
	 * @param oDate
	 * @return
	 */
	public static int daysOfTwo(Date fDate, Date oDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(fDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(oDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return day2 - day1;
    }
	
	/**
	 * 默认将日期转换为字符串，格式(yyyy-MM-dd HH:mm:ss)
	 * 
	 * @param date
	 * @return String
	 */
	public static String formatDateToString(java.util.Date date) {
		return toString(date, DEFAULT_TIME_FORMAT);
	}

	/**
	 * 
	 * 返回两个日期的日间隔数
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static List<String> getDateDayArrayByParams(String beginTime,String endTime){
		ArrayList<String> result = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化为年月
		try{
			Calendar min = Calendar.getInstance();
			Calendar max = Calendar.getInstance();
			min.setTime(sdf.parse(beginTime));
			max.setTime(sdf.parse(endTime));
			Calendar curr = min;
			while (curr.before(max)) {
				result.add(sdf.format(curr.getTime()));
				curr.add(Calendar.DAY_OF_YEAR, 1);
			}
			result.add(sdf.format(curr.getTime()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 
	 * 返回两个日期的日间隔数
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static List<String> getDateMonthArrayByParams(String beginTime,String endTime){
		ArrayList<String> result = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月
		try{
			Calendar min = Calendar.getInstance();
			Calendar max = Calendar.getInstance();
			min.setTime(sdf.parse(beginTime));
			max.setTime(sdf.parse(endTime));
			Calendar curr = min;
			while (curr.before(max)) {
				result.add(sdf.format(curr.getTime()));
				curr.add(Calendar.MONTH, 1);
			}
			result.add(sdf.format(max.getTime()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 判断传入的时间是不是今天
	 * @param date
	 * @return
	 */
	public static boolean isToday(Date date) { 
	    Calendar c1 = Calendar.getInstance();              
	    c1.setTime(date);                                 
	    int year1 = c1.get(Calendar.YEAR);
	        int month1 = c1.get(Calendar.MONTH)+1;
	        int day1 = c1.get(Calendar.DAY_OF_MONTH);     
	        Calendar c2 = Calendar.getInstance();    
	        c2.setTime(new Date());      
	        int year2 = c2.get(Calendar.YEAR);
	        int month2 = c2.get(Calendar.MONTH)+1;
	        int day2 = c2.get(Calendar.DAY_OF_MONTH);   
	        if(year1 == year2 && month1 == month2 && day1 == day2){
	        return true;
	        }
	    return false;                               
	}
	
}
