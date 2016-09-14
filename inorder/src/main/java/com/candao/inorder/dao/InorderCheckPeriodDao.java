package com.candao.inorder.dao;

import com.candao.inorder.pojo.TblCheckperiod;

public interface InorderCheckPeriodDao {
	public final static String PREFIX = InorderCheckPeriodDao.class.getName();
     public TblCheckperiod queryCurrentPeriod();
}
