package com.candao.www.data.dao.impl;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.PaywaySetDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class PaywaySetDaoImpl implements PaywaySetDao {

    @Autowired
    private DaoSupport dao;

    @Override
    public <T> List<T> findAll() {
        return dao.find(PREFIX + ".findAll", null);
    }

    @Override
    public int insertBatch(List<Map<String, Object>> params) {
        return dao.insert(PREFIX + ".insertBatch", params);
    }

    @Override
    public int truncate() {
        return dao.delete(PREFIX + ".truncate", null);
    }
}
