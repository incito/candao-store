package com.candao.www.dataserver.util;

import com.candao.www.dataserver.entity.Device;
import com.candao.www.dataserver.service.device.obj.DeviceObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ytq on 2016/4/8.
 */
public final class BeanUtilEx
        extends BeanUtils {

    private static Map cache = new HashMap();
    private static Log logger = LogFactory.getFactory().getInstance(BeanUtilEx.class);

    private BeanUtilEx() {
    }

    static {
        //注册sql.date的转换器，即允许BeanUtils.copyProperties时的源目标的sql类型的值允许为空
        ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
        //ConvertUtils.register(new SqlTimestampConverter(), java.sql.Timestamp.class);
        //注册util.date的转换器，即允许BeanUtils.copyProperties时的源目标的util类型的值允许为空
        ConvertUtils.register(new DateConvert(), java.util.Date.class);
    }

    public static void copyProperties(Object target, Object source) throws
            InvocationTargetException, IllegalAccessException {
        //update bu zhuzf at 2004-9-29
        //支持对日期copy

        org.apache.commons.beanutils.BeanUtils.copyProperties(target, source);

    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        Device device = new Device();
        device.setId(1);
        device.setCreateTime(new Date());
        DeviceObject deviceObject = new DeviceObject();
        BeanUtilEx.copyProperties(deviceObject, device);
        System.out.println();
    }
}