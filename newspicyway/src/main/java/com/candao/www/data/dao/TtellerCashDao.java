package com.candao.www.data.dao;

import com.candao.www.data.model.TtellerCash;

import java.util.List;

public interface TtellerCashDao {

    public final static String PREFIX = TtellerCashDao.class.getName();

    /**
     * 获取未清机的POS列表
     *
     * @return
     */
    public List<TtellerCash> findUncleanPosList();

    public List<TtellerCash> selectNotClearByUserId(String userId, String ip);

}
