package com.candao.www.dataserver.util;

import com.candao.common.utils.DateUtils;
import com.candao.www.dataserver.service.SpringContextUtils;
import com.candao.www.dataserver.service.member.BusinessService;
import com.candao.www.dataserver.service.msghandler.MsgForwardService;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ytq on 2016/3/21.
 */
public class WorkDateUtil {
    private static BusinessService businessService = SpringContextUtils.getBean("businessServiceImpl");

    public static String getWorkDate() {
        Date openDate = businessService.getOpenDate();
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

    public static void main(String[] args) {
        System.out.println(WorkDateUtil.getWorkDate());
    }
}
