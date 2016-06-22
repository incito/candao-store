package com.candao.www.data.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TReturnDishDetailDao;

import java.util.List;
import java.util.Map;

/**
 * 退菜明细表
 *
 * @author Administrator
 */
@Repository
public class TReturnDishDetailDaoImpl implements TReturnDishDetailDao {

    @Autowired
    private DaoSupport daoSupport;

    @Override
    public <T, K, V> List<T> getReturnDishList(Map<K, V> params) {
        return daoSupport.find(PREFIX + ".getReturnDishList", params);
    }

    @Override
    public <T, K, V> List<T> getPresentDishList(Map<K, V> params) {
        return daoSupport.find(PREFIX + ".getPresentDishList", params);
    }
}
