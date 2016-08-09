package com.candao.www.data.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TbMessageInstrumentDao;
import com.candao.www.data.model.TbMessageInstrument;
@Repository
public class TbMessageInstrumentDaoImpl implements TbMessageInstrumentDao{
    @Autowired
    private DaoSupport dao;
	@Override
	public TbMessageInstrument get(Map<String,Object> params) {
		// TODO Auto-generated method stub
		return dao.get(PREFIX + ".get", params);
	}

	@Override
	public <K, V> Map<K, V> findOne(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T, K, V> List<T> find(Map<K, V> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(TbMessageInstrument tbMessageInstrument) {
		return dao.insert(PREFIX + ".insert", tbMessageInstrument);
	}

	@Override
	public int update(TbMessageInstrument tbMessageInstrument) {
		return this.dao.update(PREFIX+".update", tbMessageInstrument);
	}

	@Override
	public int delete(Map params) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateStatus(TbMessageInstrument tbMessageInstrument) {
		// TODO Auto-generated method stub
		return 0;
	}

}
