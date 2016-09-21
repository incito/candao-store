package com.candao.inorder.dao;

import com.candao.inorder.pojo.TblDayinfo;

/**
 * 
 * @author liangdong查询工作时间
 *
 */
public interface InorderDayInfoDao {
	public final static String PREFIX = InorderDayInfoDao.class.getName();
 public  TblDayinfo getActionDayIndo();
}
