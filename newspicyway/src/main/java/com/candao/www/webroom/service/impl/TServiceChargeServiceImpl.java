package com.candao.www.webroom.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TServiceChargeDao;
import com.candao.www.data.model.TServiceCharge;
import com.candao.www.webroom.service.TServiceChargeService;

@Service
public class TServiceChargeServiceImpl implements TServiceChargeService {
	private Logger logger = LoggerFactory.getLogger(TServiceChargeServiceImpl.class);

	@Autowired
	private TServiceChargeDao serviceChargeDao;
	@Override
	public int updateChargeInfo(TServiceCharge chargeInfo) {
		return serviceChargeDao.updateChargeInfo(chargeInfo);
	}

}
