package com.candao.inorder.dao;

import java.util.List;
import java.util.Map;

import com.candao.inorder.pojo.TblStation;

public interface InorderStationDao {
	public final static String PREFIX = InorderStationDao.class.getName();
	  public List<TblStation> tblStations(Map<String, String> params);
	  public TblStation tblStation(String  params);
	  public int updateStation(TblStation tblStation);
}
