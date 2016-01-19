package com.candao.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;  
import java.util.ArrayList;  
import java.util.Calendar;  
import java.util.Date;  
import java.util.HashMap;
import java.util.List;  
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import com.candao.common.utils.timeSlotUtils;
  
public class FindDates {  
  
    public static void main(String[] args) throws Exception { 
    	  Map<String, Object> map1 = new HashMap();
//   map ={"inserttime=1"};
    	System.out.println(map1);
//		Map<String, Object> maps = new HashMap<String, Object>();
//		
//		
//		
//		String index=(String) maps.get("index");
//	
//		
//		
//        Calendar cal = Calendar.getInstance();  
//        String start = "2014-02-01";  
//        String end = "2014-03-02";  
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
//        Date dBegin = sdf.parse(start);  
//        Date dEnd = sdf.parse(end);  
////        List<String> lDate = findDates(dBegin, dEnd);  
//        for (String date : lDate) {  
//            System.out.println(sdf.format(date));  
//        }  
    }  
  
    public static List<Map<String, Object>> findDates(String dBegin, String dEnd) throws ParseException {  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	
		int i=1;
        List lDate = new ArrayList();  
//        lDate.add(sdf.format(dBegin));  
        Calendar calBegin = Calendar.getInstance();  
        // 使用给定的 Date 设置此 Calendar 的时间    
        calBegin.setTime(sdf.parse(dBegin));  
        Calendar calEnd = Calendar.getInstance();  
        // 使用给定的 Date 设置此 Calendar 的时间    
        calEnd.setTime(sdf.parse(dEnd));  
        // 测试此日期是否在指定日期之后    
        while (sdf.parse(dEnd).after(calBegin.getTime())) {  
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量    
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            Map<String, Object> map1 = new HashMap();
            map1.put("inserttime", sdf.format(calBegin.getTime()));
            lDate.add(map1);  
            
        }  
        return lDate;  
    }  
    
}  
