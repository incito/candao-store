package com.candao.www.webroom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TbOpenBizLogDao;
import com.candao.www.data.model.TbOpenBizLog;
import com.candao.www.webroom.service.OpenBizService;

@Service
public class OpenBizServiceImpl implements OpenBizService{

	
	@Autowired
	TbOpenBizLogDao tbOpenBizLogDao;
	
	
	@Override
	public TbOpenBizLog getOpenBizLog() {
		return tbOpenBizLogDao.findOpenBizDate();
	}

	@Override
	public int deleteOpenBizLog() {
		return tbOpenBizLogDao.deleteOpenBizLog();
	}

	@Override
	public int insertOpenBizLog(TbOpenBizLog tbOpenBizLog) {
		return tbOpenBizLogDao.insertOpenBizLog(tbOpenBizLog);
	}

}
