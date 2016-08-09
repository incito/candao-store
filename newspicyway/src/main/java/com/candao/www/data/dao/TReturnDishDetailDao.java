package com.candao.www.data.dao;

import java.util.List;
import java.util.Map;

/**
 * 退菜明细表
 *
 * @author Administrator
 */
public interface TReturnDishDetailDao {

    String PREFIX = TReturnDishDetailDao.class.getName();

    <T, K, V> List<T> getReturnDishList(Map<K, V> params);

    <T, K, V> List<T> getPresentDishList(Map<K, V> params);

    List<Map<String, String>> getUserMapList();
}
