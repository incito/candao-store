package com.candao.www.data.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TSynSqlMapper;
import com.candao.www.webroom.model.SynSqlObject;

@Repository
public class TSynSqlDaoImpl implements TSynSqlMapper  {

	@Autowired
    private DaoSupport dao;
	
	@Override
	public int insert(SynSqlObject record) {
		return dao.insert(PREFIX + ".insert",  record);
	}

	@Override
	public <E, K, V> List<E> getSynSqlDetailByparams(Map<K, V> params) {
		return dao.find(PREFIX + ".getSynSqlDetailByparams", params);
	}

	@Override
	public int update(SynSqlObject record) {
		return dao.update(PREFIX + ".update", record);
	}

	public void synData(Map<String, Object> mapParam){
		  dao.find(PREFIX + ".syndata", mapParam);
	}

	@Override
	public int copyDataFromTemp() {
		return dao.update(PREFIX + ".copyDataFromTemp", null);
		
	}

	@Override
	public void deleteDataTemp() {
		  dao.delete(PREFIX + ".deleteDataTemp", null);
	}
}
