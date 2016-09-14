package com.candao.inorder.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.inorder.dao.DaoSupport;
import com.candao.inorder.dao.InorderDayInfoDao;
import com.candao.inorder.pojo.TblDayinfo;

@Repository
public class InorderDayInfoDaoImpl implements InorderDayInfoDao {
	
	
	@Autowired
	private DaoSupport daoSupport;

	@Override
	public TblDayinfo getActionDayIndo() {
		return daoSupport.findOne(PREFIX+".queryActiveDayInfo");
	}

}
