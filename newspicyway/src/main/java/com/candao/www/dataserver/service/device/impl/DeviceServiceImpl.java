package com.candao.www.dataserver.service.device.impl;

import com.candao.www.dataserver.entity.Device;
import com.candao.www.dataserver.mapper.DeviceMapper;
import com.candao.www.dataserver.service.device.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ytq on 2016/3/17.
 */
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public Integer save(Device device) {
        return deviceMapper.save(device);
    }

    @Override
    public void update(Device device) {
        deviceMapper.update(device);
    }

    @Override
    public boolean isExistDevice(String group, String id) {
        return deviceMapper.countByGroupAndId(group, id) > 0;
    }

    public void saveOrUpdateDevice(Device device) {
        if (deviceMapper.countByGroupAndId(device.getDeviceGroup(), device.getDeviceId()) > 0) {
            deviceMapper.update(device);
        } else {
            deviceMapper.save(device);
        }
    }

    @Override
    public Device getDeviceById(Integer id) {
        return deviceMapper.getDeviceById(id);
    }

    @Override
    public void handler(String msg) {
    }
}
