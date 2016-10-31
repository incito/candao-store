package com.candao.www.bossstore.util;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: johnson
 * Date: 5/29/15
 * Time: 2:06 下午
 */
public class DateTimeUtils {



	/**
	 * 日期转字符串
	 * @author G/2015-1-15 下午2:32:54
	 * @param d
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String DateTimeToString(Date date){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}
	/**
	 * 字符串转日期时间
	 * @author G/2015-1-16 上午8:14:57
	 * @param day
	 * @return
	 */
	public static Date StrintToDateTime(String day){
		Date date = new Date();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		try {
			date = (Date)formatter.parse(day);
		} catch (ParseException e) {
		}
		return date;
	}
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

			day = Integer.parseInt((endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000) + "");
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
			List<String> dateList = new ArrayList();
			DateFormat format = new SimpleDateFormat(Stringformat);
			DateFormat outformat = new SimpleDateFormat(outStringformat);
			Date d = format.parse(beginTime);
			dateList.add(getFormatStringToString(beginTime));
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
		dateList.add(getFormatStringToString(beginTime));
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


	/**
	 * 获取当前日期的时间列表
	 * @return
	 */
	public static List<String> getCurrentDayList() {
		String currentDate = getCurrentDate();
		String[] dayformat=currentDate.split("-") ;
		String firstday = dayformat[0] + "-" + dayformat[1] + "-" + "1";
		return getDateDayArrayByParams(firstday, currentDate);
	}


	/**
	 * 获取当前日期
	 * @return
	 */
	 public static String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//get current date time with Date()
		Date date = new Date();
		//get current date time with Calendar()
		Calendar cal = Calendar.getInstance();
		 return dateFormat.format(cal.getTime());
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


	public static String getFormatStringToString(String date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date daystart = null;
		try {
			daystart = df.parse(date);
			Calendar start = Calendar.getInstance();
			start.setTime(daystart);
			DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
			//日期查询结束值
			return DATE_FORMAT.format(daystart);
		} catch (ParseException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		return "";
	}
	
	public static void main(String[] args){
        
		String branchId = "239776";

        Calendar calendar = Calendar.getInstance();
        
        int num = calendar.get(Calendar.MONTH);
        for(int i = 0 ; i <= num ; i ++){
        	calendar.set(Calendar.MONTH, i);
            calendar.set(Calendar.DATE, 1);
            String beginTime = DateUtils.toString(calendar.getTime())+" 00:00:00";
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            String endTime = DateUtils.toString(calendar.getTime())+" 23:59:59";
            String mouth = calendar.get(Calendar.MONTH)+1<10?""+(calendar.get(Calendar.MONTH)+1):(calendar.get(Calendar.MONTH)+1)+"";
            String date = calendar.get(Calendar.YEAR)+"-"+mouth+"-01";
            if(i==num){
            	endTime = DateTimeUtils.getCurrentDate()+" 23:59:59";
            }
            System.out.println(endTime+"<<<"+date);
        }
	}

}
