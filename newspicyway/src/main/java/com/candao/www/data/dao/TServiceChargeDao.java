package com.candao.www.data.dao;

import java.util.Map;

import com.candao.www.data.model.TServiceCharge;
public interface TServiceChargeDao {
	public final static String PREFIX = TServiceChargeDao.class.getName();
	 int insertChargeInfo(TServiceCharge charge);
	 int updateChargeInfo(TServiceCharge chargeInfo);
	 TServiceCharge getChargeInfo(Map<String, Object> params);
	 int delChargeInfo(Map<String, Object> params);
	 
}
