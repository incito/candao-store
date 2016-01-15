package com.candao.www.webroom.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.ToperationLogDao;
import com.candao.www.data.model.ToperationLog;
import com.candao.www.webroom.service.ToperationLogService;
@Service
public class ToperationLogServiceImpl implements ToperationLogService {
	@Autowired 
	private ToperationLogDao toperationLogDao;

	@Override
	public boolean save(ToperationLog toperationLog) {
		// TODO Auto-generated method stub
		return toperationLogDao.save(toperationLog)>0;

	}

	@Override
	public ToperationLog findByparams(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return toperationLogDao.findByparams(map);
	}

	@Override
	public int deleteToperationLog(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return toperationLogDao.deleteToperationLog(map);
	}

	@Override
	public int deleteToperationLogByTableNo(String tableNO) {
		// TODO Auto-generated method stub
		return toperationLogDao.deleteToperationLogByTableNo(tableNO);
	}

}
