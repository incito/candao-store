package com.candao.www.data.pos;

import com.candao.www.data.model.TPrinterDevice;
import com.candao.www.data.model.TPrinterDeviceExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TPrinterDeviceMapper {
    long countByExample(TPrinterDeviceExample example);

    int deleteByExample(TPrinterDeviceExample example);

    int deleteByPrimaryKey(String deviceid);

    int insert(TPrinterDevice record);

    int insertSelective(TPrinterDevice record);

    List<TPrinterDevice> selectByExample(TPrinterDeviceExample example);

    TPrinterDevice selectByPrimaryKey(String deviceid);

    int updateByExampleSelective(@Param("record") TPrinterDevice record, @Param("example") TPrinterDeviceExample example);

    int updateByExample(@Param("record") TPrinterDevice record, @Param("example") TPrinterDeviceExample example);

    int updateByPrimaryKeySelective(TPrinterDevice record);

    int updateByPrimaryKey(TPrinterDevice record);
}