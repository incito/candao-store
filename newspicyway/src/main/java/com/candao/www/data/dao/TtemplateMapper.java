package com.candao.www.data.dao;

import com.candao.www.data.model.Ttemplate;

public interface TtemplateMapper {
    int deleteByPrimaryKey(String id);

    int insert(Ttemplate record);

    int insertSelective(Ttemplate record);

    Ttemplate selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Ttemplate record);

    int updateByPrimaryKey(Ttemplate record);
}