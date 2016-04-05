package com.candao.www.dataserver.util;

import com.candao.common.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ytq on 2016/3/21.
 */
public class WorkDateUtil {
    public static String getWorkDate() {
        Date workDate = new Date();
        if (workDate.getHours() < 6) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
            workDate = calendar.getTime();
        }
        return DateUtils.toString(workDate, "yyyy-MM-dd");
    }

    public static Date getWorkDate1() {
        Date workDate = new Date();
        if (workDate.getHours() < 6) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
            workDate = calendar.getTime();
        }
        return workDate;
    }

    public static void main(String[] args) {
        System.out.println(WorkDateUtil.getWorkDate());
    }
}
