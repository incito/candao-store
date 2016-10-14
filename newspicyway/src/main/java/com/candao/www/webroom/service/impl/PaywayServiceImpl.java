package com.candao.www.webroom.service.impl;

import com.candao.www.data.dao.PaywaySetDao;
import com.candao.www.webroom.service.PaywayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by liaoy on 2016/10/13.
 */
@Service
public class PaywayServiceImpl implements PaywayService {
    @Autowired
    private PaywaySetDao paywaySetDao;

    @Override
    public List<Map> getPayways() {
        return paywaySetDao.findAll();
    }

    @Override
    public int savePayways(List<Map<String, Object>> payways) {
        paywaySetDao.truncate();
        return paywaySetDao.insertBatch(payways);
    }
}
