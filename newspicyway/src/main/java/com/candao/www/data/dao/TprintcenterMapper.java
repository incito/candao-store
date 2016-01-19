package com.candao.www.data.dao;

import com.candao.www.data.model.Tprintcenter;

public interface TprintcenterMapper {
    int deleteByPrimaryKey(String id);

    int insert(Tprintcenter record);

    int insertSelective(Tprintcenter record);

    Tprintcenter selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Tprintcenter record);

    int updateByPrimaryKey(Tprintcenter record);
}