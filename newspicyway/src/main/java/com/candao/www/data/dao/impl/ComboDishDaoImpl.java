package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.ComboDishDao;
import com.candao.www.data.model.TcomboDishGroup;
import com.candao.www.data.model.TgroupDetail;
@Repository
public class ComboDishDaoImpl implements ComboDishDao {
	@Autowired
	private DaoSupport daoSupport;
	@Override
	public List<Map<String,Object>> getTdishGroupList(String dishid) {
		Map<String,Object> parame =new HashMap<String,Object>();
		parame.put("dishid", dishid);
		return daoSupport.find(PREFIX+".getTdishGroupList", parame);
	}

	@Override
	public List<Map<String,Object>> getTgroupDetailList(String tdishGroupid) {
		Map<String,Object> parame =new HashMap<String,Object>();
		parame.put("groupid", tdishGroupid);
		return daoSupport.find(PREFIX+".getTgroupDetailList", parame);
	}

	@Override
	public int saveGroup(TcomboDishGroup group) {
         return daoSupport.insert(PREFIX + ".saveGroup", group);
		
	}

	@Override
	public int saveGroupDetail(TgroupDetail detail) {
		  return daoSupport.insert(PREFIX + ".saveGroupDetail", detail);
		
	}

	@Override
	public int deleteGroup(String dishId) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", dishId);
			return daoSupport.delete(PREFIX + ".deleteGroup", params);
	}

	@Override
	public int deleteGroupDetail(String dishId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", dishId);
		return daoSupport.delete(PREFIX + ".deleteGroupDetail", params);
	}

	@Override
	public List<Map<String, Object>> getFishPotDetailList(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		 return daoSupport.find(PREFIX+".getFishPotDetailList", map);
	}

	@Override
	public List<Map<String, Object>> getFishPotDetailPad(Map<String, Object> map) {
		// TODO Auto-generated method stub
		 return daoSupport.find(PREFIX+".getFishPotDetailPad", map);
	}

	@Override
	public List<Map<String, Object>> ifDishesDetail(Map<String, Object> map) {
		// TODO Auto-generated method stub
		 return daoSupport.find(PREFIX+".ifDishesDetail", map);
	}
	
}
