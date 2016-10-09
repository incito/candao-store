package com.candao.www.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang.StringUtils;

import com.candao.common.utils.DateUtils;

/**
 * Created by IntelliJ IDEA.
 * User: johnson
 * Date: 5/29/15
 * Time: 2:06 下午
 */
public class DateTimeUtils {

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	/**
	 * 两个时间段相隔天数计算
	 * @param formattime
	 * @param beginDateTime
	 * @param endDateTime
	 * @return
	 * @throws java.text.ParseException
	 */
	public static int getsubtractDaynum(String formattime, String beginDateTime, String endDateTime)   {
		int day=0;
		try {

			SimpleDateFormat format = new SimpleDateFormat(formattime);
			Date beginDate = null;
			beginDate = format.parse(beginDateTime);
			Date endDate = format.parse(endDateTime);


			day = Integer.parseInt ((endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000)+"");
			return day;




		} catch (ParseException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		return day;
	}

	/**
	 * 通过时间字符串输出一个天数的列表
	 * @param beginTime
	 * @param size
	 * @param Stringformat
	 * @param outStringformat
	 * @return
	 */
	public static List<String> getdateList(String beginTime,int size,String Stringformat,String outStringformat)   {


		try {
			List<String> dateList = new ArrayList<>();
			DateFormat format = new SimpleDateFormat(Stringformat);
			DateFormat outformat = new SimpleDateFormat(outStringformat);
			Date d = format.parse(beginTime);
			dateList.add(getFormatStringToString(beginTime,"yyyy-MM-dd",outStringformat));
			for (int i = 0; i < size; i++) {
				GregorianCalendar gc =new GregorianCalendar();
				gc.setTime(d);
				gc.add(gc.DATE, i + 1);
				Date temp_date = gc.getTime();
				dateList.add(outformat.format(temp_date));
			}
			return dateList;
		} catch (ParseException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		return null;

	}

	/**
	 * 获取当前月份的最后一天
	 * @param month
	 * @param year
	 * @return
	 */
	public static String getMonthLastDay(int month, int year) {
		Calendar calendar = Calendar.getInstance();
		// passing month-1 because 0-->jan, 1-->feb... 11-->dec
		calendar.set(year, month - 1, 1);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		Date date = calendar.getTime();
		DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
		return DATE_FORMAT.format(date);
	}
	/**
	 * 获取当前月份的最后一天
	 * @param month
	 * @param year
	 * @return
	 */
	public static String getMonthLastTime(int month, int year) {
		Calendar calendar = Calendar.getInstance();
		// passing month-1 because 0-->jan, 1-->feb... 11-->dec
		calendar.set(year, month - 1, 1);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		Date date = calendar.getTime();
		DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
		return DATE_FORMAT.format(date);
	}
	/**
	 * 获取当前月份的最后一天
	 * @param month
	 * @param year
	 * @return
	 */
	public static String getMonthFirstDay(int month, int year) {
		Calendar calendar = Calendar.getInstance();
		// passing month-1 because 0-->jan, 1-->feb... 11-->dec
		calendar.set(year, month - 1, 1);
		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
		Date date = calendar.getTime();
		DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
		return DATE_FORMAT.format(date);
	}
	/**
	 * 获取当前月份的最后一天
	 * @param month
	 * @param year
	 * @return
	 */
	public static String getMonthFirstTime(int month, int year) {
		Calendar calendar = Calendar.getInstance();
		// passing month-1 because 0-->jan, 1-->feb... 11-->dec
		calendar.set(year, month - 1, 1);
		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
		Date date = calendar.getTime();
		DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
		return DATE_FORMAT.format(date);
	}

	/**
	 * 拼装营业分析查询参数
	 * @author weizhifang
	 * @since 2015-6-5
	 * @param params
	 * @return
	 */
	public static List<String> getDateArrayByParams(Map<String, Object> params){
		String beginTime = params.get("beginTime").toString();
		String endTime = params.get("endTime").toString();
		String dateType = params.get("Datetype").toString();
		List<String> dateList = new ArrayList<String>();
		if(dateType.equals("D")){
			dateList = getDateDayArrayByParams(beginTime,endTime);
		}
		if(dateType.equals("M")){
			dateList = getDateMonthArrayByParams(beginTime,endTime);
		}
		if(dateType.equals("Y")){
			dateList = getDateYearArrayByParams(beginTime,endTime);
		}
		return dateList;
	}

	/**
	 * 拼装营业分析查询参数日数据
	 * @author weizhifang
	 * @since 2015-6-5
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static List<String> getDateDayArrayByParams(String beginTime,String endTime){
		List<String> dateList = new ArrayList<String>();
		dateList.add(getFormatStringToString(beginTime,"yyyy-MM-dd","yyyy-MM-dd"));
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		int result = DateTimeUtils.getsubtractDaynum("yyyy-MM-dd",beginTime,endTime);
		try {
			Date daystart = df.parse(beginTime);
			Calendar start = Calendar.getInstance();
			start.setTime(daystart);
			//日期查询结束值
			Date dayend = df.parse(endTime);
			Calendar end = Calendar.getInstance();
			end.setTime(dayend);
			for(int i=0;i<result;i++){
				start.add(Calendar.DAY_OF_MONTH, 1);
				String dateStr = DateUtils.toString(start.getTime());
				dateList.add(dateStr);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateList;
	}

	/**
	 * 拼装营业分析查询参数月数据
	 * @author weizhifang
	 * @since 2015-6-5
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
			min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
			max.setTime(sdf.parse(endTime));
			max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
			Calendar curr = min;
			while (curr.before(max)) {
				result.add(sdf.format(curr.getTime()));
				curr.add(Calendar.MONTH, 1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 拼装营业分析查询参数年数据
	 * @author weizhifang
	 * @since 2015-6-5
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static List<String> getDateYearArrayByParams(String beginTime,String endTime){
		List<String> dateList = new ArrayList<String>();
		dateList.add(beginTime);
		DateFormat df = new SimpleDateFormat("yyyy");
		int result = DateTimeUtils.getsubtractDaynum("yyyy",beginTime,endTime);
		result = result/365;
		try {
			Date daystart = df.parse(beginTime);
			Calendar start = Calendar.getInstance();
			start.setTime(daystart);
			//日期查询结束值
			Date dayend = df.parse(endTime);
			Calendar end = Calendar.getInstance();
			end.setTime(dayend);

			for(int i=0;i<result;i++){
				start.add(Calendar.YEAR, 1);
				String dateStr = df.format(start.getTime());
				dateList.add(dateStr);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateList;
	}

	public static void main(String[] args) {
		DateTimeUtils t = new DateTimeUtils();
		Map<String,Object> map1 = new HashMap<String,Object>();
		map1.put("beginTime", "2015-05-01");
		map1.put("endTime", "2015-05-03");
		map1.put("DateType", "D");
		List<String> list1 = t.getDateArrayByParams(map1);
		for(String date : list1){
			System.out.println(date);
		}
		Map<String,Object> map2 = new HashMap<String,Object>();
		map2.put("beginTime", "2014-05");
		map2.put("endTime", "2015-05");
		map2.put("DateType", "M");
		List<String> list2 = t.getDateArrayByParams(map2);
		for(String date : list2){
			System.out.println(date);
		}
		Map<String,Object> map3 = new HashMap<String,Object>();
		map3.put("beginTime", "2012");
		map3.put("endTime", "2015");
		map3.put("DateType", "Y");
		List<String> list3 = t.getDateArrayByParams(map3);
		for(String date : list3){
			System.out.println(date);
		}
	}
	
	/**
	 * 格式化从数据库中取出来的值
	 * @author weizhifang
	 * @since 2015-6-7
	 * @param date
	 * @param datetype
	 * @return
	 */
	public static String formatDate(String date,String datetype){
		String dateStr = "";
		if (StringUtils.equals(datetype, "D")) {//日查询
			dateStr = "yyyy-MM-dd";
			
		}
		if (StringUtils.equals(datetype, "M")) {//月查询
			dateStr = "yyyy-MM";
		}
		if (StringUtils.equals(datetype, "Y")) {//月查询
			dateStr = "yyyy";
		}
		DateFormat dateFormat = new SimpleDateFormat(dateStr);
		return dateFormat.format(DateUtils.parse(date));
	}


	public static String getFormatStringToString(String date,String starformat, String format) {
		DateFormat df = new SimpleDateFormat(starformat);
		Date daystart = null;
		try {
			daystart = df.parse(date);
			Calendar start = Calendar.getInstance();
			start.setTime(daystart);
			DateFormat DATE_FORMAT = new SimpleDateFormat(format);
			//日期查询结束值
			return DATE_FORMAT.format(daystart);
		} catch (ParseException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		return "";
	}

	/**
	 * 按照指定的格式，将日期类型对象转换成字符串，例如：yyyy-MM-dd,yyyy/MM/dd,yyyy/MM/dd hh:mm:ss
	 *
	 * @param date
	 * @param pattern 格式
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat formater = new SimpleDateFormat(pattern);
		return formater.format(date);
	}

	/**
	 * 按照yyyy-MM-dd格式，将日期类型对象转换成字符串
	 *
	 * @param date
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		return dateFormat.format(date);
	}

	/**
	 * 按照指定的格式，将字符串转换成日期类型对象，例如：yyyy-MM-dd,yyyy/MM/dd,yyyy/MM/dd hh:mm:ss
	 *
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static Date parseDate(String dateStr, String pattern) {
		if (StringUtils.isEmpty(dateStr)) {
			return null;
		}
		SimpleDateFormat formater = new SimpleDateFormat(pattern);
		try {
			return formater.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将字符串（yyyy-MM-dd）解析成日期
	 *
	 * @param dateStr
	 * @return
	 */
	public static Date parseDate(String dateStr) {
		if (StringUtils.isEmpty(dateStr)) {
			return null;
		}
		try {
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
