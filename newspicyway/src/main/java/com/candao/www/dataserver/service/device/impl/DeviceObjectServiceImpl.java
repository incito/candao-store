package com.candao.www.dataserver.service.device.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.candao.communication.vo.Response;
import com.candao.www.dataserver.entity.Device;
import com.candao.www.dataserver.mapper.DeviceMapper;
import com.candao.www.dataserver.model.DeviceData;
import com.candao.www.dataserver.service.device.DeviceObjectService;
import com.candao.www.dataserver.service.device.obj.DeviceObject;
import com.candao.www.dataserver.service.msghandler.MsgProcessService;
import com.candao.www.dataserver.util.BeanUtilEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ytq on 2016/3/23.
 */
@Service
public class DeviceObjectServiceImpl implements DeviceObjectService {
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private MsgProcessService msgProcessService;

    @Override
    public List<DeviceObject> getAllDevice() {
        LOGGER.info("### 查询所有设备 ###");
        List<DeviceObject> deviceObjectList = new ArrayList<>();
        copyDeviceToDeviceObject(deviceMapper.getAllDevice(), deviceObjectList);
        return deviceObjectList;
    }

    @Override
    public List<DeviceObject> getOnLineDevice(String group) {
        LOGGER.info("### 查询group={}所有在线设备 ###", group);
        DeviceData deviceData = new DeviceData();
        deviceData.setGroup(group);
        List<DeviceObject> deviceObjectList = new ArrayList<>();
        Response resp = msgProcessService.queryTerminals(JSON.toJSONString(deviceData));
        if ("0".equals(resp.getCode())) {
            List<DeviceData> deviceDataList = JSON.parseObject(resp.getData() + "", new TypeReference<ArrayList<DeviceData>>() {
            });
            for (DeviceData data : deviceDataList) {
                DeviceObject deviceObject = new DeviceObject();
                deviceObject.setDeviceGroup(data.getGroup());
                deviceObject.setDeviceId(data.getId());
                deviceObjectList.add(deviceObject);
            }
        }
        return deviceObjectList;
    }

    private void copyDeviceToDeviceObject(List<Device> devices, List<DeviceObject> deviceObjectList) {
        for (Device device : devices) {
            DeviceObject deviceObject = new DeviceObject();
            try {
                BeanUtilEx.copyProperties(deviceObject, device);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("### copyDeviceToDeviceObject error={} ###", e);
            }
            deviceObjectList.add(deviceObject);
        }
    }

    @Override
    public List<DeviceObject> getOnLineDevice() {
        LOGGER.info("### 查询所有在线设备 ###");
        Response resp = msgProcessService.queryTerminals("");
        List<DeviceObject> deviceObjectList = new ArrayList<>();
        if ("0".equals(resp.getCode())) {
            List<DeviceData> deviceDataList = JSON.parseObject(resp.getData() + "", new TypeReference<ArrayList<DeviceData>>() {
            });
            for (DeviceData data : deviceDataList) {
                DeviceObject deviceObject = new DeviceObject();
                deviceObject.setDeviceGroup(data.getGroup());
                deviceObject.setDeviceId(data.getId());
                deviceObjectList.add(deviceObject);
            }
        }
        return deviceObjectList;
    }

    @Override
    public DeviceObject getByGroupAndId(String group, String id) {
        Device device = deviceMapper.getByGroupAndId(group, id);
        DeviceObject deviceObject = new DeviceObject();
        if (device != null) {
            try {
                BeanUtilEx.copyProperties(deviceObject, device);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("### copyDeviceToDeviceObject error={} ###", e);
            }
        }
        return deviceObject;
    }

    @Override
    public DeviceObject getDeviceByMeId(String meid) {
        Device device = deviceMapper.getDeviceByMeId(meid);
        DeviceObject deviceObject = new DeviceObject();
        if (device != null) {
            try {
                BeanUtilEx.copyProperties(deviceObject, device);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("### copyDeviceToDeviceObject error={} ###", e);
            }
        }
        return deviceObject;
    }
}
