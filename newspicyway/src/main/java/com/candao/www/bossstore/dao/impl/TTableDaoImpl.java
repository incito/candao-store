package com.candao.www.bossstore.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.bossstore.dao.TTableDao;
@Repository
public class TTableDaoImpl implements TTableDao{
	@Autowired
	private DaoSupport daoSupport;
	@Override
	public List<Object[]> queryTablesStatus() {
		List<Object[]> result = new ArrayList<Object[]>();
		List<Map<String,Object>> list = daoSupport.find(PREFIX+".queryTablesStatus");
		for(Map<String,Object> map:list){
			Object[] objects = new Object[4];
			objects[0] = map.get("personNum");
			objects[1] = map.get("inUse");
			objects[2] = map.get("total");
			objects[3] = map.get("tableType");
			result.add(objects);
		}
		return result;
	}

	@Override
	public List<Object[]> queryService(Map<String,String> params) {
		List<Object[]> result = new ArrayList<Object[]>();
		List<Map<String,Object>> list = daoSupport.find(PREFIX+".queryService",params);
		for(Map<String,Object> map:list){
			Object[] objects = new Object[7];
			objects[0] = map.get("userId");
			objects[1] = map.get("name");
			objects[2] = map.get("typeId");
			objects[3] = map.get("startTime");
			objects[4] = map.get("endTime");
			objects[5] = map.get("outTime");
			objects[6] = map.get("response");
			result.add(objects);
		}
		return result;
	}

	@Override
	public Object[] queryTableInfoByTableId(Map<String,Object> params) {
		Object[] objects = new Object[2];
		Map<String,Object> map = daoSupport.findOne(PREFIX+".queryTableInfoByTableId", params);
		objects[0] = map.get("personNum");
		objects[1] = map.get("tableName");
		return objects;
	}

}
