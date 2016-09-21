package com.candao.inorder.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.inorder.dao.DaoSupport;
import com.candao.inorder.dao.InorderStationDao;
import com.candao.inorder.pojo.TblStation;

@Repository
public class InorderStationDaoImpl implements InorderStationDao {

	@Autowired
	private DaoSupport daoSupport;

	@Override
	public List<TblStation> tblStations(Map<String, String> params) {
		return daoSupport.find(PREFIX+".listStations", params);
	}

	@Override
	public TblStation tblStation(String  params) {
	
		return 	daoSupport.getOne(PREFIX+".queryByStation", params);
	}

	@Override
	public int updateStation(TblStation tblStation) {
		return daoSupport.update(PREFIX+".updateStation", tblStation);
	}

}
