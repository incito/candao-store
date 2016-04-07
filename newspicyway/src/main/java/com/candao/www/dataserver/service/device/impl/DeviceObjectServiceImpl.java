package com.candao.www.dataserver.service.device.impl;

import com.alibaba.fastjson.JSON;
import com.candao.www.dataserver.entity.Device;
import com.candao.www.dataserver.mapper.DeviceMapper;
import com.candao.www.dataserver.model.DeviceData;
import com.candao.www.dataserver.model.MsgResponseData;
import com.candao.www.dataserver.service.device.DeviceObjectService;
import com.candao.www.dataserver.service.device.obj.DeviceObject;
import com.candao.www.dataserver.service.msghandler.MsgProcessService;
import com.candao.www.dataserver.util.MsgAnalyzeTool;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String resp = msgProcessService.queryTerminals(JSON.toJSONString(deviceData));
        MsgResponseData msgResponseData = MsgAnalyzeTool.analyzeQueryTerminals(resp);
        List<DeviceObject> deviceObjectList = new ArrayList<>();
        for (DeviceData data : msgResponseData.getData()) {
            DeviceObject deviceObject = new DeviceObject();
            deviceObject.setDeviceGroup(data.getGroup());
            deviceObject.setDeviceId(data.getId());
            deviceObjectList.add(deviceObject);
        }
        return deviceObjectList;
    }

    private void copyDeviceToDeviceObject(List<Device> devices, List<DeviceObject> deviceObjectList) {
        for (Device device : devices) {
            DeviceObject deviceObject = new DeviceObject();
            try {
                BeanUtils.copyProperties(deviceObject, device);
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
        String resp = msgProcessService.queryTerminals("");
        MsgResponseData msgResponseData = MsgAnalyzeTool.analyzeQueryTerminals(resp);
        List<DeviceObject> deviceObjectList = new ArrayList<>();
        for (DeviceData data : msgResponseData.getData()) {
            DeviceObject deviceObject = new DeviceObject();
            deviceObject.setDeviceGroup(data.getGroup());
            deviceObject.setDeviceId(data.getId());
            deviceObjectList.add(deviceObject);
        }
        return deviceObjectList;
    }

    @Override
    public List<Map> getDeviceByUserId(String userId) {
        List<Map> mapList = new ArrayList<>();
        for (Device device : deviceMapper.getDeviceByUserId(userId)) {
            Map map = new HashMap<>();
            map.put("group", device.getDeviceGroup());
            map.put("id", device.getDeviceId());
            map.put("tableNo", device.getTableNo());
            map.put("ssId", device.getSsId());
            mapList.add(map);
        }
        return mapList;
    }
}