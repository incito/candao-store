package com.candao.inorder.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.candao.inorder.dao.DaoSupport;
import com.candao.inorder.dao.InorderCheckPeriodDao;
import com.candao.inorder.pojo.TblCheckperiod;

@Repository
public class InorderCheckPeriodDaoImpl implements InorderCheckPeriodDao {

	@Autowired
	private DaoSupport daoSupport;

	@Override
	public TblCheckperiod queryCurrentPeriod() {
		return daoSupport.findOne(PREFIX + ".queryCurrentPeriod");
	}

}
