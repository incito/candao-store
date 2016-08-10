package com.candao.www.data.dao.impl;

import com.candao.common.dao.DaoSupport;
import com.candao.common.log.LoggerFactory;
import com.candao.common.log.LoggerHelper;
import com.candao.www.data.dao.TtellerCashDao;
import com.candao.www.data.model.TtellerCash;
import com.candao.www.webroom.service.impl.ConvertVideoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TtellerCashDaoImpl implements TtellerCashDao {

    LoggerHelper logger = LoggerFactory.getLogger(ConvertVideoUtil.class);
    @Autowired
    private DaoSupport daoSupport;

    @Override
    public List<TtellerCash> findUncleanPosList() {
        return daoSupport.find(PREFIX + ".findUncleanPosList", null);
    }

    @Override
    public TtellerCash selectNotClearByUserId(String userId, String ip) {
        Map<String, String> param = new HashMap<>();
        param.put("userId", userId);
        param.put("ip", ip);
        return daoSupport.get(PREFIX + ".selectNotClearByUserId", param);
    }

    @Override
    public TtellerCash selectLastUser(String userId, String ip) {
        Map<String, String> param = new HashMap<>();
        param.put("userId", userId);
        param.put("ip", ip);
        return daoSupport.get(PREFIX + ".selectLastUser", param);
    }
}
