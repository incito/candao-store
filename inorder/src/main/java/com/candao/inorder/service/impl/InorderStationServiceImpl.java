package com.candao.inorder.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.inorder.dao.InorderStationDao;
import com.candao.inorder.pojo.TblStation;
import com.candao.inorder.service.InorderStationService;

/**
 * 
 * @author Candao
 *吉旺工作站点查询
 */
@Service
public class InorderStationServiceImpl implements InorderStationService  {

	@Autowired
	private InorderStationDao  inorderStationDao;
	
	@Override
	public List<TblStation> tblStations(Map<String, String> params) {
		return inorderStationDao.tblStations(params);
	}

	@Override
	public TblStation tblStation(Map<String, String> params) {
		return inorderStationDao.tblStation(null);
	}

	@Override
	public int updateStation(Map<String, String> params) {
		return inorderStationDao.updateStation(null);
	}


}
