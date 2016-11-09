package com.candao.www.data.dao;


import java.util.List;
import java.util.Map;

/**
 * 数据访问接口
 */
public interface PaywaySetDao {
    public final static String PREFIX = PaywaySetDao.class.getName();

    public <T> List<T> findAll();

    public int insertBatch(List<Map<String, Object>> params);

    public int truncate();

}


