package com.candao.www.data.pos;

import com.candao.www.data.model.TPrinterDeviceprinter;
import com.candao.www.data.model.TPrinterDeviceprinterExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TPrinterDeviceprinterMapper {
    long countByExample(TPrinterDeviceprinterExample example);

    int deleteByExample(TPrinterDeviceprinterExample example);

    int deleteByPrimaryKey(String id);

    int insert(TPrinterDeviceprinter record);

    int insertSelective(TPrinterDeviceprinter record);

    List<TPrinterDeviceprinter> selectByExample(TPrinterDeviceprinterExample example);

    TPrinterDeviceprinter selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TPrinterDeviceprinter record, @Param("example") TPrinterDeviceprinterExample example);

    int updateByExample(@Param("record") TPrinterDeviceprinter record, @Param("example") TPrinterDeviceprinterExample example);

    int updateByPrimaryKeySelective(TPrinterDeviceprinter record);

    int updateByPrimaryKey(TPrinterDeviceprinter record);
}