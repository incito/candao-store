package com.candao.www.dataserver.util;

import org.apache.commons.beanutils.Converter;

import java.text.SimpleDateFormat;

/**
 * Created by ytq on 2016/4/8.
 */
public class DateConvert implements Converter {

    public Object convert(Class arg0, Object arg1) {
        return arg1;
    }
}
