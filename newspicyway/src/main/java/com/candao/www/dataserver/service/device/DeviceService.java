package com.candao.www.dataserver.service.device;


import com.candao.www.dataserver.entity.Device;
import com.candao.www.dataserver.service.msghandler.MsgHandler;

/**
 * Created by ytq on 2016/3/17.
 */
public interface DeviceService extends MsgHandler {
    org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DeviceService.class);


    //保存或更新设备信息
    void saveOrUpdateDevice(Device device);

    //根据id查询设备信息
    Device getDeviceById(Integer id);

}
