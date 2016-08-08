package com.candao.www.data.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TbUserInstrumentDao;
import com.candao.www.data.model.TbUserInstrument;
@Repository
public class TbUserInstrumentDaoImpl implements TbUserInstrumentDao{
    @Autowired
    private DaoSupport dao;
	@Override
	public TbUserInstrument get(Map params) {
		// TODO Auto-generated method stub
		return null;
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
	public int insert(TbUserInstrument tbUserInstrument) {
		return dao.insert(PREFIX + ".insert", tbUserInstrument);
	}

	@Override
	public int update(TbUserInstrument tbUserInstrument) {
		return this.dao.update(PREFIX+".update", tbUserInstrument);
	}
	
	@Override
	public int updateByid(TbUserInstrument tbUserInstrument) {
		return this.dao.update(PREFIX+".updateByid", tbUserInstrument);
	}
	
	@Override
	public int delete(Map params) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateStatus(TbUserInstrument tbUserInstrument) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<TbUserInstrument> findByParams(Map<String, Object> params) {
		return this.dao.find(PREFIX+".findByParams", params);
	}

	@Override
	public List<Map<String, Object>> findUseridByParams(
			Map<String, Object> params) {
		return this.dao.find(PREFIX+".findUseridByParams", params);
	}

}
