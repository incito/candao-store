package com.candao.www.data.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TbInstrumentDao;
import com.candao.www.data.model.TbInstrument;
@Repository
public class TbInstrumentDaoImpl implements TbInstrumentDao{
    @Autowired
    private DaoSupport dao;
	@Override
	public TbInstrument get(Map params) {
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
	public int insert(TbInstrument tbInstrument) {
		return dao.insert(PREFIX + ".insert", tbInstrument);
	}

	@Override
	public int update(TbInstrument tbInstrument) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Map params) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateStatus(TbInstrument tbInstrument) {
		// TODO Auto-generated method stub
		return 0;
	}

}
