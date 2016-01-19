package com.candao.www.data.dao.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.ComplainDao;
import com.candao.www.webroom.model.Complain;
@Repository
public class ComplainDaoImpl implements ComplainDao {
	@Autowired
	private DaoSupport daoSupport;

	@Override
	public int saveComplain(Complain complain) {
		return daoSupport.insert(PREFIX + ".saveComplain", complain);
	}
	
	@Override
	public int saveComplainType(Map<String,String> paramMap) {
		return daoSupport.insert(PREFIX + ".saveComplainType", paramMap);
	}
	
}
