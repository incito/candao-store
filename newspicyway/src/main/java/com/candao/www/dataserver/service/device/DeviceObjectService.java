package com.candao.www.dataserver.service.device;


import com.candao.www.dataserver.service.device.obj.DeviceObject;

import java.util.List;
import java.util.Map;

/**
 * Created by ytq on 2016/3/23.
 */
public interface DeviceObjectService {
    org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DeviceService.class);

    //查询该门店所有设备
    List<DeviceObject> getAllDevice();

    //查询指定组的所有在线设备
    List<DeviceObject> getOnLineDevice(String group);

    //查询所有在线设备
    List<DeviceObject> getOnLineDevice();

    //根据用户id查询用户对应的机具信息
    List<Map> getDeviceByUserId(String userId);
}
