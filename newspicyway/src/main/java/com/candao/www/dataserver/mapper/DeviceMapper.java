package com.candao.www.dataserver.mapper;

import com.candao.www.dataserver.entity.Device;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ytq on 2016/3/17.
 */
public interface DeviceMapper {
    Integer save(Device device);

    int countByGroupAndId(@Param("group") String group, @Param("id") String id);

    void update(Device device);

    List<Device> getAllDevice();

    List<Device> getDeviceByUserId(String userId);

    Device getDeviceById(Integer id);
}
