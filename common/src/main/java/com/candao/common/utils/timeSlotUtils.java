package com.candao.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class timeSlotUtils {
	static Map<String, Object> map = new HashMap<String, Object>();
  	static String begintime="";
  	static String endtime="";
   private static final int FIRST_DAY = Calendar.MONDAY;
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
   static SimpleDateFormat dateFormat3 = new SimpleDateFormat("HHmm");
   public static  HashMap<String, Object> choose(int num){
   	
   	switch(num){
   	case 0:today(); break;
   	case 1: thisWeek();break;
   	case 2:thisMonth();break;
   	case 3:thisMonth();break;
   	}
   	map.put("begintime",begintime);
   	map.put("endtime",endtime);
		return (HashMap<String, Object>) map;
   }
   private static void today(){
   	 Calendar calendar1 = Calendar.getInstance();
   	dateFormat.format(calendar1.getTime());
   	 begintime=dateFormat.format(calendar1.getTime());
   	calendar1.add(Calendar.DAY_OF_MONTH, 1);
   	endtime=dateFormat.format(calendar1.getTime());
   	calendar1.add(Calendar.DAY_OF_MONTH, 0);
   	 endtime=begintime; 
   }
   private static void thisDay(){
	    Calendar calendar2 = Calendar.getInstance();
	   	calendar2.set(Calendar.DATE,1);
	       begintime=dateFormat3.format(calendar2.getTime());
	       calendar2.add(Calendar.MONTH, 1);
	       calendar2.set(Calendar.DAY_OF_MONTH,0);
	       endtime=dateFormat3.format(calendar2.getTime());
	       calendar2.add(Calendar.MONTH, 0);
   }
   private static void thisMonth(){
	   //要区分Calendar的add与set方法
   	Calendar calendar2 = Calendar.getInstance();
   	calendar2.set(Calendar.DAY_OF_MONTH,1);
       begintime=dateFormat.format(calendar2.getTime());
       calendar2.add(Calendar.MONTH, 1);
       calendar2.set(Calendar.DAY_OF_MONTH,1);
       endtime=dateFormat.format(calendar2.getTime());
       calendar2.add(Calendar.MONTH, 0);
       
   }
   private static void thisWeek(){
   	Calendar calendar3 = Calendar.getInstance();
   	while (calendar3.get(Calendar.DAY_OF_WEEK) != FIRST_DAY) {
           calendar3.add(Calendar.DATE, -1);
       }
       begintime=dateFormat.format(calendar3.getTime());
       calendar3.add(Calendar.DATE, 7);
       endtime=dateFormat.format(calendar3.getTime());
       calendar3.add(Calendar.DATE, 0);
   }
   public static String getNextDay() {
       Calendar cal = Calendar.getInstance();
       cal.setTime(new Date());
       cal.set(Calendar.HOUR_OF_DAY, 0);
       cal.set(Calendar.MINUTE, 0);
      // cal.set(Calendar.SECOND, 0);
      // cal.set(Calendar.MILLISECOND, 0);
//       cal.add(Calendar.DAY_OF_MONTH, 1);
       return  dateFormat3.format(cal.getTime());
   }
   public static String getHalfDay(int hour) {
       Calendar cal = Calendar.getInstance();
       cal.setTime(new Date());
       cal.set(Calendar.HOUR_OF_DAY, hour);
       cal.set(Calendar.MINUTE, 0);
      // cal.set(Calendar.SECOND, 0);
      // cal.set(Calendar.MILLISECOND, 0);
//       cal.add(Calendar.DAY_OF_MONTH, 0);
       return  dateFormat3.format(cal.getTime());
   }
//   public static String getNextDay() {  
//       Calendar calendar = Calendar.getInstance();  
//       calendar.setTime(new Date());
//       calendar.add(Calendar.DAY_OF_MONTH, 1);  
//       
//       return dateFormat3.format(calendar.getTime());  
//   }  
	public static void main(String[] args) {
//		 SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
//	  	 Calendar calendar1 = Calendar.getInstance();
//	 
//	    	 begintime=dateFormat2.format(calendar1.getTime());
//	  
//		System.out.println(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
	
		Map map =choose(0);
	System.out.println(map.get("begintime"));
	System.out.println(map.get("endtime"));
	map =choose(1);
System.out.println(map.get("begintime"));
System.out.println(map.get("endtime"));
 map =choose(2);
System.out.println(map.get("begintime"));
System.out.println(map.get("endtime"));
 map =choose(3);
System.out.println(map.get("begintime"));
System.out.println(map.get("endtime"));
//	System.out.println(getTimeOf12());
	System.out.println(getHalfDay(16));
	System.out.println(getNextDay());
	
	
	}

}
