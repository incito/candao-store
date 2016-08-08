package com.candao.www.data.dao.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.ToperationLogDao;
import com.candao.www.data.model.ToperationLog;
@Repository
public class ToperationLogDaoImpl implements ToperationLogDao {
	@Autowired
	private DaoSupport daoSupport;

	@Override
	public int save(ToperationLog toperationLog) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public ToperationLog findByparams(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return daoSupport.get(PREFIX+".findByparams", map);
	}

	@Override
	public int deleteToperationLog(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int deleteToperationLogByTableNo(String tableNO) {
		// TODO Auto-generated method stub
		return 1;
	}

}
