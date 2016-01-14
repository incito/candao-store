package com.candao.www.data.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TJsonRecordDao;
import com.candao.www.data.model.TJsonRecord;

@Repository
public class TJsonRecordDaoImpl implements TJsonRecordDao{

	
	@Autowired
	private DaoSupport dao;
	
	@Override
	public int insertJsonRecord(TJsonRecord jsonRecord) {
		return dao.insert(PREFIX + ".insertJsonRecord", jsonRecord);
	}

}
