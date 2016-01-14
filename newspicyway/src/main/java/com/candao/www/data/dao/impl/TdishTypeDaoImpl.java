package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TdishTypeDao;
@Repository
public class TdishTypeDaoImpl implements TdishTypeDao {
	 @Autowired
	 private DaoSupport dao;
	@Override
	public int addDishType(List<Map<String, Object>> tdu) {
		// TODO Auto-generated method stub
		return dao.insert(PREFIX + ".insertOnce", tdu);
	}

	@Override
	public boolean delDishType(String dishid) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<>();
		map.put("dishid", dishid);
		return dao.delete(PREFIX + ".delDishType",map)>0;
	}
}
