package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TdishUnitDao;
import com.candao.www.data.model.TdishUnit;
@Repository
public class TdishUnitDaoImpl implements TdishUnitDao {
    @Autowired
	private DaoSupport dao;

	@Override
	public int addDishUnit(List<TdishUnit> tdu) {
		// TODO Auto-generated method stub
		 return dao.insert(PREFIX + ".insertOnce", tdu);
	}

	@Override
	public boolean delDishUnit(String dishid) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<>();
		map.put("dishid", dishid);
		return dao.delete(PREFIX + ".delDishUnit",map)>0;
	}

	@Override
	public List<TdishUnit> getUnitsBydishId(String dishId) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<>();
		map.put("dishid", dishId);
		return dao.find(PREFIX+".getUnitsBydishId", map);
	}

	@Override
	public Map<String, String> getDishDetail(String unitId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getUnitHistorylist() {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<>();
		return dao.find(PREFIX+".getUnitHistorylist", map);
	}

}
