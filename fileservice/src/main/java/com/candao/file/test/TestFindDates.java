package com.candao.file.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;
  
public class TestFindDates {  
  
    public static void main(String[] args) throws Exception {  
    	List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		Map<String, Object> maps = new HashMap<String, Object>();
		
		
		maps= null;///timeSlotUtils.choose(1);
		String index=(String) maps.get("index");
		lists.add(maps);
		ModelAndView mav = new ModelAndView();
		mav.addObject(maps);
		
        Calendar cal = Calendar.getInstance();  
        String start = "2014-02-01";  
        String end = "2014-03-02";  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        Date dBegin = sdf.parse(start);  
        Date dEnd = sdf.parse(end);  
        List<Date> lDate = findDates(dBegin, dEnd);  
        for (Date date : lDate) {  
            System.out.println(sdf.format(date));  
        }  
    }  
  
    public static List<Date> findDates(Date dBegin, Date dEnd) {  
        List lDate = new ArrayList();  
        lDate.add(dBegin);  
        Calendar calBegin = Calendar.getInstance();  
        // 使用给定的 Date 设置此 Calendar 的时间    
        calBegin.setTime(dBegin);  
        Calendar calEnd = Calendar.getInstance();  
        // 使用给定的 Date 设置此 Calendar 的时间    
        calEnd.setTime(dEnd);  
        // 测试此日期是否在指定日期之后    
        while (dEnd.after(calBegin.getTime())) {  
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量    
            calBegin.add(Calendar.DAY_OF_MONTH, 1);  
            lDate.add(calBegin.getTime());  
        }  
        return lDate;  
    }  
    
}  
