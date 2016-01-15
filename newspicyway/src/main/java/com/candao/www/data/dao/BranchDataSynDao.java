package com.candao.www.data.dao;

import java.util.Map;



public interface BranchDataSynDao {
	public final static String PREFIX = BranchDataSynDao.class.getName();
	
	public  int checkBizData();
	
	public int checkSynDataFinish();
	
	public int insertSynRecord(Map<String, String> mapValue) ;
	
	public int updateSynRecord(Map<String, Object> mapValue) ;

	public void transferToHistory();
	
	public String getSynSql(String tableName,String sqlCondition);

	public void deleteSynRecord();

	public void updateBizLog();

	public Map<String, String> getBizDate();
}
