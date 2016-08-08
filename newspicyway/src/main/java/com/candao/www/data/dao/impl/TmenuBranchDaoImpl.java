package com.candao.www.data.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.common.dao.DaoSupport;
import com.candao.www.data.dao.TmenuBranchDao;
import com.candao.www.data.model.TmenuBranch;
@Repository
public class TmenuBranchDaoImpl implements TmenuBranchDao {
	@Autowired
	private DaoSupport daoSupport;

	@Override
	public int addTmenuBranch(List<TmenuBranch> list) {
		// TODO Auto-generated method stub
		return daoSupport.insert(PREFIX+".insertOnce", list);
	}

	@Override
	public int delTmenuBranch(String menuid) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("menuid", menuid);
		return daoSupport.delete(PREFIX+".delete", params);
	}

	@Override
	public List<TmenuBranch> getTmenuBranchBymenuId(String menuid) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("menuid", menuid);
		return daoSupport.find(PREFIX+".find", params);
	}

	@Override
	public List<Map<String, Object>> getBranchDetailBymenuId(String menuid) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("menuid", menuid);
		return daoSupport.find(PREFIX+".getBranchDetailBymenuId", params);
	}

	@Override
	public List<Integer> getBranchIdBymenuId(String menuid) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("menuid", menuid);
		return daoSupport.find(PREFIX+".getBranchIdBymenuId", params);
	}

}
