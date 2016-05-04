package com.candao.www.data.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.PadConfigDao;
import com.candao.www.webroom.model.PadConfig;

@Repository
public class PadConfigDaoImpl implements PadConfigDao{

	@Autowired
	  private DaoSupport dao;
	
	@Override
	public int selectisExsit() {
		
		return dao.get(PREFIX+".selectisExsit", null);
	}

	@Override
	public int update(PadConfig padConfig) {
		return dao.update(PREFIX+".update", padConfig);
	}

	@Override
	public int insert(PadConfig padConfig) {
		
		return dao.insert(PREFIX+".insert", padConfig);
	}

}
