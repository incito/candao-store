package com.candao.www.dataserver.util;

import com.candao.common.utils.DateUtils;
import com.candao.www.dataserver.service.SpringContextUtils;
import com.candao.www.dataserver.service.member.BusinessService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ytq on 2016/3/21.
 */
public class WorkDateUtil {
//    private static BusinessService businessService = SpringContextUtils.getBean("businessServiceImpl");

    public static Long parse(String dateString, String dateFormat) {

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
        return date.getTime();
    }

    public static String getWorkDate() {
        Date openDate = /*businessService.getOpenDate()*/null;
        if (null != openDate) {
            return DateUtils.toString(openDate, "yyyy-MM-dd");
        } else {
            Date workDate = new Date();
            if (workDate.getHours() < 6) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
                workDate = calendar.getTime();
            }
            return DateUtils.toString(workDate, "yyyy-MM-dd");
        }
    }

    public static Date getWorkDate1() {
        String workDate = getWorkDate();
        return DateUtils.parse(workDate, "yyyy-MM-dd");
    }

    public static Date getTomorrowDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
        return calendar.getTime();
    }

    public static Date getAfterHour(int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + hour);
        return calendar.getTime();
    }

    public static void main(String[] args) {
        System.out.println(WorkDateUtil.parse("20160425 17:52:31", "yyyyMMdd HH:mm:ss"));
    }
}
